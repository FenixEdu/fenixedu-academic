package net.sourceforge.fenixedu.domain.functionalities;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.CyclicModuleException;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import dml.runtime.RelationAdapter;

/**
 * The module is an aggregation of functionalities. It allows to enable
 * or disable a group of functionalities and composes functionalities
 * in an hierarchical structure.
 * 
 * @author cfgi
 */
public class Module extends Module_Base {
    
    //
    // Listeners
    //
    
    static {
        ModuleHasSubModules.addListener(new ModuleIsFunctionalityListener());
        ModuleAggregatesFunctionalities.addListener(new SomeFunctionalitiesAreModulesListener());
        ModuleAggregatesFunctionalities.addListener(new FunctionalitiesHaveAnOrderListener());
    }
 
    /**
     * Required default constructor.
     */
    protected Module() {
        super();
    }

    /**
     * @see Functionality#Functionality(MultiLanguageString, String)
     */
    public Module(MultiLanguageString name, String path) {
        this();
        
        setName(name);
        setPath(path);
    }
    
    /**
     * The prefix of the module, that is, the path that will preced any path
     * of a sub functinality that is relative. 
     * 
     * <p>
     * This is just a convenience method for {@link Functionality#getPath()}
     * 
     * @return
     */
    public String getPrefix() {
        return getPath();
    }

    /**
     * Returns all the functionalities under this module. It makes no
     * distintion between sub functionalities and modules. 
     */
    @Override
    public List<Functionality> getFunctionalities() {
        return super.getFunctionalities();
    }

    /**
     * This method orders all the sub functionalities according with their
     * order in the module.
     * 
     * @return all the sub functionalities ordered by their order in the module
     * 
     * @see Functionality#getOrder()
     */
    public List<Functionality> getOrderedFunctionalities() {
        return Functionality.sort(getFunctionalities());
    }
    
    @Override
    protected void checkDeletion() {
        // can be deleted
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        
        removeParent();

        // this also includes modules, see the relation listeners
        for (Functionality functionality : getFunctionalities()) {
            functionality.delete();
        }
    }

    //
    //
    //
    
    /**
     * This utility method finds top-level modules, that is, all modules
     * that don't have a parent.
     * 
     * @return the list of modules with no parent
     */
    public static List<Module> getTopLevelModules() {
        List<Module> modules = new ArrayList<Module>();
        
        for (Functionality functionality : RootDomainObject.getInstance().getFunctionalities()) {
            if (functionality instanceof Module) {
                Module module = (Module) functionality;
                
                if (module.getParent() == null) {
                    modules.add(module);
                }
            }
        }
        
        return modules;
    }
    
    /**
     * This relation listener is used to ensure that any module added/removed
     * from the {@link Module_Base#ModuleHasSubModules} relation is also added/removed from the 
     * {@link Module_Base#ModuleAggregatesFunctionalities} relation.
     * 
     * @author cfgi
     */
    private static class ModuleIsFunctionalityListener extends RelationAdapter<Module, Module> {

        @Override
        public void beforeAdd(Module self, Module other) {
            super.beforeAdd(self, other);

            for (Module parent = self; parent != null; parent = parent.getParent()) {
                if (parent == other) {
                    throw new CyclicModuleException();
                }
            }
        }

        @Override
        public void afterAdd(Module self, Module child) {
            super.afterAdd(self, child);

            if (self != null && child != null) {
                if (! self.getFunctionalities().contains(child)) {
                    self.addFunctionalities(child);
                }
            }
        }

        @Override
        public void afterRemove(Module self, Module child) {
            super.afterRemove(self, child);
            
            if (self != null && child != null) {
                if (self.getFunctionalities().contains(child)) {
                    self.removeFunctionalities(child);
                }
            }
        }
    }
    
    /**
     * This relation listener is used to ensure that any module added/removed
     * from the {@link Module_Base#ModuleAggregatesFunctionalities} relation is also 
     * added/removed from the {@link Module_Base#ModuleHasSubModules} relation.
     * 
     * @author cfgi
     */
    private static class SomeFunctionalitiesAreModulesListener extends RelationAdapter<Module, Functionality> {
        @Override
        public void afterAdd(Module self, Functionality child) {
            super.afterAdd(self, child);
            
            if (self != null && child != null) {
                if (child instanceof Module) {
                    if (! self.getModules().contains(child)) {
                        self.addModules((Module) child);
                    }
                }
            }
        }

        @Override
        public void afterRemove(Module self, Functionality child) {
            super.afterRemove(self, child);
            
            if (self != null && child != null) {
                if (child instanceof Module) {
                    if (self.getModules().contains(child)) {
                        self.removeModules((Module) child);
                    }
                }
            }
        }
    }
    
    /**
     * This listener ensures the order of all contained functionalities. Every added functionality is
     * placed in the end, that is, with the greates order. When a functionality is removed all other 
     * functionalities are updated so that there are no "holes" in the order.
     * 
     * @author cfgi
     */
    private static class FunctionalitiesHaveAnOrderListener extends RelationAdapter<Module, Functionality> {

        /**
         * Normalizes the order of all functionalities so that it correspondes to it's index
         * in the list returned by {@link Module#getOrderedFunctionalities(Module)}.
         * 
         * @param module the module to pack or <code>null</code> for the toplevel
         */
        private void pack(Module module) {
            List<Functionality> functionalities  = getOrderedFunctionalities(module);
            
            int index = 0;
            for (Functionality f : functionalities) {
                f.setOrderInModule(index++);
            }
        }

        private List<Functionality> getOrderedFunctionalities(Module module) {
            if (module == null) {
                return Functionality.getOrderedTopLevelFunctionalities();
            }
            else {
                return module.getOrderedFunctionalities();
            }
        }

        @Override
        public void afterRemove(Module self, Functionality functionality) {
            super.afterRemove(self, functionality);
            
            pack(self);
        }
        
        @Override
        public void afterAdd(Module self, Functionality functionality) {
            super.afterAdd(self, functionality);
            
            if (functionality == null) {
                return;
            }
            
            functionality.setOrderInModule(Integer.MAX_VALUE); // ensures last
            pack(self);
        }
    
    }
}
