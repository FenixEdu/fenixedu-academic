package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class CompetenceCourseDispatchAction extends FenixDispatchAction {
	
    public ActionForward showAllCompetences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
        
    	IUserView userView = SessionUtils.getUserView(request);
        Object[] args = {};
        List<InfoCompetenceCourse> infoCompetenceCoursesList = null;
        
        try {
        	
            infoCompetenceCoursesList = (List<InfoCompetenceCourse>) ServiceUtils.executeService(userView,
                    "ReadAllCompetenceCourses", args);
            
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        
        Collections.sort(infoCompetenceCoursesList, new BeanComparator("name", Collator.getInstance()));
        
        request.setAttribute("competenceCourses", infoCompetenceCoursesList);
        return mapping.findForward("showAllCompetenceCourses");
    }
    
    public ActionForward showCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
    	IUserView userView = SessionUtils.getUserView(request);
    	
    	Integer competenceCourseID = Integer.valueOf((String) request.getParameter("competenceCourseID"));
        Object[] args = {competenceCourseID};
        InfoCompetenceCourse competenceCourse = null;
        try {
            competenceCourse = (InfoCompetenceCourse) ServiceUtils.executeService(userView,"ReadCompetenceCourse", args);
        } catch (NotExistingServiceException notExistingServiceException) {
        	
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        
        request.setAttribute("competenceCourse", competenceCourse);
        return mapping.findForward("showCompetenceCourse");
    }
    
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
    	IUserView userView = SessionUtils.getUserView(request);
    	
    	Integer competenceCourseID = Integer.valueOf((String) request.getParameter("competenceCourse"));
    	Object[] args = {competenceCourseID};
    	Object[] args2 = {};
    	InfoCompetenceCourse competenceCourse = null;
    	List<InfoDepartment> infoDepartments = null;
        try {
            competenceCourse = (InfoCompetenceCourse) ServiceUtils.executeService(userView,"ReadCompetenceCourse", args);
            infoDepartments = (List<InfoDepartment>) ServiceUtils.executeService(userView,"ReadAllDepartments", args2);
        } catch (NotExistingServiceException notExistingServiceException) {
            
         
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        
        request.setAttribute("departments", infoDepartments);
        request.setAttribute("competenceCourse", competenceCourse);
        
        DynaActionForm actionForm = (DynaActionForm) form;
        actionForm.set("competenceCourseID", competenceCourse.getIdInternal());
        actionForm.set("code", competenceCourse.getCode());
        actionForm.set("name", competenceCourse.getName());
        if(competenceCourse.getDepartment() != null) {
        	actionForm.set("departmentID", competenceCourse.getDepartment().getIdInternal());
        }
        
        return mapping.findForward("edit");
    }    

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    		HttpServletResponse response) throws FenixActionException, FenixFilterException{
    	IUserView userView = SessionUtils.getUserView(request);
    	DynaActionForm actionForm = (DynaActionForm) form;
    	Integer competenceCourseID = (Integer) actionForm.get("competenceCourseID");
    	String code = (String) actionForm.get("code");
    	String name = (String) actionForm.get("name");
    	Integer departmentID = (Integer) actionForm.get("departmentID");
    	Object[] args = {competenceCourseID, code, name, departmentID};
    	InfoCompetenceCourse competenceCourse = null;
        try {
            competenceCourse = (InfoCompetenceCourse) ServiceUtils.executeService(userView,"CreateEditCompetenceCourse", args);
            
        } catch (InvalidArgumentsServiceException invalidArgumentsServiceException) {

        } catch (NotExistingServiceException notExistingServiceException) {
            
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        
        request.setAttribute("competenceCourse", competenceCourse);
        return mapping.findForward("showCompetenceCourse");
    }
    
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    		HttpServletResponse response) throws FenixActionException, FenixFilterException{
    	IUserView userView = SessionUtils.getUserView(request);
    	Object[] args = {};
    	List<InfoDepartment> departmentList = null;
        try {
            departmentList = (List<InfoDepartment>) ServiceUtils.executeService(userView,"ReadAllDepartments", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        
        request.setAttribute("departments", departmentList);
    	return mapping.findForward("createCompetenceCourse");
    }
    
    public ActionForward createCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    		HttpServletResponse response) throws FenixActionException, FenixFilterException{
    	IUserView userView = SessionUtils.getUserView(request);
    	DynaActionForm actionForm = (DynaActionForm) form;
    	
    	String code = (String) actionForm.get("code");
    	String name = (String) actionForm.get("name");
    	Integer departmentID = (Integer) actionForm.get("departmentID");
    	Object[] args = {null, code, name, departmentID};
    	InfoCompetenceCourse competenceCourse = null;
        try {
            competenceCourse = (InfoCompetenceCourse) ServiceUtils.executeService(userView,"CreateEditCompetenceCourse", args);
            
        } catch (InvalidArgumentsServiceException invalidArgumentsServiceException) {

        } catch (NotExistingServiceException notExistingServiceException) {
            
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        
        request.setAttribute("competenceCourse", competenceCourse);
        return mapping.findForward("showCompetenceCourse");
    }
    

}
