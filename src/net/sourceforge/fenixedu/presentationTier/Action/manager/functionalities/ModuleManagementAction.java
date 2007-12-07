package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.MatchPathConflictException;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.utl.fenix.utils.Pair;

public class ModuleManagementAction extends FunctionalitiesDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        
        if (module == null) {
            return viewRoot(mapping, actionForm, request, response);
        }
        else {
            return viewModule(module, mapping, actionForm, request, response);
        }
    }
    
    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        
        if (module == null) {
            return viewRoot(mapping, actionForm, request, response);
        }
        else {
            return forwardTo(mapping.findForward("edit"), request, module, false);
        }
    }
    
    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getParent(request);
        
        if (module == null) {
            return mapping.findForward("create.toplevel");
        }
        else {
            return forwardTo(mapping.findForward("create"), request, module, true);
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
        try {
            updateStructure(getTreeRoots(module), structure);
        }
        catch (MatchPathConflictException e) {
            processException(request, mapping, null, e);
        }

        return viewModule(module, mapping, actionForm, request, response);
    }
    
    private List<Content> getTreeRoots(Module module) {
        List<Content> roots = new ArrayList<Content>();
        
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
    private void updateStructure(List<Content> roots, String structure) throws Exception {
        List<Pair<Module, Content>> arrangements = new ArrayList<Pair<Module, Content>>();
        
        List<Content> functionalities = flatten(roots);
        
        String[] nodes = structure.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");
            
            Integer childIndex = getId(parts[0]);
            Integer parentIndex = getId(parts[1]);

            Module parent = (Module) functionalities.get(parentIndex);
            Content child = functionalities.get(childIndex);
         
            arrangements.add(new Pair<Module, Content>(parent, child));
        }
        
        rearrangeFunctionalities(arrangements);
    }

    private List<Content> flatten(List<Content> roots) {
        List<Content> functionalities = new ArrayList<Content>();
        
        for (Content functionality : roots) {
            if (functionality instanceof Module) {
                functionalities.addAll(flatten((Module) functionality));
            }
            else {
                functionalities.add(functionality);
            }
        }
        
        return functionalities;
    }

    private List<Content> flatten(Module root) {
        List<Content> result = new ArrayList<Content>();
        
        result.add(root);
        for (Content functionality : root.getOrderedChildren(Content.class)) {
            if (functionality instanceof Module) {
                result.addAll(flatten((Module) functionality));
            }
            else {
                result.add(functionality);
            }
        }
        
        return result;
    }

    public ActionForward importStructure(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        
        IViewState viewState = RenderUtils.getViewState("structure");
        RenderUtils.invalidateViewState("structure");
        
        if (viewState == null) {
            return viewModule(module, mapping, actionForm, request, response);
        }
        
        StructureBean bean = (StructureBean) viewState.getMetaObject().getObject();
        if (bean == null) {
            return viewModule(module, mapping, actionForm, request, response);
        }
        
        try {
            if (bean.isUuidUsed() && !bean.isCurrentParentUsed()) {
                importStartupFunctionalities(bean.getStream());
            }
            else { 
                importFunctionalities(module, bean.getStream(), bean.isPrincipalPreserved(), bean.isUuidUsed());
            }
        }
        catch (DomainException e) {
            addMessage(request, "error", e.getKey(), e.getArgs());
            return uploadStructure(mapping, actionForm, request, response);
        }
        catch (IOException e) {
            addMessage(request, "error", "functionalities.import.file.read.failed", new String[0]);
            return uploadStructure(mapping, actionForm, request, response);
        }
        catch (Exception e) {
            addMessage(request, "error", "functionalities.import.file.failed", e.getMessage());
            return uploadStructure(mapping, actionForm, request, response);
        }
        
        return viewModule(module, mapping, actionForm, request, response);
    }

    public ActionForward uploadStructure(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = getModule(request);
        
        request.setAttribute("bean", new StructureBean());

        if (module == null) {
            return mapping.findForward("upload.toplevel");
        }
        else {
            return forwardTo(mapping.findForward("upload"), request, module, true);
        }
    }
    
}
