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
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.InfoStudentCurricularPlansWithSelectedEnrollments;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.EnrollmentStateSelectionType;
import Util.StudentCurricularPlanIDDomainType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 * @author André Fernandes / João Brito 
 */

public class CurriculumDispatchAction extends DispatchAction {
    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        
        String studentNumber = getStudent(request);
        InfoPerson infoPerson = null;

        String username = null;

        if (studentNumber != null) // para um aluno em particular
        {
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
        } else if (studentNumber == null) // deixa de importar se o ID do plano curricular for especificado
        {
            username = userView.getUtilizador();
        }

        try {
            executeService(username, request);
        } catch (Exception e) {
            return mapping.findForward("NotAuthorized");
        }

        request.setAttribute("studentPerson", infoPerson);

        if(degreeCurricularPlanID != null && degreeCurricularPlanID.intValue() != 0){
            return mapping.findForward("ShowStudentCurriculumForCoordinator");
        }
        return mapping.findForward("ShowStudentCurriculum");
    }

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getStudentCP(mapping, form, request, response);
    }

    private void executeService(String username, HttpServletRequest request) throws Exception
	{
	    IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);
	    	    
	    String criterioStr = (String)request.getAttribute("select");
	    if (criterioStr == null)
	        criterioStr = (String)request.getParameter("select");
		
		String scpIDStr = request.getParameter("studentCPID");
        if (scpIDStr == null)
            scpIDStr = (String) request.getAttribute("studentCPID");
	    
	    StudentCurricularPlanIDDomainType scpID = null;
		EnrollmentStateSelectionType criterio = null;
		
		if (criterioStr != null)
		    criterio = new EnrollmentStateSelectionType(criterioStr);
		else
		    criterio = EnrollmentStateSelectionType.ALL;
		
		if(scpIDStr != null)
		    scpID = new StudentCurricularPlanIDDomainType(scpIDStr);
		else
		    scpID = StudentCurricularPlanIDDomainType.NEWEST;
		
		InfoStudentCurricularPlansWithSelectedEnrollments infoSCPs = null;
		InfoStudentCurricularPlansWithSelectedEnrollments allInfoSCPs = null;
		
		try
		{
		    /* invoca o servico para obter os SCPs pedidos */
		    Object args[] = { username, scpID, criterio};
		    infoSCPs = (InfoStudentCurricularPlansWithSelectedEnrollments) ServiceManagerServiceFactory.executeService(userView, "ReadStudentCurricularPlansByPersonAndCriteria", args);
		    
		    /* invoca o servico para obter TODOS os SCPs com NENHUM enrollment*/
		    Object args1[] = {username, StudentCurricularPlanIDDomainType.ALL, EnrollmentStateSelectionType.NONE};
		    allInfoSCPs = (InfoStudentCurricularPlansWithSelectedEnrollments)ServiceManagerServiceFactory.executeService(userView,"ReadStudentCurricularPlansByPersonAndCriteria",args1);
		}
		catch (NonExistingServiceException e)
		{
		    throw new FenixActionException(e);
		}
		
		// qualquer numero de aluno serve para identificar Person
		String studentNumberTmp = String.valueOf(((InfoStudentCurricularPlan)allInfoSCPs.getInfoStudentCurricularPlans().get(0)).getInfoStudent().getNumber());
		request.setAttribute("studentNumber",studentNumberTmp);

		request.setAttribute("studentCPs", infoSCPs);
		
		List enrollmentOptions = EnrollmentStateSelectionType.getLabelValueBeanList();
		request.setAttribute("enrollmentOptions",enrollmentOptions);
		
		List allSCPs = getLabelValueBeanList(allInfoSCPs);
		request.setAttribute("allSCPs",allSCPs);
	}

    public ActionForward getCurriculumForCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        //get and set the degreeCurricularPlanID from the request and onto the request
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

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

    public static List getLabelValueBeanList(InfoStudentCurricularPlansWithSelectedEnrollments infoSCPs) {
        ArrayList result = new ArrayList();

        List sCPs = infoSCPs.getInfoStudentCurricularPlans();

        Iterator it = sCPs.iterator();

        // adiciona "todos" e "o mais recente"
        result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.NEWEST_STRING,
                StudentCurricularPlanIDDomainType.NEWEST.toString()));
        result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.ALL_STRING,
                StudentCurricularPlanIDDomainType.ALL.toString()));

        while (it.hasNext()) {
            InfoStudentCurricularPlan infoSCP = (InfoStudentCurricularPlan) it.next();
            String label = "";

            label += infoSCP.getInfoDegreeCurricularPlan().getInfoDegree().getNome() + " ";
            label += "(" + infoSCP.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso() + ")";

            if (infoSCP.getSpecialization() != null)
                label += " - " + infoSCP.getSpecialization();

            label += " - " + infoSCP.getStartDate();

            result.add(new LabelValueBean(label, String.valueOf(infoSCP.getIdInternal())));
        }

        return result;
    }

}