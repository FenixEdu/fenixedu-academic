/*
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Feb 1, 2004 , 3:57:28 PM
 *  
 */
package ServidorApresentacao.Action.Seminaries;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import DataBeans.InfoEnrolment;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.Seminaries.InfoClassification;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;
/**
 * 
 * Created at Feb 1, 2004 , 3:57:28 PM
 * 
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *  
 */
public class ViewCandidateCurriculum extends FenixAction
{
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String username = request.getParameter("username");
        IUserView studentUserView = new UserView(username, new LinkedList());
        List cps = null;
        List enrollments = null;
        InfoStudentCurricularPlan selectedSCP = null;
        try
        {
            Object args[] = { studentUserView };
            cps =
                (ArrayList) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadStudentCurricularPlansForSeminaries",
                    args);
            long startDate = Long.MAX_VALUE;
            for (Iterator iter = cps.iterator(); iter.hasNext();)
            {
                InfoStudentCurricularPlan cp = (InfoStudentCurricularPlan) iter.next();
                if (cp.getStartDate().getTime() < startDate)
                {
                    startDate = cp.getStartDate().getTime();
                    selectedSCP = cp;
                }
            }
            Object getCurriculumArgs[] = { null, selectedSCP.getIdInternal()};
            enrollments =
                (ArrayList) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadStudentCurriculum",
                    getCurriculumArgs);
        } catch (NonExistingServiceException e)
        {
            throw new FenixActionException(e);
        }
        InfoClassification ic = new InfoClassification();
        int i = 0;
        float acc = 0;
        float grade = 0;
        for (Iterator iter = enrollments.iterator(); iter.hasNext();)
        {
            InfoEnrolment ie = (InfoEnrolment) iter.next();
            String stringGrade = ie.getInfoEnrolmentEvaluation().getGrade();
            if (stringGrade != null && !stringGrade.equals("RE") && !stringGrade.equals("NA"))
            {
                Float gradeObject = new Float(stringGrade);
                grade = gradeObject.floatValue();
                acc += grade;
                i++;
            }
        }
        if (i != 0)
        {
            String value = new DecimalFormat("#0.0").format(acc / i);
            ic.setAritmeticClassification(value);
        }
        ic.setCompletedCourses(new Integer(i).toString());
        request.setAttribute("classification", ic);
        request.setAttribute(SessionConstants.CURRICULUM, enrollments);
        request.setAttribute(SessionConstants.STUDENT_CURRICULAR_PLAN, selectedSCP);
        return mapping.findForward("viewCurriculum");
    }
}
