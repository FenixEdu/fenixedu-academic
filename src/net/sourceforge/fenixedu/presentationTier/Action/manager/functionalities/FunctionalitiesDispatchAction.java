package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.IFunctionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.utl.ist.fenix.tools.util.Pair;

public class FunctionalitiesDispatchAction extends FenixDispatchAction {

    protected void addMessage(HttpServletRequest request, String name, String key, String ... args) {
        ActionMessages messages = getMessages(request);
        messages.add(name, new ActionMessage(key, args));
        saveMessages(request, messages);
    }
    
    public ActionForward forwardTo(ActionForward forward, HttpServletRequest request, Content functionality) throws Exception {
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
    
    public ActionForward viewRoot(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Module module = Module.getRootModule();
        
        return viewModule(module, mapping, actionForm, request, response);
    }
    
    protected ActionForward viewModule(Module module, ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (module == null) {
            return viewRoot(mapping, actionForm, request, response);
        }
        else {
            setBreadCrumbs(request, module);
            
            request.setAttribute("module", module);
            request.setAttribute("functionalities", module.getOrderedFunctionalities());
            
            return mapping.findForward("view.module");
        }
    }

    protected List<Module> getBreadCrumbs(HttpServletRequest request, Content content) {
        List<Module> crumbs = new ArrayList<Module>();

        IFunctionality functionality = (IFunctionality) content;
        for (Module parent = functionality.getModule(); parent != null; parent = parent.getModule()) {
            if (crumbs.isEmpty()) {
                crumbs.add(parent);
            }
            else {
                crumbs.add(0, parent);
            }
        }
        
        return crumbs;
    }
    
    protected void setBreadCrumbs(HttpServletRequest request, Content functionality) {
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
        Content functionality = getFunctionality(request);
    
        if (functionality == null) {
            return viewRoot(mapping, actionForm, request, response);
        }
        
        return forwardTo(mapping.findForward("confirm"), request, functionality);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Content functionality = getFunctionality(request);
    
        if (functionality == null) {
            return viewRoot(mapping, actionForm, request, response);
        }
        
        if (request.getParameter("confirm") != null) {
            Module parent = ((IFunctionality) functionality).getModule();
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

    protected Content getFunctionality(HttpServletRequest request) {
        return (Content) getObject(request, Content.class, "functionality");
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
    public static void deleteFunctionality(Content functionality) throws Exception {
        ServiceUtils.executeService( "DeleteFunctionality", functionality);
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
    public static void rearrangeFunctionalities(List<Pair<Module, Content>> arrangements)
            throws Exception {
        ServiceUtils.executeService( "ArrangeFunctionalities", arrangements);
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
        ServiceUtils.executeService( "ChangeEnableInFunctionality",
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
        ServiceUtils.executeService( "ChangeEnableInFunctionality",
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
    public static void setGroupAvailability(Content functionality, String expression)
            throws Exception {
        ServiceUtils.executeService( "CreateGroupAvailability",
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
     *            conflicts with existing functionalities
     * @param uuidUsed
     *            if the uuid declared in the document should be used as the new
     *            functionality uuid, that is, functionalities will be imported 
     *            with the same uuids
     */
    public static void importFunctionalities(Module module, InputStream stream, boolean principalPreserved, boolean uuidUsed) throws Exception {
        ServiceUtils.executeService( "ImportFunctionalities",
                module, stream, principalPreserved, uuidUsed);
    }
    
    /**
     * Auxiliary method that invokes a service to import functionalities from a
     * file using the parent declared in that file or by creating a new top level
     * module to hold them.
     * 
     * @param stream the stream containing the XML funcitonalities structure
     */
    public static void importStartupFunctionalities(InputStream stream) throws Exception {
        ServiceUtils.executeService( "ImportStartupFunctionalities", stream);
    }
}
