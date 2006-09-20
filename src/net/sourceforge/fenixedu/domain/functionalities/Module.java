package net.sourceforge.fenixedu.domain.functionalities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.CyclicModuleException;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import dml.runtime.RelationAdapter;

/**
 * The module is an aggregation of functionalities. It allows to enable or
 * disable a group of functionalities and composes functionalities in an
 * hierarchical structure.
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
    public Module(MultiLanguageString name, String prefix) {
        this();

        setName(name);
        setPrefix(prefix);
        changeUuid(Functionality.generateUuid());
    }

    public Module(UUID uuid, MultiLanguageString name, String prefix) {
        this();
        
        setName(name);
        setPrefix(prefix);
        changeUuid(uuid);
    }

    @Override
    public void setPrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            throw new FieldIsRequiredException("prefix", "functionalities.module.required.prefix");
        }

        super.setPrefix(prefix);
        Functionality.checkMatchPath();
    }

    /**
     * The public prefix is the part of the module that is visible by the client
     * and is prefixed to all the sub-functionalities of the module. The
     * difference to {@link #getPrefix() normal prefix} is that this prefix
     * consideres all the parent modules.
     * 
     * @return the prefix of this module as seen be the client
     */
    public String getPublicPrefix() {
        if (getParent() != null) {
            return getParent().getPublicPrefix() + getNormalizedPrefix();
        } else {
            return getPrefix();
        }
    }

    /**
     * @return the {@link #getPrefix() current prefix} but ensuring that starts
     *         but does not end with "/"
     */
    protected String getNormalizedPrefix() {
        String prefix = getPrefix();

        int end = prefix.endsWith("/") ? prefix.length() - 1 : prefix.length();
        return (prefix.startsWith("/") ? "" : "/") + prefix.substring(0, end);
    }

    /**
     * Returns all the functionalities under this module. It makes no distintion
     * between sub functionalities and modules.
     */
    @Override
    public List<Functionality> getFunctionalities() {
        return super.getFunctionalities();
    }

    /**
     * This method orders all the sub functionalities according with their order
     * in the module.
     * 
     * @return all the sub functionalities ordered by their order in the module
     * 
     * @see Functionality#getOrder()
     */
    public List<Functionality> getOrderedFunctionalities() {
        return Functionality.sort(getFunctionalities());
    }

    /**
     * A module is visible if one of it's sub functionalities is visible to the
     * user. This allows a user to see, for example, a top-level module that
     * deep in it's sub-functionalities tree has a visible functionality.
     * 
     * <p>
     * A module is also visible when it has an accessible public path. So if the
     * module has a public path but none of it's children is visible then it is still
     * visible to the user.
     */
    @Override
    public boolean isVisible(FunctionalityContext context) {
        if (! isVisible()) {
            return false;
        }
        
        if (getPublicPath() != null && isAvailable(context)) {
            return true;
        }
        
        for (Functionality child : getFunctionalities()) {
            if (child.isVisible(context)) {
                return true;
            }
        }

        return false;
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
     * This utility method finds top-level modules, that is, all modules that
     * don't have a parent.
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
     * from the {@link Module_Base#ModuleHasSubModules} relation is also
     * added/removed from the
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
                if (!self.getFunctionalities().contains(child)) {
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
     * from the {@link Module_Base#ModuleAggregatesFunctionalities} relation is
     * also added/removed from the {@link Module_Base#ModuleHasSubModules}
     * relation.
     * 
     * @author cfgi
     */
    private static class SomeFunctionalitiesAreModulesListener extends
            RelationAdapter<Module, Functionality> {
        @Override
        public void afterAdd(Module self, Functionality child) {
            super.afterAdd(self, child);

            if (self != null && child != null) {
                if (child instanceof Module) {
                    if (!self.getModules().contains(child)) {
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
     * This listener ensures the order of all contained functionalities. Every
     * added functionality is placed in the end, that is, with the greates
     * order. When a functionality is removed all other functionalities are
     * updated so that there are no "holes" in the order.
     * 
     * @author cfgi
     */
    private static class FunctionalitiesHaveAnOrderListener extends
            RelationAdapter<Module, Functionality> {

        /**
         * Normalizes the order of all functionalities so that it correspondes
         * to it's index in the list returned by
         * {@link Module#getOrderedFunctionalities(Module)}.
         * 
         * @param module
         *            the module to pack or <code>null</code> for the toplevel
         */
        private void pack(Module module) {
            List<Functionality> functionalities = getOrderedFunctionalities(module);

            int index = 0;
            for (Functionality f : functionalities) {
                f.setOrderInModule(index++);
            }
        }

        private List<Functionality> getOrderedFunctionalities(Module module) {
            if (module == null) {
                return Functionality.getOrderedTopLevelFunctionalities();
            } else {
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
