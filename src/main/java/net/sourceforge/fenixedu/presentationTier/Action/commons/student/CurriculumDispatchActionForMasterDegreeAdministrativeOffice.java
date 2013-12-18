package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlans;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentsByPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByUsername;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentByNumberAndDegreeType;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 */

public class CurriculumDispatchActionForMasterDegreeAdministrativeOffice extends FenixDispatchAction {

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = getUserView(request);

        String studentCurricularPlanID = request.getParameter("studentCPID");
        if (studentCurricularPlanID == null) {
            studentCurricularPlanID = (String) request.getAttribute("studentCPID");
        }

        String executionDegreeId = getExecutionDegree(request);
        List result = null;
        try {
            result = ReadStudentCurriculum.runReadStudentCurriculum(executionDegreeId, studentCurricularPlanID);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        }

        BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
        BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(courseName);
        chainComparator.addComparator(executionYear);

        Collections.sort(result, chainComparator);

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try {

            infoStudentCurricularPlan = ReadStudentCurricularPlan.run(studentCurricularPlanID);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }

        request.setAttribute(PresentationConstants.CURRICULUM, result);
        request.setAttribute(PresentationConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

        return mapping.findForward("ShowStudentCurriculum");
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = getUserView(request);

        String studentNumber = getStudent(request);
        List infoStudents = null;
        InfoPerson infoPerson = null;

        if (studentNumber == null) {
            try {

                infoPerson = ReadPersonByUsername.run(userView.getUsername());

                infoStudents = ReadStudentsByPerson.runReadStudentsByPerson(infoPerson);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else {
            InfoStudent infoStudent =
                    (InfoStudent) ReadStudentByNumberAndDegreeType.run(Integer.valueOf(studentNumber), DegreeType.MASTER_DEGREE);
            infoStudents = new ArrayList();
            infoStudents.add(infoStudent);
            infoPerson = infoStudent.getInfoPerson();

        }

        List result = new ArrayList();
        if (infoStudents != null) {
            Iterator iterator = infoStudents.iterator();
            while (iterator.hasNext()) {
                InfoStudent infoStudent = (InfoStudent) iterator.next();
                try {

                    List resultTemp = ReadStudentCurricularPlans.run(infoStudent.getNumber(), infoStudent.getDegreeType());
                    result.addAll(resultTemp);
                } catch (NonExistingServiceException e) {
                }
            }
        }

        getExecutionDegree(request);

        request.setAttribute("studentCPs", result);
        request.setAttribute("studentPerson", infoPerson);

        return mapping.findForward("ShowStudentCurricularPlans");
    }

    private String getStudent(HttpServletRequest request) {
        String studentNumber = request.getParameter("studentNumber");
        if (studentNumber == null) {
            studentNumber = (String) request.getAttribute("studentNumber");
        }
        return studentNumber;
    }

    private String getExecutionDegree(HttpServletRequest request) {
        String executionDegreeIdString = request.getParameter("executionDegreeId");
        if (executionDegreeIdString == null) {
            executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
        }
        request.setAttribute("executionDegreeId", executionDegreeIdString);
        return executionDegreeIdString;
    }

}