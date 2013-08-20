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
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlansForSeminaries;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.MockUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * Created at Feb 1, 2004 , 3:57:28 PM
 * 
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * 
 */
@Mapping(module = "teacher", path = "/viewCandidateCurriculum", scope = "session")
@Forwards(value = { @Forward(name = "viewCurriculum", path = "/teacher/viewCandidateCurriculum.jsp", tileProperties = @Tile(
        navLocal = "/teacher/showSeminariesIndex_bd.jsp", title = "private.seminars.viewapplications")) })
public class ViewCandidateCurriculum extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IUserView userView = getUserView(request);
        String username = request.getParameter("username");
        IUserView studentUserView = new MockUserView(username, new ArrayList(0), null);
        List cps = null;
        List enrollments = null;
        InfoStudentCurricularPlan selectedSCP = null;
        try {

            cps = ReadStudentCurricularPlansForSeminaries.run(studentUserView);
            long startDate = Long.MAX_VALUE;
            for (Iterator iter = cps.iterator(); iter.hasNext();) {
                InfoStudentCurricularPlan cp = (InfoStudentCurricularPlan) iter.next();
                if (cp.getStartDate().getTime() < startDate) {
                    startDate = cp.getStartDate().getTime();
                    selectedSCP = cp;
                }
            }
            enrollments = ReadStudentCurriculum.runReadStudentCurriculum(null, selectedSCP.getExternalId());
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

                stringGrade = ie.getInfoEnrolmentEvaluation().getGradeValue();
            } else {
                stringGrade = GradeScale.NA;
            }
            if (stringGrade != null && !stringGrade.equals("") && !stringGrade.equals(GradeScale.RE)
                    && !stringGrade.equals(GradeScale.NA) && !stringGrade.equals(GradeScale.AP)) {
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
        request.setAttribute(PresentationConstants.CURRICULUM, enrollments);
        request.setAttribute(PresentationConstants.STUDENT_CURRICULAR_PLAN, selectedSCP);
        return mapping.findForward("viewCurriculum");
    }
}