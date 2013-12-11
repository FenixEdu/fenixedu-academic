package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.general.ReadAllCountries;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate.ChangeApplicationInfo;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadMasterDegreeCandidateByID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountryEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.GenderHelper;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.Data;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

public class ChangeApplicationInfoDispatchAction extends FenixDispatchAction {

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();
        DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;

        String candidateID = (String) changeApplicationInfoForm.get("candidateID");
        request.setAttribute("candidateID", candidateID);

        if (!isTokenValid(request)) {
            return mapping.findForward("BackError");
        }
        generateToken(request);
        saveToken(request);

        InfoMasterDegreeCandidate masterDegreeCandidate = readMasterDegreeCandidate(userView, candidateID);

        InfoPersonEditor infoPerson = new InfoPersonEditor();

        // Create Dates
        infoPerson.setNascimento(buildDate(changeApplicationInfoForm, "birthDay", "birthMonth", "birthYear"));
        infoPerson.setDataEmissaoDocumentoIdentificacao(buildDate(changeApplicationInfoForm, "idIssueDateDay",
                "idIssueDateMonth", "idIssueDateYear"));
        infoPerson.setDataValidadeDocumentoIdentificacao(buildDate(changeApplicationInfoForm, "idExpirationDateDay",
                "idExpirationDateMonth", "idExpirationDateYear"));

        InfoCountryEditor nationality = new InfoCountryEditor();
        nationality.setNationality((String) changeApplicationInfoForm.get("nationality"));

        infoPerson.setTipoDocumentoIdentificacao(IDDocumentType.valueOf((String) changeApplicationInfoForm
                .get("identificationDocumentType")));
        infoPerson.setNumeroDocumentoIdentificacao((String) changeApplicationInfoForm.get("identificationDocumentNumber"));
        infoPerson.setLocalEmissaoDocumentoIdentificacao((String) changeApplicationInfoForm
                .get("identificationDocumentIssuePlace"));
        infoPerson.setNome((String) changeApplicationInfoForm.get("name"));

        String aux = (String) changeApplicationInfoForm.get("sex");
        if (StringUtils.isEmpty(aux)) {
            infoPerson.setSexo(null);
        } else {
            infoPerson.setSexo(Gender.valueOf(aux));
        }

        aux = (String) changeApplicationInfoForm.get("maritalStatus");
        if (StringUtils.isEmpty(aux)) {
            infoPerson.setMaritalStatus(null);
        } else {
            infoPerson.setMaritalStatus(MaritalStatus.valueOf(aux));
        }

        infoPerson.setInfoPais(nationality);
        infoPerson.setNomePai((String) changeApplicationInfoForm.get("fatherName"));
        infoPerson.setNomeMae((String) changeApplicationInfoForm.get("motherName"));
        infoPerson.setFreguesiaNaturalidade((String) changeApplicationInfoForm.get("birthPlaceParish"));
        infoPerson.setConcelhoNaturalidade((String) changeApplicationInfoForm.get("birthPlaceMunicipality"));
        infoPerson.setDistritoNaturalidade((String) changeApplicationInfoForm.get("birthPlaceDistrict"));
        infoPerson.setMorada((String) changeApplicationInfoForm.get("address"));
        infoPerson.setLocalidade((String) changeApplicationInfoForm.get("place"));
        infoPerson.setCodigoPostal((String) changeApplicationInfoForm.get("postCode"));
        infoPerson.setFreguesiaMorada((String) changeApplicationInfoForm.get("addressParish"));
        infoPerson.setConcelhoMorada((String) changeApplicationInfoForm.get("addressMunicipality"));
        infoPerson.setDistritoMorada((String) changeApplicationInfoForm.get("addressDistrict"));

        infoPerson.setTelefone((String) changeApplicationInfoForm.get("telephone"));
        infoPerson.setTelemovel((String) changeApplicationInfoForm.get("mobilePhone"));
        infoPerson.setEmail((String) changeApplicationInfoForm.get("email"));
        infoPerson.setEnderecoWeb((String) changeApplicationInfoForm.get("webSite"));

        infoPerson.setNumContribuinte((String) changeApplicationInfoForm.get("contributorNumber"));
        infoPerson.setProfissao((String) changeApplicationInfoForm.get("occupation"));
        infoPerson.setUsername((String) changeApplicationInfoForm.get("username"));
        infoPerson.setLocalidadeCodigoPostal((String) changeApplicationInfoForm.get("areaOfAreaCode"));

        masterDegreeCandidate.setAverage(Double.valueOf((String) changeApplicationInfoForm.get("average")));
        masterDegreeCandidate.setMajorDegree((String) changeApplicationInfoForm.get("majorDegree"));
        masterDegreeCandidate.setMajorDegreeSchool((String) changeApplicationInfoForm.get("majorDegreeSchool"));
        masterDegreeCandidate.setMajorDegreeYear((Integer) changeApplicationInfoForm.get("majorDegreeYear"));
        masterDegreeCandidate.setSpecializationArea((String) changeApplicationInfoForm.get("specializationArea"));

        Boolean isNewPerson = false;
        if (userView.getPerson().getPersonRolesSet().size() == 2) {
            isNewPerson = true;
        }

        try {

            masterDegreeCandidate = ChangeApplicationInfo.run(masterDegreeCandidate, infoPerson, userView, isNewPerson);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE, masterDegreeCandidate);

        return mapping.findForward("Success");
    }

    private Date buildDate(DynaActionForm form, String dayField, String monthField, String yearField) {
        String dayString = (String) form.get(dayField);
        String monthString = (String) form.get(monthField);
        String yearString = (String) form.get(yearField);

        if (StringUtils.isEmpty(dayString) || StringUtils.isEmpty(monthString) || StringUtils.isEmpty(yearString)) {
            return null;
        } else {
            Integer day = new Integer((String) form.get("birthDay"));
            Integer month = new Integer((String) form.get("birthMonth"));
            Integer year = new Integer((String) form.get("birthYear"));

            Calendar resultDate = Calendar.getInstance();
            resultDate.set(year, month, day);
            return resultDate.getTime();
        }
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        String choosenCandidateID = request.getParameter("candidateID");
        request.setAttribute("candidateID", choosenCandidateID);

        InfoMasterDegreeCandidate masterDegreeCandidate = readMasterDegreeCandidate(userView, choosenCandidateID);

        InfoCandidateSituation infoCandidateSituation = masterDegreeCandidate.getInfoCandidateSituation();

        // Check if the info can be changed
        if ((infoCandidateSituation == null) || !(infoCandidateSituation.getSituation().equals(SituationName.PENDENTE_OBJ))) {
            request.setAttribute(PresentationConstants.CANDIDATE_SITUATION, infoCandidateSituation);
            return mapping.findForward("Unchangeable");
        }

        boolean validationError = request.getParameter("error") != null;
        if (!validationError) {
            populateForm(changeApplicationInfoForm, masterDegreeCandidate.getInfoPerson(), masterDegreeCandidate);
        }

        // Get List of available Countries
        List<InfoCountry> country = null;
        try {
            country = (ArrayList) ReadAllCountries.run();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        // Build List of Countries for the Form
        List nationalityList = new ArrayList();
        for (InfoCountry countryTemp : country) {
            nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp.getNationality()));
        }

        request.setAttribute(PresentationConstants.NATIONALITY_LIST_KEY, nationalityList);
        request.setAttribute(PresentationConstants.SEX_LIST_KEY,
                GenderHelper.getSexLabelValues((Locale) request.getAttribute(Globals.LOCALE_KEY)));
        request.setAttribute(PresentationConstants.MONTH_DAYS_KEY, Data.getMonthDays());
        request.setAttribute(PresentationConstants.MONTH_LIST_KEY, Data.getMonths());
        request.setAttribute(PresentationConstants.YEARS_KEY, Data.getYears());
        request.setAttribute(PresentationConstants.EXPIRATION_YEARS_KEY, Data.getExpirationYears());
        request.setAttribute(PresentationConstants.PERSONAL_INFO_KEY, masterDegreeCandidate.getInfoPerson());
        changeApplicationInfoForm.set("candidateID", masterDegreeCandidate.getExternalId());

        // if New Person -> All personal info can be changed
        if (userView.getPerson().getPersonRolesSet().size() == 2) {
            request.setAttribute("newPerson", "true");
        }

        generateToken(request);
        saveToken(request);

        return mapping.findForward("prepareReady");

    }

    private InfoMasterDegreeCandidate readMasterDegreeCandidate(User userView, String candidateID) throws FenixActionException {
        InfoMasterDegreeCandidate masterDegreeCandidate = null;
        masterDegreeCandidate = ReadMasterDegreeCandidateByID.run(candidateID);
        return masterDegreeCandidate;
    }

    private void populateForm(DynaActionForm changeApplicationInfoForm, InfoPerson infoPerson,
            InfoMasterDegreeCandidate infoMasterDegreeCandidate) {
        changeApplicationInfoForm.set("identificationDocumentNumber", infoPerson.getNumeroDocumentoIdentificacao());
        changeApplicationInfoForm.set("identificationDocumentType", infoPerson.getTipoDocumentoIdentificacao().toString());
        changeApplicationInfoForm.set("identificationDocumentIssuePlace", infoPerson.getLocalEmissaoDocumentoIdentificacao());
        changeApplicationInfoForm.set("name", infoPerson.getNome());

        Calendar birthDate = Calendar.getInstance();
        if (infoPerson.getNascimento() == null) {
            changeApplicationInfoForm.set("birthDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthYear", Data.OPTION_DEFAULT);
        } else {
            birthDate.setTime(infoPerson.getNascimento());
            changeApplicationInfoForm.set("birthDay", new Integer(birthDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("birthMonth", new Integer(birthDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("birthYear", new Integer(birthDate.get(Calendar.YEAR)).toString());
        }

        Calendar identificationDocumentIssueDate = Calendar.getInstance();
        if (infoPerson.getDataEmissaoDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idIssueDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentIssueDate.setTime(infoPerson.getDataEmissaoDocumentoIdentificacao());
            changeApplicationInfoForm.set("idIssueDateDay",
                    new Integer(identificationDocumentIssueDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateMonth",
                    new Integer(identificationDocumentIssueDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateYear",
                    new Integer(identificationDocumentIssueDate.get(Calendar.YEAR)).toString());
        }

        Calendar identificationDocumentExpirationDate = Calendar.getInstance();
        if (infoPerson.getDataValidadeDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idExpirationDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentExpirationDate.setTime(infoPerson.getDataValidadeDocumentoIdentificacao());
            changeApplicationInfoForm.set("idExpirationDateDay",
                    new Integer(identificationDocumentExpirationDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateMonth",
                    new Integer(identificationDocumentExpirationDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateYear",
                    new Integer(identificationDocumentExpirationDate.get(Calendar.YEAR)).toString());
        }

        changeApplicationInfoForm.set("fatherName", infoPerson.getNomePai());
        changeApplicationInfoForm.set("motherName", infoPerson.getNomeMae());
        if (infoPerson.getInfoPais() != null) {
            changeApplicationInfoForm.set("nationality", infoPerson.getInfoPais().getNationality());
        } else {
            changeApplicationInfoForm.set("nationality", null);
        }
        changeApplicationInfoForm.set("birthPlaceParish", infoPerson.getFreguesiaNaturalidade());
        changeApplicationInfoForm.set("birthPlaceMunicipality", infoPerson.getConcelhoNaturalidade());
        changeApplicationInfoForm.set("birthPlaceDistrict", infoPerson.getDistritoNaturalidade());
        changeApplicationInfoForm.set("address", infoPerson.getMorada());
        changeApplicationInfoForm.set("place", infoPerson.getLocalidade());
        changeApplicationInfoForm.set("postCode", infoPerson.getCodigoPostal());
        changeApplicationInfoForm.set("addressParish", infoPerson.getFreguesiaMorada());
        changeApplicationInfoForm.set("addressMunicipality", infoPerson.getConcelhoMorada());
        changeApplicationInfoForm.set("addressDistrict", infoPerson.getDistritoMorada());
        changeApplicationInfoForm.set("telephone", infoPerson.getTelefone());
        changeApplicationInfoForm.set("mobilePhone", infoPerson.getTelemovel());
        changeApplicationInfoForm.set("email", infoPerson.getEmail());
        changeApplicationInfoForm.set("webSite", infoPerson.getEnderecoWeb());
        changeApplicationInfoForm.set("contributorNumber", infoPerson.getNumContribuinte());
        changeApplicationInfoForm.set("occupation", infoPerson.getProfissao());
        changeApplicationInfoForm.set("username", infoPerson.getUsername());
        changeApplicationInfoForm.set("areaOfAreaCode", infoPerson.getLocalidadeCodigoPostal());

        changeApplicationInfoForm.set("majorDegree", infoMasterDegreeCandidate.getMajorDegree());
        changeApplicationInfoForm.set("majorDegreeSchool", infoMasterDegreeCandidate.getMajorDegreeSchool());
        changeApplicationInfoForm.set("majorDegreeYear", infoMasterDegreeCandidate.getMajorDegreeYear());
        changeApplicationInfoForm.set("specializationArea", infoMasterDegreeCandidate.getSpecializationArea());
        changeApplicationInfoForm.set("average",
                (infoMasterDegreeCandidate.getAverage() == null) ? null : infoMasterDegreeCandidate.getAverage().toString());

        if (infoPerson.getSexo() != null) {
            changeApplicationInfoForm.set("sex", infoPerson.getSexo().toString());
        }
        if (infoPerson.getMaritalStatus() != null) {
            changeApplicationInfoForm.set("maritalStatus", infoPerson.getMaritalStatus().toString());
        }
    }

}