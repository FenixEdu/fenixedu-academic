package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality.Movement;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.utl.fenix.utils.Pair;

public class FunctionalitiesDispatchAction extends FenixDispatchAction {

    protected void addMessage(HttpServletRequest request, String name, String key, String ... args) {
        ActionMessages messages = getMessages(request);
        messages.add(name, new ActionMessage(key, args));
        saveMessages(request, messages);
    }
    
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
            deleteFunctionality(functionality);
            
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

    //
    // Auxiliary methods
    //

    /**
     * Auxiliary method that invokes the real service that will delete the
     * functionality.
     * 
     * @param functionality
     *            the functionality that will be deleted
     * @throws Exception
     *             the exception throw by the service
     */
    public static void deleteFunctionality(Functionality functionality) throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "DeleteFunctionality", functionality);
    }

    /**
     * Auxiliary method that invokes the real service that will move the
     * functionality.
     * 
     * @param functionality
     *            the functionality to be moven
     * @param movement
     *            the type of movement
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void moveFunctionality(Functionality functionality, Movement movement)
            throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "MoveFunctionality", functionality,
                movement);
    }

    /**
     * Auxiliary method that invokes the real service that will rearrange all
     * the functionalities. All pairs passed as argument describe the new
     * module/functionality relations. Relations will be broken and created
     * between all the referred functionalities in one transaction.
     * 
     * @param arrangements
     *            a list of pairs (parent, child)
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void rearrangeFunctionalities(List<Pair<Module, Functionality>> arrangements)
            throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "ArrangeFunctionalities", arrangements);
    }

    /**
     * Auxiliary method that invokes the service to enable the given
     * functionality.
     * 
     * @param functionality
     *            the functionality to enable
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void enable(Functionality functionality) throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "ChangeEnableInFunctionality",
                functionality, true);
    }

    /**
     * Auxiliary method that invokes the service to disable the given
     * functionality.
     * 
     * @param functionality
     *            the functionality to disable
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void disable(Functionality functionality) throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "ChangeEnableInFunctionality",
                functionality, false);
    }

    /**
     * Auxiliary method that invokes a service to create a
     * {@link GroupAvailability} for the given functinolity
     * 
     * @param functionality
     *            the functionality that will have it's availability changed
     * @param expression
     *            the group expression used to the create the new group
     *            availability
     */
    public static void setGroupAvailability(Functionality functionality, String expression)
            throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "CreateGroupAvailability",
                functionality, expression);
    }

    /**
     * Auxiliary method that invokes a service to import functionalities from a
     * file and include those functionalities in a module.
     * 
     * @param module
     *            the module that will hold all the imported functionalities
     * @param stream
     *            the input stream corresponding to the XML document containing
     *            the structure
     * @param principalPreserved
     *            if <code>false</code> then all functionalities will have the
     *            value of <tt>principal</tt> set to false, possibly avoiding
     *            conflicts with existing functionalities.
     */
    public static void importFunctionalities(Module module, InputStream stream, boolean principalPreserved) throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "ImportFunctionalities",
                module, stream, principalPreserved);
    }
}
