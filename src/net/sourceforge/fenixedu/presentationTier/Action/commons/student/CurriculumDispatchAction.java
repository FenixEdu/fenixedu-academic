package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.InfoStudentCurricularPlansWithSelectedEnrollments;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.EnrollmentStateSelectionType;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 * @author André Fernandes / João Brito
 */

public class CurriculumDispatchAction extends FenixDispatchAction {

    private final static ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
    
    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        String studentNumber = getStudent(request);
        InfoPerson infoPerson = null;

        String username = null;

        if (studentNumber != null) { // para um aluno em particular
            try {
                Object args[] = { Integer.valueOf(studentNumber) };
                InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(
                        userView, "ReadStudentByNumberAndAllDegreeTypes", args);

                if (infoStudent == null)
                    return mapping.findForward("NotAuthorized");

                infoPerson = infoStudent.getInfoPerson();

                username = infoPerson.getUsername();
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else if (studentNumber == null) {
            // deixa de importar se o ID do plano curricular for especificado
            username = userView.getUtilizador();
        }

        try {
            executeService(username, request);
        } catch (Exception e) {
            return mapping.findForward("NotAuthorized");
        }

        request.setAttribute("studentPerson", infoPerson);

        if (degreeCurricularPlanID != null && degreeCurricularPlanID.intValue() != 0) {
            return mapping.findForward("ShowStudentCurriculumForCoordinator");
        }
        return mapping.findForward("ShowStudentCurriculum");
    }

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getStudentCP(mapping, form, request, response);
    }

    private void executeService(String username, HttpServletRequest request) throws Exception {
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        String criterioStr = (String) request.getAttribute("select");
        if (criterioStr == null)
            criterioStr = request.getParameter("select");

        String scpIDStr = request.getParameter("studentCPID");
        if (scpIDStr == null)
            scpIDStr = (String) request.getAttribute("studentCPID");

        StudentCurricularPlanIDDomainType scpID = null;
        EnrollmentStateSelectionType criterio = null;

        if (criterioStr != null)
            criterio = new EnrollmentStateSelectionType(criterioStr);
        else
            criterio = EnrollmentStateSelectionType.ALL;

        if (scpIDStr != null)
            scpID = new StudentCurricularPlanIDDomainType(scpIDStr);
        else
            scpID = StudentCurricularPlanIDDomainType.NEWEST;

        InfoStudentCurricularPlansWithSelectedEnrollments infoSCPs = null;
        InfoStudentCurricularPlansWithSelectedEnrollments allInfoSCPs = null;

        try {
            /* invoca o servico para obter os SCPs pedidos */
            Object args[] = { username, scpID, criterio };
            infoSCPs = (InfoStudentCurricularPlansWithSelectedEnrollments) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentCurricularPlansByPersonAndCriteria", args);

            /* invoca o servico para obter TODOS os SCPs com NENHUM enrollment */
            Object args1[] = { username, StudentCurricularPlanIDDomainType.ALL,
                    EnrollmentStateSelectionType.NONE };
            allInfoSCPs = (InfoStudentCurricularPlansWithSelectedEnrollments) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentCurricularPlansByPersonAndCriteria", args1);
        } catch (NonExistingServiceException e) {
            throw new FenixActionException(e);
        }

        // qualquer numero de aluno serve para identificar Person
        // String studentNumberTmp =
        // String.valueOf(((InfoStudentCurricularPlan)allInfoSCPs.getInfoStudentCurricularPlans().get(0)).getInfoStudent().getNumber());
        String studentNumberTmp = String.valueOf(((InfoStudentCurricularPlan) infoSCPs
                .getInfoStudentCurricularPlans().get(0)).getInfoStudent().getNumber());
        request.setAttribute("studentNumber", studentNumberTmp);

        request.setAttribute("studentCPs", infoSCPs);

        List enrollmentOptions = EnrollmentStateSelectionType.getLabelValueBeanList();
        request.setAttribute("enrollmentOptions", enrollmentOptions);

        List allSCPs = getLabelValueBeanList(allInfoSCPs);
        request.setAttribute("allSCPs", allSCPs);
    }

    public ActionForward getCurriculumForCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get and set the degreeCurricularPlanID from the request and onto the
        // request
        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        IUserView userView = getUserView(request);

        String studentCurricularPlanID = request.getParameter("studentCPID");
        if (studentCurricularPlanID == null) {
            studentCurricularPlanID = (String) request.getAttribute("studentCPID");
        }

        Integer executionDegreeID = getExecutionDegree(request);
        List result = null;
        try {
            Object args[] = { executionDegreeID, Integer.valueOf(studentCurricularPlanID) };
            result = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentCurriculum", args);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        }
        // TODO Remove this exception! It returns null and it is not supposed!
        catch (Exception exp) {
            exp.printStackTrace();
            return null;
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

    private String getStudent(HttpServletRequest request) {
        String studentNumber = request.getParameter("studentNumber");
        if (studentNumber == null && StringUtils.isNumeric(studentNumber)) {
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

    public static List getLabelValueBeanList(InfoStudentCurricularPlansWithSelectedEnrollments infoSCPs) {
	final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
	
	result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.NEWEST_STRING, StudentCurricularPlanIDDomainType.NEWEST.toString()));
        result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.ALL_STRING, StudentCurricularPlanIDDomainType.ALL.toString()));

        for (final InfoStudentCurricularPlan infoSCP : infoSCPs.getInfoStudentCurricularPlans()) {
            StringBuilder label = new StringBuilder();

            label.append(enumerationResources.getString(infoSCP.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().name()));
            label.append(" ").append(applicationResources.getString("label.in")).append(" ");
            label.append(infoSCP.getInfoDegreeCurricularPlan().getInfoDegree().getNome());

            if (infoSCP.getSpecialization() != null) {
        	label.append(" - ").append(infoSCP.getSpecialization());
            }
                
            label.append(" - ").append(DateFormatUtil.format("dd.MM.yyyy", infoSCP.getStartDate()));

            result.add(new LabelValueBean(label.toString(), String.valueOf(infoSCP.getIdInternal())));
        }

        return result;
    }

}