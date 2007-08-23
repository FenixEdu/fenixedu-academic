package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ManageExecutionCourseDA extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);
        ExecutionCourse executionCourse = infoExecutionCourse.getExecutionCourse();        
        readAndSetExecutionCourseClasses(request, executionCourse);       
        request.setAttribute("courseLoadBean", new CourseLoadBean(executionCourse));        
        return mapping.findForward("ManageExecutionCourse");
    }

    public ActionForward preparePostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
	
	CourseLoadBean bean = (CourseLoadBean) getRenderedObject("courseLoadBeanID");	
	ShiftType type = bean.getType();
	if(type != null) {
	    CourseLoad courseLoad = bean.getExecutionCourse().getCourseLoadByShiftType(type);
	    if(courseLoad != null) {
		bean.setUnitQuantity(courseLoad.getUnitQuantity());
		bean.setTotalQuantity(courseLoad.getTotalQuantity());		
	    } else {
		bean.setUnitQuantity(null);
		bean.setTotalQuantity(null);
	    }
	}	
	readAndSetExecutionCourseClasses(request, bean.getExecutionCourse());
	RenderUtils.invalidateViewState("courseLoadBeanID");
	request.setAttribute("courseLoadBean", bean);	
	return mapping.findForward("ManageExecutionCourse");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

	CourseLoadBean bean = (CourseLoadBean) getRenderedObject("courseLoadBeanID");		
        ServiceManagerServiceFactory.executeService(getUserView(request), "EditExecutionCourse",  new Object[] { bean } );        
        
        bean.setType(null);
        bean.setUnitQuantity(null);
	bean.setTotalQuantity(null);
	
        readAndSetExecutionCourseClasses(request, bean.getExecutionCourse());
        request.setAttribute("courseLoadBean", bean);        
	return mapping.findForward("ManageExecutionCourse");        
    }
    
    public ActionForward deleteCourseLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
	
	CourseLoad courseLoad = getCourseLoadFromParameter(request);
	ExecutionCourse executionCourse = courseLoad.getExecutionCourse();
	try {
	    ServiceManagerServiceFactory.executeService(getUserView(request), "DeleteCourseLoad",  new Object[] { courseLoad } );
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());    
	}
	
        readAndSetExecutionCourseClasses(request, executionCourse);       
        request.setAttribute("courseLoadBean", new CourseLoadBean(executionCourse));        
        return mapping.findForward("ManageExecutionCourse");
    }
    
    private void readAndSetExecutionCourseClasses(HttpServletRequest request, ExecutionCourse executionCourse) 
    	throws FenixFilterException, FenixServiceException{
	
	List<InfoClass> infoClasses = (List<InfoClass>) ServiceManagerServiceFactory.executeService(getUserView(request),
		"ReadClassesByExecutionCourse", new Object[] {executionCourse});

        if (infoClasses != null && !infoClasses.isEmpty()) {
            Collections.sort(infoClasses, new BeanComparator("nome"));
            request.setAttribute(SessionConstants.LIST_INFOCLASS, infoClasses);
        } 
    }      
    
    private CourseLoad getCourseLoadFromParameter(final HttpServletRequest request) {
	final String idString = request.getParameterMap().containsKey("courseLoadID") ? request.getParameter("courseLoadID") : null;
	final Integer courseLoadID = idString != null ? Integer.valueOf(idString) : null;
	return rootDomainObject.readCourseLoadByOID(courseLoadID);
    }    
}