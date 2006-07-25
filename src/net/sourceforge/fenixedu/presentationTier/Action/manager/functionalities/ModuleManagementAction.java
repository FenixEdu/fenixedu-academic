package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.utl.fenix.utils.Pair;

public class ModuleManagementAction extends FunctionalitiesDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        
        if (module == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }
        else {
            return viewModule(module, mapping, actionForm, request, response);
        }
    }
    
    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        
        if (module == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }
        else {
            setBreadCrumbs(request, module);
            request.setAttribute("module", module);
            return mapping.findForward("edit");
        }
    }
    
    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getParent(request);
        
        if (module == null) {
            return mapping.findForward("create.toplevel");
        }
        else {
            setBreadCrumbs(request, module, true);
            request.setAttribute("parent", module);
            return mapping.findForward("create");
        }
    }

    protected Module getParent(HttpServletRequest request) {
        return (Module) getObject(request, Module.class, "parent");
    }

    public ActionForward organize(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        String structure = request.getParameter("tree");

        if (structure == null || structure.length() == 0) {
            return viewModule(module, mapping, actionForm, request, response);
        }

        // structure =~ "([0-9]+-[0-9]+,)+" 
        // each repeating block is a pair with <initial position of child>-<initial position of parent>
        // meaning that child is now a child of parent.
        updateStructure(getTreeRoots(module), structure);
        
        return viewModule(module, mapping, actionForm, request, response);
    }
    
    private List<Functionality> getTreeRoots(Module module) {
        List<Functionality> roots = new ArrayList<Functionality>();
        
        roots.add(module); // root of tree
        
        if (module != null) {
            roots.addAll(module.getOrderedFunctionalities());
        }
        else {
            roots.addAll(Functionality.getOrderedTopLevelFunctionalities());
        }
        
        return roots;
    }

    /**
     * @throws ClassCastException if some parent referred in <tt>structure</tt> is not a {@link Module}
     * @throws IndexOutOfBoundsException if some index used in <tt>structure</tt> is not valid according to the given module
     * @throws NullPointerException if some index used in <tt>structure</tt> in not a number
     */
    private void updateStructure(List<Functionality> roots, String structure) throws Exception {
        List<Pair<Module, Functionality>> arrangements = new ArrayList<Pair<Module, Functionality>>();
        
        List<Functionality> functionalities = flatten(roots);
        
        String[] nodes = structure.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");
            
            Integer childIndex = getId(parts[0]);
            Integer parentIndex = getId(parts[1]);

            Module parent = (Module) functionalities.get(parentIndex);
            Functionality child = functionalities.get(childIndex);
         
            arrangements.add(new Pair<Module, Functionality>(parent, child));
        }
        
        Functionality.rearrangeFunctionalities(arrangements);
    }

    private List<Functionality> flatten(List<Functionality> roots) {
        List<Functionality> functionalities = new ArrayList<Functionality>();
        
        for (Functionality functionality : roots) {
            if (functionality instanceof Module) {
                functionalities.addAll(flatten((Module) functionality));
            }
            else {
                functionalities.add(functionality);
            }
        }
        
        return functionalities;
    }

    private List<Functionality> flatten(Module root) {
        List<Functionality> result = new ArrayList<Functionality>();
        
        result.add(root);
        for (Functionality functionality : root.getOrderedFunctionalities()) {
            if (functionality instanceof Module) {
                result.addAll(flatten((Module) functionality));
            }
            else {
                result.add(functionality);
            }
        }
        
        return result;
    }

}
