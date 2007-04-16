package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonChange;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonTarget;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisContextBean;
import net.sourceforge.fenixedu.presentationTier.docs.thesis.ApproveJuryDocument;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageThesisDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("thesis", getThesis(request));
        request.setAttribute("student", getStudent(request));
        
        ExecutionYear executionYear = getExecutionYear(request);
        
        setFilterContext(request, executionYear);
        
        return super.execute(mapping, actionForm, request, response);
    }
    
    private void setFilterContext(HttpServletRequest request, ExecutionYear executionYear) {
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("executionYearId", executionYear == null ? "" : executionYear.getIdInternal());
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        Integer id = getId(request.getParameter("degreeCurricularPlanID"));
        if (id == null) {
            return null;
        }
        else {
            return RootDomainObject.getInstance().readDegreeCurricularPlanByOID(id);
        }
    }

    private Thesis getThesis(HttpServletRequest request) {
        Thesis thesis = (Thesis) request.getAttribute("thesis");
        
        if (thesis != null) {
            return thesis;
        }
        else {
            Integer id = getId(request.getParameter("thesisID"));
            if (id == null) {
                return null;
            }
            else {
                return RootDomainObject.getInstance().readThesisByOID(id);
            }
        }
    }
    
    private Student getStudent(HttpServletRequest request) {
        Student student = (Student) request.getAttribute("student");
        
        if (student != null) {
            return student;
        }
        else {
            Integer id = getId(request.getParameter("studentID"));
            if (id == null) {
                return null;
            }
            else {
                return RootDomainObject.getInstance().readStudentByOID(id);
            }
        }
    }
    
    private ExecutionYear getExecutionYear(HttpServletRequest request) {
    	Integer id = getId(request.getParameter("executionYearId"));
    	if (id == null) {
    	    return ExecutionYear.readCurrentExecutionYear();
    	} else {
    	    return RootDomainObject.getInstance().readExecutionYearByOID(id);
    	}
    }
    
    private ThesisContextBean getContextBean(HttpServletRequest request) {
    	ThesisContextBean bean = (ThesisContextBean) getRenderedObject("contextBean");
    	RenderUtils.invalidateViewState("contextBean");
    	
    	if (bean != null) {
    	    return bean;
    	}
    	else {
			ExecutionYear executionYear = getExecutionYear(request);

			if (executionYear == null) {
				executionYear = ExecutionYear.readCurrentExecutionYear();
			}

			return new ThesisContextBean(executionYear);
		}
    }
    
    private Integer getId(String id) {
        if (id == null) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ActionForward searchStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = new ThesisBean();
        
        request.setAttribute("bean", bean);
        return mapping.findForward("search-student");
    }
    
    public ActionForward selectStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("student");
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
        else {
            request.setAttribute("bean", bean);
        }
        
        Student student = bean.getStudent();
        
        if (student == null) {
            addActionMessage(request, "thesis.selectStudent.notFound");
            return mapping.findForward("search-student");
        }
        else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            
            if (student.isCurrentlyEnroled(degreeCurricularPlan)) {
                Enrolment enrolment = student.getDissertationEnrolment(degreeCurricularPlan);

                if (enrolment != null) {
                    Thesis thesis = enrolment.getThesis();
                    
                    if (thesis == null) {
//                        ProposalAssignment assignment = enrolment.getThesisProposalAssignment();
//                        
//                        if (assignment != null) {
//                            request.setAttribute("assignment", assignment);
//                            return mapping.findForward("search-student");
//                        }
//                        else {
                            request.setAttribute("proposeStartProcess", true);
                            return mapping.findForward("search-student");
//                        }
                    }
                    else {
                        request.setAttribute("hasThesis", true);
                        request.setAttribute("thesis", thesis);
                        return mapping.findForward("search-student");
                    }
                }
                else {
                    addActionMessage(request, "thesis.selectStudent.dissertation.notEnroled");
                    return mapping.findForward("search-student");
                }
            }
            else {
                addActionMessage(request, "thesis.selectStudent.degreeCurricularPlan.notEnroled", degreeCurricularPlan.getName());
                return mapping.findForward("search-student");
            }
            
        }
    }
    
    public ActionForward viewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis.isDraft()) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        if (thesis.isSubmitted()) {
            return viewSubmitted(mapping, actionForm, request, response);
        }
        
        if (thesis.isWaitingConfirmation()) {
            return viewApproved(mapping, actionForm, request, response);
        }
        
        if (thesis.isConfirmed()) {
            return viewConfirmed(mapping, actionForm, request, response);
        }
        
        if (thesis.isEvaluated()) {
            return viewEvaluated(mapping, actionForm, request, response);
        }
        
        return searchStudent(mapping, actionForm, request, response);
    }
    
    public ActionForward listThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);       
        ThesisContextBean bean = getContextBean(request);

        List<StudentThesisInfo> result = new ArrayList<StudentThesisInfo>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getDissertationCurricularCourses()) {
            // TODO: thesis, allow to choose executionYear
            for (Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(bean.getExecutionYear())) {
                StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
                
                if (studentCurricularPlan.getDegreeCurricularPlan() != degreeCurricularPlan) {
                    continue;
                }
                
                Student student = studentCurricularPlan.getRegistration().getStudent();
                result.add(new StudentThesisInfo(student, enrolment));
            }
        }
        
        request.setAttribute("theses", result);
        request.setAttribute("contextBean", bean);
        
        return mapping.findForward("list-thesis");
    }
    
    public ActionForward prepareCreateProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Student student = getStudent(request);
        
        if (student == null) {
            return listThesis(mapping, actionForm, request, response);   
        }
        
        ThesisBean bean = new ThesisBean();
        bean.setStudent(student);
        
        request.setAttribute("bean", bean);
        return mapping.findForward("collect-basic-information");
    }
    
    public ActionForward createProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        RenderUtils.invalidateViewState("bean");
        
        if (bean == null) {
            return selectStudent(mapping, actionForm, request, response);
        }
        
        Thesis thesis = (Thesis) executeService("CreateThesisProposal", degreeCurricularPlan, bean.getStudent(), bean.getTitle(), bean.getComment());
        request.setAttribute("thesis", thesis);
        
        return editProposal(mapping, actionForm, request, response);
    }
    
    // Draft
    
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        request.setAttribute("conditions", thesis.getConditions());

        if (thesis.isOrientatorCreditsDistributionNeeded()) {
            request.setAttribute("orientatorCreditsDistribution", true);
        }
        
        if (thesis.isCoorientatorCreditsDistributionNeeded()) {
            request.setAttribute("coorientatorCreditsDistribution", true);
        }
        
        return mapping.findForward("edit-thesis");
    }
    
    public ActionForward changeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("change-information");
    }
    
    public ActionForward changeCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        
        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        switch (PersonTarget.valueOf(target)) {
        case orientator:
            request.setAttribute("editOrientatorCreditsDistribution", true);
            break;
        case coorientator:
            request.setAttribute("editCoorientatorCreditsDistribution", true);
            break;
        default:
        }
        
        return editProposal(mapping, actionForm, request, response);
    }
    
    public ActionForward changeParticipationInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        Thesis thesis = getThesis(request);
        ThesisEvaluationParticipant participant;
        
        PersonTarget targetType = PersonTarget.valueOf(target);
        switch (targetType) {
        case orientator:
            participant = thesis.getOrientator();
            break;
        case coorientator:
            participant = thesis.getCoorientator();
            
            // HACK: ouch! type is used for a lable in the destination page, and we don't
            // want to make a distinction between orientator and coorientator
            targetType = PersonTarget.orientator;
            break;
        case president:
            participant = thesis.getPresident();
            break;
        case vowel:
            participant = getVowel(request);
            break;
        default:
            participant = null;
        }

        if (participant == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        else {
            request.setAttribute("targetType", targetType);
            request.setAttribute("participant", participant);
            return mapping.findForward("editParticipant");
        }
    }
    
    public ActionForward changePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean remove = request.getParameter("remove") != null;
        
        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        ThesisBean bean = new ThesisBean();

        Degree degree = getDegreeCurricularPlan(request).getDegree();
        bean.setDegree(degree);
        
        PersonTarget targetType = PersonTarget.valueOf(target);
        bean.setTargetType(targetType);
        
        if (targetType.equals(PersonTarget.vowel)) {
            ThesisEvaluationParticipant targetVowel = getVowel(request);
            
            if (targetVowel != null) {
                bean.setTarget(targetVowel);
            }
            else {
                bean.setTarget(null);
            }
        }

        if (remove) {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            executeService("ChangeThesisPerson", degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), null, bean.getTarget()));
            
            return editProposal(mapping, actionForm, request, response);
        }
        else {
            request.setAttribute("bean", bean);
            return mapping.findForward("select-person");
        }
    }

    private ThesisEvaluationParticipant getVowel(HttpServletRequest request) {
        String parameter = request.getParameter("vowelID");
        if (parameter == null) {
            return null;
        }
        
        Integer id = Integer.valueOf(parameter);
        
        Thesis thesis = getThesis(request);
        for (ThesisEvaluationParticipant participant : thesis.getVowels()) {
            if (participant.getIdInternal().equals(id)) {
                return participant;
            }
        }
        
        return null;
    }

    public ActionForward changePersonInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }

    public ActionForward changePersonType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        RenderUtils.invalidateViewState("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
     
        bean.setPersonName(null);
        bean.setRawPersonName(null);
        bean.setUnitName(null);
        bean.setRawUnitName(null);
        
        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }
    
    public ActionForward selectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        
        Person selectedPerson = bean.getPerson();
        if (selectedPerson == null) {
            addActionMessage("info", request, "thesis.selectPerson.internal.required");
            return mapping.findForward("select-person");
        }
        else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            executeService("ChangeThesisPerson", degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));
            
            return editProposal(mapping, actionForm, request, response);
        }
    }

    public ActionForward selectPersonInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }
     
        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }
    
    public ActionForward selectExternalPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        boolean create = request.getParameter("create") != null;
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        
        Person selectedPerson = bean.getPerson();
        if (selectedPerson == null) {
            if (! create) {
                if (bean.getRawPersonName() == null || bean.getRawPersonName().trim().length() == 0) {
                    addActionMessage("info", request, "thesis.selectPerson.external.name.required");
                }
                else {
                    request.setAttribute("proposeCreation", true);
                }
    
                return mapping.findForward("select-person");
            }
            else {
                RenderUtils.invalidateViewState("bean");
                return mapping.findForward("select-unit");
            }
        }
        else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            executeService("ChangeThesisPerson", degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));
            
            return editProposal(mapping, actionForm, request, response);
        }
    }
    
    public ActionForward selectUnitInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        return mapping.findForward("select-unit");
    }
    
    public ActionForward selectExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        boolean create = request.getParameter("create") != null;
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        
        Unit selectedUnit = bean.getUnit();
        if (selectedUnit == null) {
            if (create) {
                DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
                Thesis thesis = getThesis(request);
                executeService("ChangeThesisPerson", degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), bean.getRawPersonName(), bean.getRawUnitName(), bean.getTarget()));
                
                return editProposal(mapping, actionForm, request, response);
            }
            else {
                if (bean.getRawUnitName() == null || bean.getRawUnitName().trim().length() == 0) {
                    addActionMessage("info", request, "thesis.selectUnit.external.name.required");
                }
                else {
                    request.setAttribute("proposeCreation", true);
                }
    
                return mapping.findForward("select-unit");
            }
        }
        else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            executeService("ChangeThesisPerson", degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), bean.getRawPersonName(), bean.getUnit(), bean.getTarget()));
            
            return editProposal(mapping, actionForm, request, response);
        }
    }
    
    public ActionForward submitProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        try {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            executeService("SubmitThesis", degreeCurricularPlan, thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return editProposal(mapping, actionForm, request, response);
        }
        
        return listThesis(mapping, actionForm, request, response);
    }

    public ActionForward confirmDeleteProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("confirmDelete", true);
        return editProposal(mapping, actionForm, request, response);
    }

    public ActionForward deleteProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        try {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            executeService("DeleteThesis", degreeCurricularPlan, thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        
        return listThesis(mapping, actionForm, request, response);
    }
    
    // Submitted
    
    public ActionForward viewSubmitted(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        return mapping.findForward("view-submitted");
    }

    // Approved
    
    public ActionForward viewApproved(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("view-approved");
    }
    
    public ActionForward printApprovalDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        try {
            ApproveJuryDocument document = new ApproveJuryDocument(thesis);
            byte[] data = ReportsUtils.exportToPdf(document);
            
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);
            
            return null;
        } catch (JRException e) {
            addActionMessage("error", request, "coordinator.thesis.approved.print.failed");
            return viewSubmitted(mapping, actionForm, request, response);
        }
    }
    
    // Confirmed
    
    public ActionForward viewConfirmed(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("view-confirmed");
    }
    
    public ActionForward confirmRevision(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("confirmRevision", true);
        return viewConfirmed(mapping, actionForm, request, response);
    }

    public ActionForward enterRevision(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }
        
        try {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            executeService("ReviseThesis", degreeCurricularPlan, thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        
        return listThesis(mapping, actionForm, request, response);
    }

    // Evaluated
    
    public ActionForward viewEvaluated(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("view-evaluated");
    }
    
}
