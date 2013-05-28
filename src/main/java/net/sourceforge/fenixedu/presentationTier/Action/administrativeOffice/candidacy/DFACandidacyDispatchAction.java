package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy.CreateCandidacy;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy.EditPrecedentDegreeInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy.RegisterCandidate;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.StateMachineRunner;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.GenerateNewPasswordService;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.RegisterCandidacyBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.CandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;

public class DFACandidacyDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        CreateDFACandidacyBean createDFACandidacyBean = new CreateDFACandidacyBean();
        request.setAttribute("candidacyBean", createDFACandidacyBean);
        return mapping.findForward("chooseExecutionDegree");
    }

    public ActionForward chooseDegreeTypePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        candidacyBean.setDegree(null);
        candidacyBean.setDegreeCurricularPlan(null);
        candidacyBean.setExecutionYear(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseDegreePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        candidacyBean.setDegreeCurricularPlan(null);
        candidacyBean.setExecutionYear(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseDegreeCurricularPlanPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseExecutionDegreePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();

        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("candidacyBean", RenderUtils.getViewState().getMetaObject().getObject());

        return mapping.getInputForward();
    }

    public ActionForward fillCandidateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("createCandidacyBean", object);

        return mapping.findForward("fillCandidateData");

    }

    public ActionForward createCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        CreateDFACandidacyBean createDFACandidacyBean =
                (CreateDFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        Candidacy candidacy = null;
        try {
            candidacy =
                    CreateCandidacy.run(createDFACandidacyBean.getExecutionDegree(), createDFACandidacyBean.getDegreeType(),
                            createDFACandidacyBean.getName(), createDFACandidacyBean.getIdentificationNumber(),
                            createDFACandidacyBean.getIdDocumentType(), createDFACandidacyBean.getContributorNumber(),
                            createDFACandidacyBean.getCandidacyDate());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            RenderUtils.invalidateViewState();
            return prepareCreateCandidacy(mapping, actionForm, request, response);
        } catch (IllegalDataAccessException e) {
            addActionMessage(request, "error.not.authorized");
            RenderUtils.invalidateViewState();
            return prepareCreateCandidacy(mapping, actionForm, request, response);
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

    public ActionForward prepareGenPass(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return mapping.findForward("prepareGenPass");

    }

    public ActionForward prepareChooseCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return mapping.findForward("chooseCandidacy");

    }

    public ActionForward chooseCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return mapping.findForward("chooseCandidacy");
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");

    }

    private void storeCandidacyDataInRequest(HttpServletRequest request, Candidacy candidacy) {
        List<CandidacyDocumentUploadBean> candidacyDocuments = new ArrayList<CandidacyDocumentUploadBean>();
        for (CandidacyDocument candidacyDocument : candidacy.getCandidacyDocuments()) {
            candidacyDocuments.add(new CandidacyDocumentUploadBean(candidacyDocument));
        }

        request.setAttribute("candidacyDocuments", candidacyDocuments);
        request.setAttribute("candidacy", candidacy);

    }

    public ActionForward showCandidacyGeneratePass(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return prepareGenPass(mapping, actionForm, request, response);
        }
        if (!candidacy.getActiveCandidacySituation().getCanGeneratePass()) {
            addActionMessage(request, "error.enrolmentFee.to.pay");
            request.setAttribute("payed", "false");
        } else {
            request.setAttribute("payed", "true");
        }

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("showCandidacyGeneratePass");

    }

    public ActionForward generatePass(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException,  FenixActionException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            throw new FenixActionException("error.invalid.candidacy.number");
        }
        if (!candidacy.getActiveCandidacySituation().getCanGeneratePass()) {
            throw new FenixActionException("error.enrolmentFee.to.pay");
        }

        String pass = GenerateNewPasswordService.run(candidacy.getPerson());
        request.setAttribute("password", pass);

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("generatePassword");
    }

    public ActionForward prepareValidateCandidacyData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("prepareValidateCandidacyData");

    }

    public ActionForward showCandidacyValidateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return prepareValidateCandidacyData(mapping, actionForm, request, response);
        }

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("showCandidacyValidateData");
    }

    public ActionForward prepareAlterCandidacyData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return prepareValidateCandidacyData(mapping, actionForm, request, response);
        }

        request.setAttribute("candidacy", candidacy);

        PrecedentDegreeInformation precedentDegreeInformation = ((StudentCandidacy) candidacy).getPrecedentDegreeInformation();
        request.setAttribute("precedentDegreeInformation", new PrecedentDegreeInformationBean(precedentDegreeInformation));

        return mapping.findForward("showCandidacyAlterData");
    }

    public ActionForward schoolLevelPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        PrecedentDegreeInformationBean precedentDegreeInformationBean = getRenderedObject("precedentDegreeInformation");
        request.setAttribute("candidacy", precedentDegreeInformationBean.getPrecedentDegreeInformation().getStudentCandidacy());
        RenderUtils.invalidateViewState();
        request.setAttribute("precedentDegreeInformation", precedentDegreeInformationBean);

        return mapping.findForward("showCandidacyAlterData");
    }

    public ActionForward alterCandidacyData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        IUserView userView = UserView.getUser();

        PrecedentDegreeInformationBean precedentDegreeInformation =
                (PrecedentDegreeInformationBean) RenderUtils.getViewState("precedentDegreeInformation").getMetaObject()
                        .getObject();

        EditPrecedentDegreeInformation.run(precedentDegreeInformation);

        request.setAttribute("candidacyID", precedentDegreeInformation.getPrecedentDegreeInformation().getStudentCandidacy()
                .getExternalId());

        return mapping.findForward("alterSuccess");
    }

    public ActionForward validateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixActionException, FenixServiceException {
        return goToNextState(mapping, actionForm, request, response, CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString());
    }

    public ActionForward invalidateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixActionException, FenixServiceException {
        return goToNextState(mapping, actionForm, request, response, CandidacySituationType.STAND_BY.toString());
    }

    private ActionForward goToNextState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, String nextState) throws FenixActionException, 
            FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            throw new FenixActionException("invalid cadidacy number");
        }

        try {
            StateMachineRunner.run(new StateMachineRunner.RunnerArgs(candidacy.getActiveCandidacySituation(), nextState));
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("candidacy", candidacy);
            // return mapping.findForward("showCandidacyValidateData");
        }
        request.setAttribute("candidacyID", candidacy.getExternalId().toString());
        return viewCandidacy(mapping, actionForm, request, response);
    }

    public ActionForward prepareListCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DFACandidacyBean candidacyBean = new DFACandidacyBean();
        request.setAttribute("candidacyBean", candidacyBean);
        return mapping.findForward("listCandidacies");
    }

    public ActionForward listCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        Set<DFACandidacy> candidacies = dfaCandidacyBean.getExecutionDegree().getDfaCandidacies();
        request.setAttribute("candidacies", candidacies);
        request.setAttribute("candidacyBean", dfaCandidacyBean);
        return mapping.findForward("listCandidacies");
    }

    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String candidacyID = (String) getFromRequest(request, "candidacyID");

        Candidacy candidacy = rootDomainObject.readCandidacyByOID(Integer.valueOf(candidacyID));
        if (candidacy == null) {
            throw new FenixActionException("error.invalid.candidacy.number");
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

    public ActionForward prepareRegisterCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer candidacyNumber = Integer.valueOf((String) getFromRequest(request, "candidacyNumber"));

        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            throw new FenixActionException("error.invalid.candidacy.number");
        }

        request.setAttribute("candidacy", candidacy);
        request.setAttribute("registerCandidacyBean", new RegisterCandidacyBean((StudentCandidacy) candidacy));
        ((DynaActionForm) actionForm).set("candidacyNumber", candidacyNumber);

        return mapping.findForward("candidacyRegistration");
    }

    public ActionForward registerCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {

        RegisterCandidacyBean registerCandidacyBean =
                (RegisterCandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        Candidacy candidacy = registerCandidacyBean.getCandidacy();

        try {
            RegisterCandidate.run(registerCandidacyBean);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("candidacy", candidacy);
            return mapping.findForward("candidacyRegistration");
        }

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("candidacyRegistrationSuccess");
    }

    public ActionForward cancelRegisterCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm candidacyForm = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) candidacyForm.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

    public ActionForward printRegistrationInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm candidacyForm = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) candidacyForm.get("candidacyNumber");

        request.setAttribute("candidacy", Candidacy.readByCandidacyNumber(candidacyNumber));
        return mapping.findForward("printRegistrationInformation");
    }

    public ActionForward cancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {

        return goToNextState(mapping, actionForm, request, response, CandidacySituationType.CANCELLED.toString());
    }

}