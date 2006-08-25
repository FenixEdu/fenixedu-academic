package net.sourceforge.fenixedu.presentationTier.Action.student.schoolRegistration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration.InfoResidenceCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidPasswordActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class SchoolRegistrationAction extends TransactionalDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("changePassword");
    }

    public ActionForward visualizeFirstTimeStudentPersonalInfoAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        InfoPerson infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                "ReadPersonByUsername", new Object[] { userView.getUtilizador() });

        DynaActionForm registrationForm = (DynaActionForm) form;
        String oldPassword = (String) registrationForm.get("oldPassword");
        String newPassword = (String) registrationForm.get("newPassword");

        try {
            validatePassword(infoPerson, oldPassword, newPassword);
        } catch (InvalidPasswordServiceException e) {
            throw new InvalidPasswordActionException(e);
        }

        registrationForm.set("name", infoPerson.getNome());
        registrationForm.set("primaryAreaCode", infoPerson.getCodigoPostal());
        registrationForm.set("areaOfAreaCode", infoPerson.getLocalidadeCodigoPostal());
        registrationForm.set("sex", infoPerson.getSexo().toString());
        registrationForm.set("emissionLocationOfDocumentID", infoPerson.getLocalEmissaoDocumentoIdentificacao());

        // Get List of available Countries
        List countries = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadAllCountries", null);

        // Build List of Countries for the Form
        Iterator iterador = countries.iterator();

        List nationalityList = new ArrayList();
        while (iterador.hasNext()) {
            InfoCountry countryTemp = (InfoCountry) iterador.next();
            nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp
                    .getNationality()));
        }

        request.setAttribute("nationalityList", nationalityList);
        registrationForm.set("nacionality", "PORTUGUESA NATURAL DO CONTINENTE");
        registrationForm.set("identificationDocumentType", IDDocumentType.IDENTITY_CARD.toString());

        if (request.getParameter("error") != null) {
            request.setAttribute("incompleteData",
                    "Existem campos obrigatórios que não foram correctamente preenchidos");
        }

        request.setAttribute("infoPerson", infoPerson);

        return mapping.findForward("Success");
    }

    public ActionForward preparePersonalDataUseInquiry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, FenixTransactionException {

        return mapping.findForward("viewPersonalDataUseInquiry");
    }

    public ActionForward prepareDislocatedStudentInquiry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, FenixTransactionException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm schoolRegistrationForm = (DynaActionForm) form;

        String answer = (String) schoolRegistrationForm.get("authorizationAnswer");
        if (StringUtils.isEmpty(answer)) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.enrollment.inquiry.mandatory"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        final List infoCountries = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadAllCountries", null);

        List uniqueInfoCountries = new ArrayList();
        Integer defaultCountryID = null;
        for (Iterator iter = infoCountries.iterator(); iter.hasNext();) {
            InfoCountry infoCountry = (InfoCountry) iter.next();
            if (!containsCountry(uniqueInfoCountries, infoCountry)) {
                uniqueInfoCountries.add(infoCountry);
                if (infoCountry.getName().equalsIgnoreCase("PORTUGAL")) {
                    defaultCountryID = infoCountry.getIdInternal();
                }
            }
        }
        Collections.sort(uniqueInfoCountries, new BeanComparator("name"));
        schoolRegistrationForm.set("countryID", defaultCountryID);
        request.setAttribute("infoCountries", uniqueInfoCountries);

        List infoDistricts = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadAllDistricts", null);
        request.setAttribute("infoDistricts", infoDistricts);

        if (schoolRegistrationForm.get("dislocatedCountryID") != null) {
            Integer dislocatedCountryID = (Integer) schoolRegistrationForm.get("dislocatedCountryID");
            if (dislocatedCountryID.equals(defaultCountryID)) {
                request.setAttribute("portugal", "portugal");
            }
        }

        if (schoolRegistrationForm.get("dislocatedAnswer") != null) {
            String dislocatedAnswer = (String) schoolRegistrationForm.get("dislocatedAnswer");
            if (dislocatedAnswer.equalsIgnoreCase("true")) {
                request.setAttribute("dislocated", "dislocated");
            }
        }

        return mapping.findForward("showDislocatedStudentInquiry");
    }

    public ActionForward prepareResidenceCandidacy(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, FenixTransactionException {

        DynaActionForm schoolRegistrationForm = (DynaActionForm) form;
        String answer = (String) schoolRegistrationForm.get("dislocatedAnswer");
        if (answer == null || answer.equals("")) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.enrollment.inquiry.mandatory"));
            saveErrors(request, actionErrors);
            return mapping.findForward("errorValidating");
        }

        Integer dislocatedCountryID = (Integer) schoolRegistrationForm.get("dislocatedCountryID");
        if (answer.equals("true") && dislocatedCountryID == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.enrollment.inquiry.country"));
            saveErrors(request, actionErrors);
            return mapping.findForward("errorValidating");
        }

        return mapping.findForward("prepareResidenceCandidacy");
    }

    public ActionForward residenceCandidacy(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("residenceCandidacy");
    }

    public ActionForward enrollStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm totalForm = (DynaActionForm) form;

        Integer idInternal = Integer.valueOf((String) totalForm.get("idInternal"));
        String identificationDocumentType = (String) totalForm.get("identificationDocumentType");
        String dayOfEmissionDateOfDocumentId = (String) totalForm.get("dayOfEmissionDateOfDocumentId");
        String monthOfEmissionDateOfDocumentId = (String) totalForm
                .get("monthOfEmissionDateOfDocumentId");
        String yearOfEmissionDateOfDocumentId = (String) totalForm.get("yearOfEmissionDateOfDocumentId");

        String dayOfExpirationDateOfDocumentId = (String) totalForm
                .get("dayOfExpirationDateOfDocumentId");
        String monthOfExpirationDateOfDocumentId = (String) totalForm
                .get("monthOfExpirationDateOfDocumentId");
        String yearOfExpirationDateOfDocumentId = (String) totalForm
                .get("yearOfExpirationDateOfDocumentId");
        String emissionLocationOfDocumentID = (String) totalForm.get("emissionLocationOfDocumentID");
        String name = (String) totalForm.get("name");
        String gender = (String) totalForm.get("sex");
        String nameOfFather = (String) totalForm.get("nameOfFather");
        String nameOfMother = (String) totalForm.get("nameOfMother");
        String nacionality = (String) totalForm.get("nacionality");
        String parishOfBirth = (String) totalForm.get("parishOfBirth");
        String districtSubvisionOfBirth = (String) totalForm.get("districtSubvisionOfBirth");
        String districtOfBirth = (String) totalForm.get("districtOfBirth");
        String address = (String) totalForm.get("address");
        String area = (String) totalForm.get("area");
        String primaryAreaCode = (String) totalForm.get("primaryAreaCode");
        String secondaryAreaCode = (String) totalForm.get("secondaryAreaCode");
        if (secondaryAreaCode == null || secondaryAreaCode.equals("")) {
            secondaryAreaCode = new String("000");
        }
        String areaOfAreaCode = (String) totalForm.get("areaOfAreaCode");
        String parishOfResidence = (String) totalForm.get("parishOfResidence");
        String districtSubdivisionOfResidence = (String) totalForm.get("districtSubdivisionOfResidence");
        String districtOfResidence = (String) totalForm.get("districtOfResidence");

        Integer phone = null;
        if (!((String) totalForm.get("phone")).equals("")) {
            phone = new Integer((String) totalForm.get("phone"));
        }
        Integer mobile = null;
        if (!((String) totalForm.get("mobile")).equals("")) {
            mobile = new Integer((String) totalForm.get("mobile"));
        }
        String email = (String) totalForm.get("email");
        Boolean availableEmail = (Boolean) totalForm.get("availableEmail");
        String webAddress = (String) totalForm.get("webAddress");
        Boolean availableWebAdress = (Boolean) totalForm.get("availableWebAdress");
        String contributorNumber = (String) totalForm.get("contributorNumber");
        String occupation = (String) totalForm.get("occupation");
        String newPassword = (String) totalForm.get("newPassword");
        String maritalStatus = (String) totalForm.get("maritalStatus");

        Boolean residenceCandidate = getCheckBoxValue((String) totalForm.get("residenceCandidate"));
        String observations = (String) totalForm.get("observations");
        Boolean availablePhoto = getCheckBoxValue((String) totalForm.get("availablePhoto"));

        Date EmissionDateOfDocumentId = Data.convertStringDate(dayOfEmissionDateOfDocumentId + "-"
                + monthOfEmissionDateOfDocumentId + "-" + yearOfEmissionDateOfDocumentId, "-");
        Date ExpirationDateOfDocumentId = Data.convertStringDate(dayOfExpirationDateOfDocumentId + "-"
                + monthOfExpirationDateOfDocumentId + "-" + yearOfExpirationDateOfDocumentId, "-");
        
        InfoPersonEditor infoPerson = new InfoPersonEditor();
        InfoCountry infoCountry = new InfoCountry();
        infoCountry.setNationality(nacionality);

        infoPerson.setIdInternal(idInternal);
        infoPerson.setTipoDocumentoIdentificacao(IDDocumentType.valueOf(identificationDocumentType));
        infoPerson.setDataEmissaoDocumentoIdentificacao(EmissionDateOfDocumentId);
        infoPerson.setDataValidadeDocumentoIdentificacao(ExpirationDateOfDocumentId);
        infoPerson.setNome(name);
        infoPerson.setNomePai(nameOfFather);
        infoPerson.setNomeMae(nameOfMother);
        infoPerson.setInfoPais(infoCountry);
        infoPerson.setFreguesiaNaturalidade(parishOfBirth);
        infoPerson.setConcelhoNaturalidade(districtSubvisionOfBirth);
        infoPerson.setDistritoNaturalidade(districtOfBirth);
        infoPerson.setMorada(address);
        infoPerson.setLocalidade(area);
        infoPerson.setCodigoPostal(primaryAreaCode + "-" + secondaryAreaCode);
        infoPerson.setLocalidadeCodigoPostal(areaOfAreaCode);
        infoPerson.setFreguesiaMorada(parishOfResidence);
        infoPerson.setConcelhoMorada(districtSubdivisionOfResidence);
        infoPerson.setDistritoMorada(districtOfResidence);
        infoPerson.setTelefone(phone.toString());
        infoPerson.setSexo(Gender.valueOf(gender));
        infoPerson.setLocalEmissaoDocumentoIdentificacao(emissionLocationOfDocumentID);

        if (mobile != null) {
            infoPerson.setTelemovel(mobile.toString());
        }
        infoPerson.setEmail(email);
        infoPerson.setAvailableEmail(availableEmail);
        infoPerson.setEnderecoWeb(webAddress);
        infoPerson.setAvailableWebSite(availableWebAdress);
        infoPerson.setNumContribuinte(contributorNumber);
        infoPerson.setProfissao(occupation);
        infoPerson.setPassword(newPassword);
        infoPerson.setMaritalStatus(MaritalStatus.valueOf(maritalStatus));
        infoPerson.setAvailablePhoto(availablePhoto);

        InfoResidenceCandidacy infoResidenceCandidacy = new InfoResidenceCandidacy();

        if (observations != null && !observations.equals("")) {
            observations = observations.replaceAll("\r\n", " ");
        }

        infoResidenceCandidacy.setCandidate(residenceCandidate);
        infoResidenceCandidacy.setObservations(observations);

        Object args[] = { userView, infoPerson, infoResidenceCandidacy };
        Boolean result = (Boolean) ServiceUtils.executeService(userView, "SchoolRegistration", args);

        if (result.booleanValue() == false) {
            request.setAttribute("studentRegistered", "Os seus dados não foram alterados "
                    + "devido à sua matricula já ter sido efectuada anteriormente.");
        } else {

            // if it's to register the student, write the dislocated and
            // personal data use inquiries information
            Integer studentID = Integer.valueOf(userView.getUtilizador().substring(1));
            Object[] args1 = { studentID, DegreeType.DEGREE };
            InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentByNumberAndDegreeType", args1);

            Integer countryID = (Integer) totalForm.get("countryID");
            String dislocatedStudent = (String) totalForm.get("dislocatedAnswer");
            Integer districtID = null;
            Integer dislocatedCountryID = null;
            if (dislocatedStudent != null && dislocatedStudent.equalsIgnoreCase("true")) {
                dislocatedCountryID = (Integer) totalForm.get("dislocatedCountryID");
                districtID = (Integer) totalForm.get("districtID");
            }

            Object[] args2 = { infoStudent.getIdInternal(), countryID, dislocatedCountryID, districtID };
            ServiceManagerServiceFactory.executeService(userView, "WriteDislocatedStudentAnswer", args2);

            String answer = (String) totalForm.get("authorizationAnswer");
            Object[] args3 = { infoStudent.getIdInternal(), answer };
            ServiceManagerServiceFactory.executeService(userView,
                    "WriteStudentPersonalDataAuthorizationAnswer", args3);
        }

        return mapping.findForward("viewEnrollments");
    }

    public ActionForward viewStudentEnrollments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Object args[] = { userView };
        List result = (List) ServiceUtils.executeService(userView, "ReadStudentEnrollmentsAndClass",
                args);

        List infoEnrollments = (List) result.get(0);
        request.setAttribute("infoEnrollments", infoEnrollments);
        InfoClass infoClass = (InfoClass) result.get(1);
        request.setAttribute("infoClass", infoClass);
        String degreeName = (String) result.get(2);
        request.setAttribute("degreeName", degreeName);

        return mapping.findForward("Success");
    }

    public ActionForward declarations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("degreeName", request.getParameter("degreeName"));
        return mapping.findForward("declarations");
    }

    private Boolean getCheckBoxValue(String value) {

        if (value != null && (value.equals("true") || value.equals("yes") || value.equals("on"))) {
            return new Boolean(true);
        }
        return new Boolean(false);

    }

    public ActionForward printSchedule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = { userView.getPerson().getStudentByUsername() };
        List infoLessons;
        try {
            infoLessons = (List) ServiceUtils.executeService(userView, "ReadStudentTimeTable", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoLessons", infoLessons);
        DynaActionForm schoolRegistrationFrom = (DynaActionForm) form;
        String name = (String) schoolRegistrationFrom.get("name");
        request.setAttribute("name",name);
        request.setAttribute("studentNumber",userView.getUtilizador().substring(1));
        request.setAttribute("degreeName", request.getParameter("degreeName"));

        return mapping.findForward("sucess");
    }

    public ActionForward printDeclaration(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        InfoPerson infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                "ReadPersonByUsername", new Object[] { userView.getUtilizador() });

        Calendar calendar = Calendar.getInstance();

        String studentNumber = infoPerson.getUsername().substring(1);
        String lectiveYear = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.YEAR) + 1);
        List allMonths = Data.getMonths();
        LabelValueBean label = (LabelValueBean) allMonths.get(calendar.get(Calendar.MONTH) + 1);
        String month = label.getLabel();
        String degreeName = request.getParameter("degreeName");

        request.setAttribute("day", calendar.get(Calendar.DAY_OF_MONTH));
        request.setAttribute("month", month);
        request.setAttribute("year", calendar.get(Calendar.YEAR));
        request.setAttribute("degreeName", degreeName.toUpperCase());
        request.setAttribute("lectiveYear", lectiveYear);
        request.setAttribute("infoPerson", infoPerson);
        request.setAttribute("studentNumber", studentNumber);

        return mapping.findForward("sucess");
    }

    public ActionForward logOff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("logoff");
    }

    private void validatePassword(InfoPerson infoPerson, String oldPassword, String newPassword)
            throws InvalidPasswordServiceException {

        if (newPassword == null
                || newPassword.equals("")
                || infoPerson.getNumeroDocumentoIdentificacao().equalsIgnoreCase(newPassword)
                || (infoPerson.getCodigoFiscal() != null && infoPerson.getCodigoFiscal()
                        .equalsIgnoreCase(newPassword))
                || (infoPerson.getNumContribuinte() != null && infoPerson.getNumContribuinte()
                        .equalsIgnoreCase(newPassword))
                || PasswordEncryptor.areEquals(infoPerson.getPassword(), newPassword)) {
            throw new InvalidPasswordServiceException("error.exception.invalid.new.password");
        }
        if (!PasswordEncryptor.areEquals(infoPerson.getPassword(), oldPassword)) {
            throw new InvalidPasswordServiceException("error.exception.invalid.existing.password");
        }
    }

    private boolean containsCountry(List infoCountries, final InfoCountry country) {
        return CollectionUtils.exists(infoCountries, new Predicate() {

            public boolean evaluate(Object arg0) {
                InfoCountry infoCountry = (InfoCountry) arg0;
                return infoCountry.getName().equalsIgnoreCase(country.getName());
            }
        });
    }
}