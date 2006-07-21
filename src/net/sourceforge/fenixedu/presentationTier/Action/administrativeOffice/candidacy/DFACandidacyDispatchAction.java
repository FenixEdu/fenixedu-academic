package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.StateMachineRunner;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.CandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class DFACandidacyDispatchAction extends FenixDispatchAction {

    private static final String FILE_DOWNLOAD_URL_FORMAT = FileManagerFactory.getFileManager()
            .getDirectDownloadUrlFormat();

    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        CreateDFACandidacyBean createDFACandidacyBean = new CreateDFACandidacyBean();
        request.setAttribute("candidacyBean", createDFACandidacyBean);
        return mapping.findForward("chooseExecutionDegree");
    }

    public ActionForward chooseDegreePostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject()
                .getObject();
        candidacyBean.setDegreeCurricularPlan(null);
        candidacyBean.setExecutionDegree(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseDegreeCurricularPlanPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject()
                .getObject();

        ExecutionDegree executionDegree = null;
        if (candidacyBean.getDegreeCurricularPlan() != null) {
            executionDegree = Collections.min(candidacyBean.getDegreeCurricularPlan()
                    .getExecutionDegrees(), ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        }

        candidacyBean.setExecutionDegree(executionDegree);
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseExecutionDegreePostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject()
                .getObject();

        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("candidacyBean", RenderUtils.getViewState().getMetaObject().getObject());

        return mapping.getInputForward();
    }

    public ActionForward fillCandidateData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("createCandidacyBean", object);

        return mapping.findForward("fillCandidateData");

    }

    public ActionForward createCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        CreateDFACandidacyBean createDFACandidacyBean = (CreateDFACandidacyBean) RenderUtils
                .getViewState().getMetaObject().getObject();
        DFACandidacy candidacy = null;
        try {
            candidacy = (DFACandidacy) ServiceUtils.executeService(getUserView(request),
                    "CreateDFACandidacy", new Object[] { createDFACandidacyBean.getExecutionDegree(),
                            createDFACandidacyBean.getName(),
                            createDFACandidacyBean.getIdentificationNumber(),
                            createDFACandidacyBean.getIdDocumentType(), createDFACandidacyBean.getContributorNumber() });
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), null);
            RenderUtils.invalidateViewState();
            return prepareCreateCandidacy(mapping, actionForm, request, response);
        } catch (NotAuthorizedFilterException e) {
            addActionMessage(request, "error.not.authorized", null);
            RenderUtils.invalidateViewState();
            return prepareCreateCandidacy(mapping, actionForm, request, response);
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

    public ActionForward prepareGenPass(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        return mapping.findForward("prepareGenPass");

    }

    public ActionForward prepareChooseCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        return mapping.findForward("chooseCandidacy");

    }

    public ActionForward chooseCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

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
        request.setAttribute("fileDownloadUrlFormat", FILE_DOWNLOAD_URL_FORMAT);
    }

    public ActionForward showCandidacyGeneratePass(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return prepareGenPass(mapping, actionForm, request, response);
        }
        if (!candidacy.getActiveCandidacySituation().getCanGeneratePass()) {
            addActionMessage(request, "error.enrolmentFee.to.pay", null);
            request.setAttribute("payed", "false");
        } else {
            request.setAttribute("payed", "true");
        }

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("showCandidacyGeneratePass");

    }

    public ActionForward generatePass(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException, FenixActionException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            throw new FenixActionException("error.invalid.candidacy.number");
        }
        if (!candidacy.getActiveCandidacySituation().getCanGeneratePass()) {
            throw new FenixActionException("error.enrolmentFee.to.pay");
        }

        String pass = (String) ServiceUtils.executeService(getUserView(request), "GenerateNewPassword",
                new Object[] { candidacy.getPerson() });
        request.setAttribute("password", pass);

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("generatePassword");
    }

    public ActionForward prepareValidateCandidacyData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("prepareValidateCandidacyData");

    }

    public ActionForward showCandidacyValidateData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
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

    public ActionForward prepareAlterCandidacyData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return prepareValidateCandidacyData(mapping, actionForm, request, response);
        }

        request.setAttribute("candidacy", candidacy);
        PrecedentDegreeInformation precedentDegreeInformation = ((DFACandidacy) candidacy)
                .getPrecedentDegreeInformation();
        request.setAttribute("precedentDegreeInformation", new PrecedentDegreeInformationBean(
                precedentDegreeInformation));

        return mapping.findForward("showCandidacyAlterData");
    }

    public ActionForward alterCandidacyData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        PrecedentDegreeInformationBean precedentDegreeInformation = (PrecedentDegreeInformationBean) RenderUtils
                .getViewState("precedentDegreeInformation").getMetaObject().getObject();

        if (precedentDegreeInformation.getNewInstitutionName() != null
                || precedentDegreeInformation.getInstitution() != null) {
            Object[] argsInstitution = { precedentDegreeInformation };
            ServiceUtils.executeService(userView, "EditPrecedentDegreeInformation", argsInstitution);
        }

//        Object[] argsStateMachine = { new StateMachineRunner.RunnerArgs(precedentDegreeInformation.getPrecedentDegreeInformation()
//                .getDfaCandidacy().getActiveCandidacySituation(), CandidacySituationType.STAND_BY_FILLED_DATA.toString()) };
//        try {
//            ServiceUtils.executeService(userView, "StateMachineRunner", argsStateMachine);
//        } catch (DomainException e) {
//            // Didn't move to next state
//        }

        request.setAttribute("candidacyID", precedentDegreeInformation.getPrecedentDegreeInformation()
                .getDfaCandidacy().getIdInternal());

        return mapping.findForward("alterSuccess");
    }

    public ActionForward validateData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixActionException, FenixServiceException {
        return goToNextState(mapping, actionForm, request, response,
                CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString());
    }

    public ActionForward invalidateData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixActionException, FenixServiceException {
        return goToNextState(mapping, actionForm, request, response, CandidacySituationType.STAND_BY
                .toString());
    }

    private ActionForward goToNextState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, String nextState)
            throws FenixActionException, FenixFilterException, FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            throw new FenixActionException("invalid cadidacy number");
        }
        Object[] args = { new StateMachineRunner.RunnerArgs(candidacy.getActiveCandidacySituation(),
                nextState) };
        try {
            ServiceUtils.executeService(getUserView(request), "StateMachineRunner", args);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), null);
            request.setAttribute("candidacy", candidacy);
            return mapping.findForward("showCandidacyValidateData");
        }
        request.setAttribute("candidacyID", candidacy.getIdInternal().toString());
        return viewCandidacy(mapping, actionForm, request, response);
    }

    public ActionForward prepareListCandidacies(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DFACandidacyBean candidacyBean = new DFACandidacyBean();
        request.setAttribute("candidacyBean", candidacyBean);
        return mapping.findForward("listCandidacies");
    }

    public ActionForward listCandidacies(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) RenderUtils.getViewState()
                .getMetaObject().getObject();
        Set<DFACandidacy> candidacies = dfaCandidacyBean.getExecutionDegree().getDfaCandidaciesSet();
        request.setAttribute("candidacies", candidacies);
        request.setAttribute("candidacyBean", dfaCandidacyBean);
        return mapping.findForward("listCandidacies");
    }

    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        String candidacyID = (String) getFromRequest(request, "candidacyID");

        Candidacy candidacy = rootDomainObject.readCandidacyByOID(Integer.valueOf(candidacyID));
        if (candidacy == null) {
            throw new FenixActionException("error.invalid.candidacy.number");
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

}
