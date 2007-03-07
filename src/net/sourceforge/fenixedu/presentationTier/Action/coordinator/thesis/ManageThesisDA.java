package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ThesisBean;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ThesisBean.PersonTarget;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
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
        ThesisBean bean = new ThesisBean(getDegreeCurricularPlan(request));
        
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
//            ProposalAssignment assignment = student.getThesisAssignment();
//            Proposal proposal = assignment == null ? null : assignment.getProposal();
//            
//            bean.setStudent(proposal.getStudent());
//            bean.setTitle(proposal.getTitle());
//            bean.setOrientator(proposal.getOrientator());
//            bean.setOrientator(proposal.getCoorientator());
            return mapping.findForward("manage-thesis");
        }
    }
    
    public ActionForward changeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        RenderUtils.invalidateViewState("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        return mapping.findForward("change-information");
    }
    
    public ActionForward changeInformationAgain(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        return mapping.findForward("change-information");
    }
    
    public ActionForward changePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean remove = request.getParameter("remove") != null;
        
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        RenderUtils.invalidateViewState("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);
        
        if (target == null) {
            return mapping.findForward("manage-thesis");
        }
        
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
            bean.changePerson(null);
            return mapping.findForward("manage-thesis");
        }
        
        return mapping.findForward("select-person");
    }

    public ActionForward changePersonInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }

    public ActionForward backToCreation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean-invisible");
        RenderUtils.invalidateViewState("bean-invisible");
        
        if (bean == null) {
            bean = (ThesisBean) getRenderedObject("bean");
            RenderUtils.invalidateViewState("bean");
            
            if (bean == null) {
                return searchStudent(mapping, actionForm, request, response);
            }
        }
        
        bean.setInternal(true);
        bean.setPersonName(null);
        bean.setRawPersonName(null);
        bean.setUnitName(null);
        bean.setRawUnitName(null);
        
        request.setAttribute("bean", bean);
        return mapping.findForward("manage-thesis");
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
    
    private Person getVowel(HttpServletRequest request) {
        String parameter = request.getParameter("vowelID");
        if (parameter == null) {
            return null;
        }
        
        final Integer id = Integer.valueOf(parameter);
        return (Person) RootDomainObject.getInstance().readPartyByOID(id);
    }

    public ActionForward selectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
        
        request.setAttribute("bean", bean);
        
        Person selectedPerson = bean.getPerson();
        if (selectedPerson == null) {
            addActionMessage("info", request, "thesis.selectPerson.internal.required");
            return mapping.findForward("select-person");
        }
        else {
            bean.changePerson(selectedPerson);
            
            return backToCreation(mapping, actionForm, request, response);
        }
    }

    public ActionForward selectPersonInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }
     
        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }
    
    public ActionForward selectExternalPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
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
            bean.changePerson(selectedPerson);
            
            return backToCreation(mapping, actionForm, request, response);
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
            return searchStudent(mapping, actionForm, request, response);
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
            return searchStudent(mapping, actionForm, request, response);
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
            ExternalContract contract = (ExternalContract) executeService("InsertExternalPerson", bean.getRawPersonName(), bean.getUnit());
            bean.changePerson(contract.getPerson());
            
            return backToCreation(mapping, actionForm, request, response);
        }
    }
    
    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean-invisible");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);

        ExternalContract contract = (ExternalContract) executeService("InsertExternalPerson", bean.getRawPersonName(), bean.getRawUnitName());
        bean.changePerson(contract.getPerson());
        
        return backToCreation(mapping, actionForm, request, response);
    }
    
    public ActionForward updateProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisBean bean = (ThesisBean) getRenderedObject("bean");
        
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);
        
        executeService("UpdateThesis", bean.getDegreeCurricularPlan().getIdInternal(), bean);
        return listDraft(mapping, actionForm, request, response);
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
    
    private Thesis getThesis(HttpServletRequest request) {
        Integer id = getId(request.getParameter("thesisID"));
        if (id == null) {
            return null;
        }
        else {
            return RootDomainObject.getInstance().readThesisByOID(id);
        }
    }
    
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }

        ThesisBean bean = new ThesisBean(degreeCurricularPlan, thesis);

        request.setAttribute("bean", bean);
        
        return mapping.findForward("manage-thesis");
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

        executeService("SubmitThesis", degreeCurricularPlan.getIdInternal(), thesis);
        return listDraft(mapping, actionForm, request, response);
    }
    
    public ActionForward viewProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Thesis thesis = getThesis(request);
        
        if (thesis == null) {
            return listDraft(mapping, actionForm, request, response);
        }

        ThesisBean bean = new ThesisBean(degreeCurricularPlan, thesis);

        request.setAttribute("bean", bean);
        
        return mapping.findForward("view-thesis");
    }
    
}
