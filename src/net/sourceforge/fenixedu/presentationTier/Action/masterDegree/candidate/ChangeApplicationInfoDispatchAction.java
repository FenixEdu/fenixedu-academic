/*
 * ChangeCandidateApplicationFormAction.java
 * 
 * 
 * Created on 14 de Dezembro de 2002, 12:31
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;
import net.sourceforge.fenixedu.util.EstadoCivil;
import net.sourceforge.fenixedu.util.Sexo;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.TipoDocumentoIdentificacao;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

public class ChangeApplicationInfoDispatchAction extends DispatchAction {

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            if (!isTokenValid(request)) {
                return mapping.findForward("BackError");
            }
            generateToken(request);
            saveToken(request);

            InfoMasterDegreeCandidate newMasterDegreeCandidate = (InfoMasterDegreeCandidate) session
                    .getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);

            // Create Dates

            Calendar birthDate = Calendar.getInstance();
            Calendar idDocumentIssueDate = Calendar.getInstance();
            Calendar idDocumentExpirationDate = Calendar.getInstance();

            InfoPerson infoPerson = new InfoPerson();

            String dayString = (String) changeApplicationInfoForm.get("birthDay");
            String monthString = (String) changeApplicationInfoForm.get("birthMonth");
            String yearString = (String) changeApplicationInfoForm.get("birthYear");

            Integer day = null;
            Integer month = null;
            Integer year = null;

            if ((dayString == null) || (monthString == null) || (yearString == null)
                    || (dayString.length() == 0) || (monthString.length() == 0)
                    || (yearString.length() == 0)) {
                infoPerson.setNascimento(null);
            } else {
                day = new Integer((String) changeApplicationInfoForm.get("birthDay"));
                month = new Integer((String) changeApplicationInfoForm.get("birthMonth"));
                year = new Integer((String) changeApplicationInfoForm.get("birthYear"));

                birthDate.set(year.intValue(), month.intValue(), day.intValue());
                infoPerson.setNascimento(birthDate.getTime());
            }

            dayString = (String) changeApplicationInfoForm.get("idIssueDateDay");
            monthString = (String) changeApplicationInfoForm.get("idIssueDateMonth");
            yearString = (String) changeApplicationInfoForm.get("idIssueDateYear");

            if ((dayString == null) || (monthString == null) || (yearString == null)
                    || (dayString.length() == 0) || (monthString.length() == 0)
                    || (yearString.length() == 0)) {
                infoPerson.setDataEmissaoDocumentoIdentificacao(null);
            } else {
                day = new Integer((String) changeApplicationInfoForm.get("idIssueDateDay"));
                month = new Integer((String) changeApplicationInfoForm.get("idIssueDateMonth"));
                year = new Integer((String) changeApplicationInfoForm.get("idIssueDateYear"));

                idDocumentIssueDate.set(year.intValue(), month.intValue(), day.intValue());
                infoPerson.setDataEmissaoDocumentoIdentificacao(idDocumentIssueDate.getTime());
            }

            dayString = (String) changeApplicationInfoForm.get("idExpirationDateDay");
            monthString = (String) changeApplicationInfoForm.get("idExpirationDateMonth");
            yearString = (String) changeApplicationInfoForm.get("idExpirationDateYear");

            if ((dayString == null) || (monthString == null) || (yearString == null)
                    || (dayString.length() == 0) || (monthString.length() == 0)
                    || (yearString.length() == 0)) {
                infoPerson.setDataValidadeDocumentoIdentificacao(null);
            } else {
                day = new Integer((String) changeApplicationInfoForm.get("idExpirationDateDay"));
                month = new Integer((String) changeApplicationInfoForm.get("idExpirationDateMonth"));
                year = new Integer((String) changeApplicationInfoForm.get("idExpirationDateYear"));

                idDocumentExpirationDate.set(year.intValue(), month.intValue(), day.intValue());
                infoPerson.setDataValidadeDocumentoIdentificacao(idDocumentExpirationDate.getTime());
            }

            InfoCountry nationality = new InfoCountry();
            nationality.setNationality((String) changeApplicationInfoForm.get("nationality"));

            infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
                    (String) changeApplicationInfoForm.get("identificationDocumentType")));
            infoPerson.setNumeroDocumentoIdentificacao((String) changeApplicationInfoForm
                    .get("identificationDocumentNumber"));
            infoPerson.setLocalEmissaoDocumentoIdentificacao((String) changeApplicationInfoForm
                    .get("identificationDocumentIssuePlace"));
            infoPerson.setNome((String) changeApplicationInfoForm.get("name"));

            String aux = (String) changeApplicationInfoForm.get("sex");
            if ((aux == null) || (aux.length() == 0))
                infoPerson.setSexo(null);
            else
                infoPerson.setSexo(new Sexo(aux));

            aux = (String) changeApplicationInfoForm.get("maritalStatus");
            if ((aux == null) || (aux.length() == 0))
                infoPerson.setEstadoCivil(null);
            else
                infoPerson.setEstadoCivil(new EstadoCivil(aux));

            infoPerson.setInfoPais(nationality);
            infoPerson.setNomePai((String) changeApplicationInfoForm.get("fatherName"));
            infoPerson.setNomeMae((String) changeApplicationInfoForm.get("motherName"));
            infoPerson.setFreguesiaNaturalidade((String) changeApplicationInfoForm
                    .get("birthPlaceParish"));
            infoPerson.setConcelhoNaturalidade((String) changeApplicationInfoForm
                    .get("birthPlaceMunicipality"));
            infoPerson.setDistritoNaturalidade((String) changeApplicationInfoForm
                    .get("birthPlaceDistrict"));
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
            infoPerson.setLocalidadeCodigoPostal((String) changeApplicationInfoForm
                    .get("areaOfAreaCode"));

            newMasterDegreeCandidate.setAverage(Double.valueOf((String) changeApplicationInfoForm
                    .get("average")));
            newMasterDegreeCandidate.setMajorDegree((String) changeApplicationInfoForm
                    .get("majorDegree"));
            newMasterDegreeCandidate.setMajorDegreeSchool((String) changeApplicationInfoForm
                    .get("majorDegreeSchool"));
            newMasterDegreeCandidate.setMajorDegreeYear((Integer) changeApplicationInfoForm
                    .get("majorDegreeYear"));
            newMasterDegreeCandidate.setSpecializationArea((String) changeApplicationInfoForm
                    .get("specializationArea"));

            Object args[] = { newMasterDegreeCandidate, infoPerson, userView };

            try {
                newMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                        .executeService(userView, "ChangeApplicationInfo", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);
            session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, newMasterDegreeCandidate);

            return mapping.findForward("Success");
        }
        throw new Exception();
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) session
                    .getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            InfoCandidateSituation infoCandidateSituation = infoMasterDegreeCandidate
                    .getInfoCandidateSituation();

            if ((infoCandidateSituation == null)
                    || !(infoCandidateSituation.getSituation().equals(SituationName.PENDENTE_OBJ))) {
                session.setAttribute(SessionConstants.CANDIDATE_SITUATION, infoCandidateSituation);
                return mapping.findForward("Unchangeable");
            }

            Object args[] = { userView };
            InfoPerson infoPerson = null;
            try {
                infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                        "ReadPersonByUsername", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            boolean validationError = request.getParameter("error") != null;
            if (!validationError)
                populateForm(changeApplicationInfoForm, infoPerson, infoMasterDegreeCandidate);

            // Get List of available Countries
            List country = null;

            try {
                country = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                        "ReadAllCountries", null);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            // Build List of Countries for the Form
            Iterator iterador = country.iterator();

            List nationalityList = new ArrayList();
            while (iterador.hasNext()) {
                InfoCountry countryTemp = (InfoCountry) iterador.next();
                nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp
                        .getNationality()));
            }

            request.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);
            request.setAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY, new EstadoCivil()
                    .toArrayList());
            request.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY,
                    TipoDocumentoIdentificacao.toArrayList());
            request.setAttribute(SessionConstants.SEX_LIST_KEY, new Sexo().toArrayList());
            request.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
            request.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
            request.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
            request.setAttribute(SessionConstants.EXPIRATION_YEARS_KEY, Data.getExpirationYears());
            request.setAttribute(SessionConstants.PERSONAL_INFO_KEY, infoPerson);

            generateToken(request);
            saveToken(request);
            return mapping.findForward("prepareReady");
        }
        throw new Exception();
    }

    private void populateForm(DynaActionForm changeApplicationInfoForm, InfoPerson infoPerson,
            InfoMasterDegreeCandidate infoMasterDegreeCandidate) {
        changeApplicationInfoForm.set("identificationDocumentNumber", infoPerson
                .getNumeroDocumentoIdentificacao());
        changeApplicationInfoForm.set("identificationDocumentType", infoPerson
                .getTipoDocumentoIdentificacao().toString());
        changeApplicationInfoForm.set("identificationDocumentIssuePlace", infoPerson
                .getLocalEmissaoDocumentoIdentificacao());
        changeApplicationInfoForm.set("name", infoPerson.getNome());

        Calendar birthDate = Calendar.getInstance();
        if (infoPerson.getNascimento() == null) {
            changeApplicationInfoForm.set("birthDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthYear", Data.OPTION_DEFAULT);
        } else {
            birthDate.setTime(infoPerson.getNascimento());
            changeApplicationInfoForm.set("birthDay", new Integer(birthDate.get(Calendar.DAY_OF_MONTH))
                    .toString());
            changeApplicationInfoForm.set("birthMonth", new Integer(birthDate.get(Calendar.MONTH))
                    .toString());
            changeApplicationInfoForm.set("birthYear", new Integer(birthDate.get(Calendar.YEAR))
                    .toString());
        }

        Calendar identificationDocumentIssueDate = Calendar.getInstance();
        if (infoPerson.getDataEmissaoDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idIssueDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentIssueDate.setTime(infoPerson.getDataEmissaoDocumentoIdentificacao());
            changeApplicationInfoForm.set("idIssueDateDay", new Integer(identificationDocumentIssueDate
                    .get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateMonth", new Integer(
                    identificationDocumentIssueDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateYear", new Integer(identificationDocumentIssueDate
                    .get(Calendar.YEAR)).toString());
        }

        Calendar identificationDocumentExpirationDate = Calendar.getInstance();
        if (infoPerson.getDataValidadeDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idExpirationDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentExpirationDate.setTime(infoPerson
                    .getDataValidadeDocumentoIdentificacao());
            changeApplicationInfoForm.set("idExpirationDateDay", new Integer(
                    identificationDocumentExpirationDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateMonth", new Integer(
                    identificationDocumentExpirationDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateYear", new Integer(
                    identificationDocumentExpirationDate.get(Calendar.YEAR)).toString());
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
        changeApplicationInfoForm.set("majorDegreeSchool", infoMasterDegreeCandidate
                .getMajorDegreeSchool());
        changeApplicationInfoForm.set("majorDegreeYear", infoMasterDegreeCandidate.getMajorDegreeYear());
        changeApplicationInfoForm.set("specializationArea", infoMasterDegreeCandidate
                .getSpecializationArea());
        changeApplicationInfoForm.set("average", infoMasterDegreeCandidate.getAverage().toString());

        if (infoPerson.getSexo() != null)
            changeApplicationInfoForm.set("sex", infoPerson.getSexo().toString());
        if (infoPerson.getEstadoCivil() != null)
            changeApplicationInfoForm.set("maritalStatus", infoPerson.getEstadoCivil().toString());
    }

}