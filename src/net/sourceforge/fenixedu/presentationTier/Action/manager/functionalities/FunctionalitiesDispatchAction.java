package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FunctionalitiesDispatchAction extends FenixDispatchAction {

    public ActionForward forwardTo(ActionForward forward, HttpServletRequest request, Functionality functionality) throws Exception {
        setBreadCrumbs(request, functionality);
        request.setAttribute("functionality", functionality);
        
        return forward;
    }
    
    public ActionForward forwardTo(ActionForward forward, HttpServletRequest request, Module module, boolean includeLast) throws Exception {
        setBreadCrumbs(request, module, includeLast);
        
        if (includeLast) {
            request.setAttribute("parent", module);
        }
        
        request.setAttribute("module", module);
        
        return forward;
    }
    
    public ActionForward viewTopLevel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Functionality> functionalities = Functionality.getOrderedTopLevelFunctionalities();
        request.setAttribute("functionalities", functionalities);
        
        return mapping.findForward("toplevel");
    }

    protected ActionForward viewModule(Module module, ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (module == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }
        else {
            setBreadCrumbs(request, module);
            
            request.setAttribute("module", module);
            request.setAttribute("functionalities", module.getOrderedFunctionalities());
            
            return mapping.findForward("view.module");
        }
    }

    protected List<Module> getBreadCrumbs(HttpServletRequest request, Functionality functionality) {
        List<Module> crumbs = new ArrayList<Module>();

        for (Module parent = functionality.getModule(); parent != null; parent = parent.getParent()) {
            if (crumbs.isEmpty()) {
                crumbs.add(parent);
            }
            else {
                crumbs.add(0, parent);
            }
        }
        
        return crumbs;
    }
    
    protected void setBreadCrumbs(HttpServletRequest request, Functionality functionality) {
        List<Module> crumbs = getBreadCrumbs(request, functionality);
        request.setAttribute("crumbs", crumbs);
    }

    protected void setBreadCrumbs(HttpServletRequest request, Module module, boolean includeLast) {
        List<Module> crumbs = getBreadCrumbs(request, module);
        
        if (includeLast) {
            crumbs.add(module);
        }
        
        request.setAttribute("crumbs", crumbs);
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);
    
        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }
        
        return forwardTo(mapping.findForward("confirm"), request, functionality);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Functionality functionality = getFunctionality(request);
    
        if (functionality == null) {
            return viewTopLevel(mapping, actionForm, request, response);
        }
        
        if (request.getParameter("confirm") != null) {
            Module parent = functionality.getModule();
            Functionality.deleteFunctionality(functionality);
            
            return viewModule(parent, mapping, actionForm, request, response);
        }
        else {
            if (functionality instanceof Module) {
                return viewModule((Module) functionality, mapping, actionForm, request, response);    
            }
            else {
                return forwardTo(mapping.findForward("view"), request, functionality);
            }
        }
    }

    protected Module getModule(HttpServletRequest request) {
        return (Module) getObject(request, Module.class, "module");
    }

    protected Functionality getFunctionality(HttpServletRequest request) {
        return (Functionality) getObject(request, ConcreteFunctionality.class, "functionality");
    }

    protected DomainObject getObject(HttpServletRequest request, Class type, String parameter) {
        Integer objectId = getObjectId(request, parameter);
        
        if (objectId == null) {
            return null;
        }
        
        return readDomainObject(request, type, objectId);
    }

    protected Integer getObjectId(HttpServletRequest request, String name) {
        return getId((String) request.getParameter(name));
    }
    
    protected Integer getId(String id) {
        if (id == null) {
            return null;
        }
        
        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

}
