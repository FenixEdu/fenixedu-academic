package ServidorApresentacao.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 */

public class CurriculumDispatchAction extends DispatchAction {

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String studentCurricularPlanID = request.getParameter("studentCPID");
        if (studentCurricularPlanID == null) {
            studentCurricularPlanID = (String) request.getAttribute("studentCPID");
        }

        Integer executionDegreeId = getExecutionDegree(request);
        List result = null;
        try {
            Object args[] = { executionDegreeId, Integer.valueOf(studentCurricularPlanID) };
            result = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentCurriculum", args);
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
            Object args[] = { Integer.valueOf(studentCurricularPlanID) };
            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentCurricularPlan", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }

        request.setAttribute(SessionConstants.CURRICULUM, result);
        request.setAttribute(SessionConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

        return mapping.findForward("ShowStudentCurriculum");
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String studentNumber = getStudent(request);
        List infoStudents = null;
        InfoPerson infoPerson = null;

        if (studentNumber == null) {
            try {
                Object args1[] = { userView.getUtilizador() };
                infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(
                        userView, "ReadPersonByUsername", args1);

                Object args2[] = { infoPerson };
                infoStudents = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadStudentsByPerson", args2);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else {
            try {
                Object args[] = { Integer.valueOf(studentNumber) };
                InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(
                        userView, "ReadStudentByNumberAndAllDegreeTypes", args);
                infoStudents = new ArrayList();
                infoStudents.add(infoStudent);
                infoPerson = infoStudent.getInfoPerson();
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

        }

        List result = new ArrayList();
        if (infoStudents != null) {
            Iterator iterator = infoStudents.iterator();
            while (iterator.hasNext()) {
                InfoStudent infoStudent = (InfoStudent) iterator.next();
                try {
                    Object args[] = { infoStudent.getNumber(), infoStudent.getDegreeType() };
                    List resultTemp = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                            "ReadStudentCurricularPlans", args);
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

    private Integer getExecutionDegree(HttpServletRequest request) {
        Integer executionDegreeId = null;

        String executionDegreeIdString = request.getParameter("executionDegreeId");
        if (executionDegreeIdString == null) {
            executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
        }
        if (executionDegreeIdString != null) {
            executionDegreeId = Integer.valueOf(executionDegreeIdString);
        }
        request.setAttribute("executionDegreeId", executionDegreeId);

        return executionDegreeId;
    }

}