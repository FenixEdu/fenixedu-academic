package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonChange;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonTarget;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageThesisDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("thesis", getThesis(request));
        
        return super.execute(mapping, actionForm, request, response);
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
//                Enrolment enrolment = student.getDissertationEnrolment();
//
//                if (enrolment != null) {
//                    Thesis thesis = enrolment.getThesis();
//                    
//                    if (thesis == null) {
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
//                    }
//                    else {
//                        request.setAttribute("hasThesis", true);
//                        request.setAttribute("thesis", thesis);
//                        return mapping.findForward("search-student");
//                    }
//                }
//                else {
//                    addActionMessage(request, "thesis.selectStudent.dissertation.notEnroled");
//                    return mapping.findForward("search-student");
//                }
            }
            else {
                addActionMessage(request, "thesis.selectStudent.degreeCurricularPlan.notEnroled", degreeCurricularPlan.getName());
                return mapping.findForward("search-student");
            }
            
        }
    }
    
    public ActionForward collectBasicInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean-invisible");
        RenderUtils.invalidateViewState("bean-invisible");

        if (bean == null) {
            bean = (ThesisBean) getRenderedObject("bean");
        }
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
        
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
        
        Thesis thesis = (Thesis) executeService("CreateThesisProposal", degreeCurricularPlan.getIdInternal(), bean.getStudent(), bean.getTitle(), bean.getComment());
        request.setAttribute("thesis", thesis);
        
        return editProposal(mapping, actionForm, request, response);
    }
    
    public ActionForward changeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("change-information");
    }
    
    public ActionForward changePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean remove = request.getParameter("remove") != null;
        
        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        ThesisBean bean = new ThesisBean();
        
        PersonTarget targetType = PersonTarget.valueOf(target);
        bean.setTargetType(targetType);
        
        if (targetType.equals(PersonTarget.vowel)) {
            Person targetVowel = getVowel(request);
            
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
            
            executeService("ChangeThesisPerson", degreeCurricularPlan.getIdInternal(), thesis, new PersonChange(bean.getTargetType(), null, bean.getTarget()));
            
            return editProposal(mapping, actionForm, request, response);
        }
        else {
            request.setAttribute("bean", bean);
            return mapping.findForward("select-person");
        }
    }

    private Person getVowel(HttpServletRequest request) {
        String parameter = request.getParameter("vowelID");
        if (parameter == null) {
            return null;
        }
        
        final Integer id = Integer.valueOf(parameter);
        return (Person) RootDomainObject.getInstance().readPartyByOID(id);
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
            
            executeService("ChangeThesisPerson", degreeCurricularPlan.getIdInternal(), thesis, new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));
            
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
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        
        Person selectedPerson = bean.getPerson();
        if (selectedPerson == null) {
            if (bean.getRawPersonName() == null || bean.getRawPersonName().trim().length() == 0) {
                addActionMessage("info", request, "thesis.selectPerson.external.name.required");
            }
            else {
                request.setAttribute("proposeCreation", true);
            }

            return mapping.findForward("select-person");
        }
        else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            
            executeService("ChangeThesisPerson", degreeCurricularPlan.getIdInternal(), thesis, new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));
            
            return editProposal(mapping, actionForm, request, response);
        }
    }
    
    public ActionForward createExternalPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean-invisible");
        RenderUtils.invalidateViewState("bean-invisible");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        return mapping.findForward("select-unit");
    }

    public ActionForward backToSelectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean-invisible");
        RenderUtils.invalidateViewState("bean-invisible");
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        bean.setUnitName(null);
        bean.setRawUnitName(null);
        
        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
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
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        
        Unit selectedUnit = bean.getUnit();
        if (selectedUnit == null) {
            if (bean.getRawUnitName() == null || bean.getRawUnitName().trim().length() == 0) {
                addActionMessage("info", request, "thesis.selectUnit.external.name.required");
            }
            else {
                request.setAttribute("proposeCreation", true);
            }

            return mapping.findForward("select-unit");
        }
        else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            Thesis thesis = getThesis(request);
            
            executeService("ChangeThesisPerson", degreeCurricularPlan.getIdInternal(), thesis, new PersonChange(bean.getTargetType(), bean.getRawPersonName(), bean.getUnit(), bean.getTarget()));
            
            return editProposal(mapping, actionForm, request, response);
        }
    }
    
    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean-invisible");
        
        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);

        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        executeService("ChangeThesisPerson", degreeCurricularPlan.getIdInternal(), thesis, new PersonChange(bean.getTargetType(), bean.getRawPersonName(), bean.getRawUnitName(), bean.getTarget()));
        
        return editProposal(mapping, actionForm, request, response);
    }
    
    public ActionForward listDraft(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("theses", Thesis.getDraftThesis(degreeCurricularPlan.getDegree()));
        
        return mapping.findForward("list-draft");
    }
    
    public ActionForward listSubmitted(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        
        request.setAttribute("theses", Thesis.getSubmittedThesis(degreeCurricularPlan.getDegree()));
        return mapping.findForward("list-submitted");
    }

    public ActionForward listApproved(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        
        request.setAttribute("theses", Thesis.getApprovedThesis(degreeCurricularPlan.getDegree()));
        return mapping.findForward("list-approved");
    }
    
    public ActionForward confirmDeleteProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }

        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("thesis", thesis);
        
        return mapping.findForward("delete-confirm");
    }

    public ActionForward deleteProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }

        try {
            executeService("DeleteThesis", degreeCurricularPlan.getIdInternal(), thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        
        return listDraft(mapping, actionForm, request, response);
    }
    
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }

        request.setAttribute("generalConditions", thesis.getGeneralConditions());
        request.setAttribute("orientationConditions", thesis.getOrientationConditions());
        request.setAttribute("presidentConditions", thesis.getPresidentConditions());
        request.setAttribute("vowelsConditions", thesis.getVowelsConditions());
        
        return mapping.findForward("edit-thesis");
    }
    
    public ActionForward confirmSubmitProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }
        
        request.setAttribute("thesis", thesis);
        
        return mapping.findForward("confirm-submission");
    }
    
    public ActionForward submitProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }

        try {
            executeService("SubmitThesis", degreeCurricularPlan.getIdInternal(), thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        
        return listDraft(mapping, actionForm, request, response);
    }

    public ActionForward viewProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }

        return mapping.findForward("view-thesis");
    }

    public ActionForward approve(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listSubmitted(mapping, actionForm, request, response);
        }
        
        try {
            executeService("ApproveThesis", degreeCurricularPlan.getIdInternal(), thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        
        return listSubmitted(mapping, actionForm, request, response);
    }
    
    public ActionForward confirmThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listApproved(mapping, actionForm, request, response);
        }
        
        return mapping.findForward("confirm-thesis");
    }

    public ActionForward evaluateThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        if (thesis == null) {
            return listApproved(mapping, actionForm, request, response);
        }
        
        return listApproved(mapping, actionForm, request, response);
    }
    
    public ActionForward listConfirmed(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        
        request.setAttribute("theses", Thesis.getConfirmedThesis(degreeCurricularPlan.getDegree()));
        return mapping.findForward("list-confirmed");
    }

    public ActionForward viewConfirmed(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("view-confirmed");
    }
    
    public ActionForward confirmRevision(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("confirm-revision");
    }

    public ActionForward enterRevision(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listConfirmed(mapping, actionForm, request, response);
        }
        
        try {
            executeService("ReviseThesis", degreeCurricularPlan.getIdInternal(), thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        
        return listConfirmed(mapping, actionForm, request, response);
    }
}
