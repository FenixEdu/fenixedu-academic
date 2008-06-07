package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class CurricularCourseCompetenceCourseDispatchAction extends FenixDispatchAction {
	
    public ActionForward removeFromCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
       	IUserView userView = UserView.getUser();
       	DynaActionForm actionForm = (DynaActionForm) form;
       	
       	Integer competenceCourseID = (Integer) actionForm.get("competenceCourseID");
       	Integer[] curricularCoursesIDs = (Integer[]) actionForm.get("curricularCoursesIds");
       	Object[] args = {competenceCourseID, curricularCoursesIDs};
       	
       	try {
            ServiceUtils.executeService("RemoveCurricularCoursesFromCompetenceCourse", args);
       	} catch (NotExistingServiceException notExistingServiceException) {
            
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        request.setAttribute("competenceCourseID", competenceCourseID);
       	return mapping.findForward("showCompetenceCourse");
    }
    
    public ActionForward addToCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
       	IUserView userView = UserView.getUser();
       	DynaActionForm actionForm = (DynaActionForm) form;
       	
       	Integer competenceCourseID = (Integer) actionForm.get("competenceCourseID");
       	Integer[] curricularCoursesIDs = (Integer[]) actionForm.get("curricularCoursesIds");
       	Object[] args = {competenceCourseID, curricularCoursesIDs};
       	
       	try {
            ServiceUtils.executeService("AssociateCurricularCoursesToCompetenceCourse", args);
       	} catch (NotExistingServiceException notExistingServiceException) {
            
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        request.setAttribute("competenceCourseID", competenceCourseID);
       	return mapping.findForward("showCompetenceCourse");
    }
    
    public ActionForward readDegrees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
       	IUserView userView = UserView.getUser();
       	Integer competenceCourseID = Integer.valueOf(RequestUtils.getAndSetStringToRequest(request, "competenceCourseID"));
       	DegreeType degreeType = DegreeType.DEGREE;
       	Object[] args = {degreeType};
       	List<InfoDegreeCurricularPlan> result = null;
       	try {
       		result = (List<InfoDegreeCurricularPlan>) ServiceUtils.executeService("ReadDegreeCurricularPlansByDegreeType", args);
       	} catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
       	DynaActionForm actionForm = (DynaActionForm) form;
       	actionForm.set("competenceCourseID", competenceCourseID);
       	request.setAttribute("degreeCurricularPlans", result);
       	return mapping.findForward("chooseDegreeCurricularPlan");
    }
    
    public ActionForward readCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
       	IUserView userView = UserView.getUser();
       	DynaActionForm actionForm = (DynaActionForm) form;
       	Integer degreeCurricularPlanID = (Integer) actionForm.get("degreeCurricularPlanID"); 
       	Object[] args = {degreeCurricularPlanID};
       	List<InfoCurricularCourse> result = null;
       	try {
       		result = (List<InfoCurricularCourse>) ServiceUtils.executeService("ReadCurricularCoursesByDegreeCurricularPlan", args);
       	} catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
       	
       	request.setAttribute("curricularCourses", result);
       	return mapping.findForward("chooseCurricularCourses");
    }
}
