package ServidorApresentacao.Action.masterDegree.administrativeOffice.student;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to display all the master degrees.
 *  
 */
public class CourseListingDispatchAction extends DispatchAction
{

    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {

            session.removeAttribute(SessionConstants.MASTER_DEGREE_LIST);

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            TipoCurso degreeType = TipoCurso.MESTRADO_OBJ;

            Object args[] =
            { degreeType};

            List result = null;
            try
            {
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadAllMasterDegrees", args);
            }
            catch (NonExistingServiceException e)
            {
                throw new NonExistingActionException("O Curso de Mestrado", e);
            }

            request.setAttribute(SessionConstants.MASTER_DEGREE_LIST, result);

            return mapping.findForward("DisplayMasterDegreeList");
        }
        else
            throw new Exception();
    }

    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //Get the Chosen Master Degree
            Integer masterDegreeID = new Integer(request.getParameter("degreeID"));
            if (masterDegreeID == null)
            {
                masterDegreeID = (Integer) request.getAttribute("degreeID");
            }

            Object args[] =
            { masterDegreeID};
            List result = null;

            try
            {

                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadCPlanFromChosenMasterDegree", args);

            }
            catch (NonExistingServiceException e)
            {
                throw new NonExistingActionException("O plano curricular ", e);
            }

            request.setAttribute(SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);

            return mapping.findForward("MasterDegreeReady");
        }
        else
            throw new Exception();
    }

    public ActionForward prepareList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        String degreeCurricularPlanId = getFromRequest("curricularPlanID", request);

        Object args[] =
        { Integer.valueOf(degreeCurricularPlanId)};
        IUserView userView = SessionUtils.getUserView(request);
        List curricularCourseList = null;
        try
        {
            curricularCourseList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadCurricularCoursesByDegreeCurricularPlanId", args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        Collections.sort(curricularCourseList, new BeanComparator("name"));
        request.setAttribute("curricularCourses", curricularCourseList);

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward getStudentsFromCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        //Get the Selected Course

        Integer scopeCode = null;
        String scopeCodeString = request.getParameter("scopeCode");
        if (scopeCodeString == null)
        {
            scopeCodeString = (String) request.getAttribute("scopeCode");
        }
        if (scopeCodeString != null)
        {
            scopeCode = new Integer(scopeCodeString);
        }

        String yearString = getFromRequest("executionYear", request);

        List enrolments = null;
        Object args[] =
        { userView, scopeCode, yearString};
        try
        {

           enrolments = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentListByCurricularCourse", args);

        }
        catch (NonExistingServiceException e)
        {
            ActionErrors errors = new ActionErrors();
            errors.add("error.exception.noStudents", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);

            return prepareList(mapping, form, request, response);
        }

        BeanComparator numberComparator = new BeanComparator("infoStudentCurricularPlan.infoStudent.number");
        Collections.sort(enrolments, numberComparator);

        request.setAttribute(SessionConstants.ENROLMENT_LIST, enrolments);

        return mapping.findForward("Success");
    }

    private String getFromRequest(String parameter, HttpServletRequest request)
    {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null)
        {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}