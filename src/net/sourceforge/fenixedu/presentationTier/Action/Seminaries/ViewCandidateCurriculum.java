/*
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Feb 1, 2004 , 3:57:28 PM
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.MockUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * Created at Feb 1, 2004 , 3:57:28 PM
 * 
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *  
 */
public class ViewCandidateCurriculum extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = getUserView(request);
        String username = request.getParameter("username");
        IUserView studentUserView = new MockUserView(username, new ArrayList(0), null);
        List cps = null;
        List enrollments = null;
        InfoStudentCurricularPlan selectedSCP = null;
        try {
            Object args[] = { studentUserView };
            cps = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentCurricularPlansForSeminaries", args);
            long startDate = Long.MAX_VALUE;
            for (Iterator iter = cps.iterator(); iter.hasNext();) {
                InfoStudentCurricularPlan cp = (InfoStudentCurricularPlan) iter.next();
                if (cp.getStartDate().getTime() < startDate) {
                    startDate = cp.getStartDate().getTime();
                    selectedSCP = cp;
                }
            }
            Object getCurriculumArgs[] = { null, selectedSCP.getIdInternal() };
            enrollments = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentCurriculum", getCurriculumArgs);
        } catch (NonExistingServiceException e) {
            throw new FenixActionException(e);
        }
        InfoClassification ic = new InfoClassification();
        int i = 0;
        float acc = 0;
        float grade = 0;
        for (Iterator iter = enrollments.iterator(); iter.hasNext();) {
            InfoEnrolment ie = (InfoEnrolment) iter.next();
            String stringGrade;
            if (ie.getInfoEnrolmentEvaluation() != null) {

                stringGrade = ie.getInfoEnrolmentEvaluation().getGrade();
            } else {
                stringGrade = GradeScale.NA;
            }
            if (stringGrade != null && !stringGrade.equals("") && !stringGrade.equals(GradeScale.RE) && !stringGrade.equals(GradeScale.NA) && !stringGrade.equals(GradeScale.AP)) {
                Float gradeObject = new Float(stringGrade);
                grade = gradeObject.floatValue();
                acc += grade;
                i++;
            }
        }
        if (i != 0) {
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