/*
 * Created on Jul 21, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.schoolRegistration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration.InfoResidenceCandidacy;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidPasswordActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;
import net.sourceforge.fenixedu.util.EstadoCivil;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */

public class SchoolRegistrationAction extends TransactionalDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.createToken(request);

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoPerson infoPerson = null;

        Object args[] = { userView };

        infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                "ReadPersonByUsername", args);

        session.removeAttribute("personalInfo");

        session.setAttribute("personalInfo", infoPerson);
        return mapping.findForward("changePassword");
    }

    public ActionForward visualizeFirstTimeStudentPersonalInfoAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm registrationForm = (DynaActionForm) form;
        String oldPassword = (String) registrationForm.get("oldPassword");
        String newPassword = (String) registrationForm.get("newPassword");

        InfoPerson infoPerson = (InfoPerson) session.getAttribute("personalInfo");

        try {
            validatePassword(infoPerson, oldPassword, newPassword);
        } catch (InvalidPasswordServiceException e) {
            throw new InvalidPasswordActionException(e);
        }

        session.setAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY, new EstadoCivil().toArrayList());
        registrationForm.set("maritalStatus", EstadoCivil.SOLTEIRO_STRING);

        //		Get List of available Countries
        Object result = null;
        result = ServiceManagerServiceFactory.executeService(userView, "ReadAllCountries", null);
        ArrayList country = (ArrayList) result;

        //			Build List of Countries for the Form
        Iterator iterador = country.iterator();

        List nationalityList = new ArrayList();
        while (iterador.hasNext()) {
            InfoCountry countryTemp = (InfoCountry) iterador.next();
            nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp
                    .getNationality()));
        }

        session.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);
        registrationForm.set("nacionality", "PORTUGUESA NATURAL DO CONTINENTE");

        if (request.getParameter("error") != null)
            request.setAttribute("incompleteData",
                    "Existem campos obrigatórios que não foram correctamente preenchidos");

        return mapping.findForward("Success");
    }

    public ActionForward viewInquiryQuestions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        return mapping.findForward("viewQuestions");
    }

    public ActionForward enrollStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm totalForm = (DynaActionForm) form;
        HashMap answersMap = (HashMap) totalForm.get("answersMap");

        Integer idInternal = new Integer((String) totalForm.get("idInternal"));
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
        if (secondaryAreaCode == null || secondaryAreaCode.equals(""))
            secondaryAreaCode = new String("000");
        String areaOfAreaCode = (String) totalForm.get("areaOfAreaCode");
        String parishOfResidence = (String) totalForm.get("parishOfResidence");
        String districtSubdivisionOfResidence = (String) totalForm.get("districtSubdivisionOfResidence");
        String districtOfResidence = (String) totalForm.get("districtOfResidence");

        Integer phone = null;
        if (!((String) totalForm.get("phone")).equals(""))
            phone = new Integer((String) totalForm.get("phone"));

        Integer mobile = null;
        if (!((String) totalForm.get("mobile")).equals(""))
            mobile = new Integer((String) totalForm.get("mobile"));

        String email = (String) totalForm.get("email");
        Boolean availableEmail = (Boolean) totalForm.get("availableEmail");
        String webAddress = (String) totalForm.get("webAddress");
        Boolean availableWebAdress = (Boolean) totalForm.get("availableWebAdress");
        String contributorNumber = (String) totalForm.get("contributorNumber");
        String occupation = (String) totalForm.get("occupation");
        String newPassword = (String) totalForm.get("newPassword");
        String maritalStatus = (String) totalForm.get("maritalStatus");

        Boolean residenceCandidate = getCheckBoxValue((String) totalForm.get("residenceCandidate"));
        Boolean dislocated = new Boolean((String) totalForm.get("dislocated"));
        String observations = (String) totalForm.get("observations");
        Boolean availablePhoto = getCheckBoxValue((String) totalForm.get("availablePhoto"));

        Date EmissionDateOfDocumentId = Data.convertStringDate(dayOfEmissionDateOfDocumentId + "-"
                + monthOfEmissionDateOfDocumentId + "-" + yearOfEmissionDateOfDocumentId, "-");
        Date ExpirationDateOfDocumentId = Data.convertStringDate(dayOfExpirationDateOfDocumentId + "-"
                + monthOfExpirationDateOfDocumentId + "-" + yearOfExpirationDateOfDocumentId, "-");
        InfoPerson infoPerson = new InfoPerson();

        infoPerson.setDataEmissaoDocumentoIdentificacao(EmissionDateOfDocumentId);
        infoPerson.setDataValidadeDocumentoIdentificacao(ExpirationDateOfDocumentId);
        infoPerson.setNomePai(nameOfFather);
        infoPerson.setNomeMae(nameOfMother);
        infoPerson.setNacionalidade(nacionality);
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
        if (phone != null)
            infoPerson.setTelefone(phone.toString());
        if (mobile != null)
            infoPerson.setTelemovel(mobile.toString());
        infoPerson.setEmail(email);
        infoPerson.setAvailableEmail(availableEmail);
        infoPerson.setEnderecoWeb(webAddress);
        infoPerson.setAvailableWebSite(availableWebAdress);
        infoPerson.setNumContribuinte(contributorNumber);
        infoPerson.setProfissao(occupation);
        infoPerson.setPassword(newPassword);
        infoPerson.setEstadoCivil(new EstadoCivil(maritalStatus));
        infoPerson.setIdInternal(idInternal);
        infoPerson.setAvailablePhoto(availablePhoto);

        InfoResidenceCandidacy infoResidenceCandidacy = null;
        if (residenceCandidate.booleanValue()) {
            infoResidenceCandidacy = new InfoResidenceCandidacy();

            if (observations != null && !observations.equals(""))
                observations = observations.replaceAll("\r\n", " ");

            infoResidenceCandidacy.setDislocated(dislocated);
            infoResidenceCandidacy.setObservations(observations);
        }

        Object args[] = { userView, answersMap, infoPerson, infoResidenceCandidacy };
        Boolean result = (Boolean) ServiceUtils.executeService(userView, "SchoolRegistration", args);

        if (result.booleanValue() == false) {
            request.setAttribute("studentRegistered", "Os seus dados não foram alterados "
                    + "devido à sua matricula já ter sido efectuada anteriormente.");
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

    public ActionForward residenceCandidacy(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("residenceCandidacy");
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

    public ActionForward printDeclaration(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        final int columnNumber = 73;
        HttpSession session = request.getSession(false);
        InfoPerson infoPerson = (InfoPerson) session.getAttribute("personalInfo");
        DynaActionForm formBean = (DynaActionForm) form;
        Calendar calendar = Calendar.getInstance();

        String studentNumber = infoPerson.getUsername().substring(1);
        String idNumber = infoPerson.getNumeroDocumentoIdentificacao();
        String parishOfBirth = (String) formBean.get("parishOfBirth");
        String districtOfBirth = (String) formBean.get("districtOfBirth");

        String nameOfFather = (String) formBean.get("nameOfFather");
        String nameOfMother = (String) formBean.get("nameOfMother");
        String degreeName = request.getParameter("degreeName");

        String partOne = "DECLARA, a pedido do interessado, que o aluno Número " + studentNumber + " ";
        String partTwo = infoPerson.getNome().toUpperCase() + " ";
        String partThree = "portador do Bilhete de Identidade " + idNumber + " ";
        String partFour = "natural de " + parishOfBirth.toUpperCase() + ", "
                + districtOfBirth.toUpperCase() + " ";
        String partFive = "filho de " + nameOfFather.toUpperCase() + " ";
        String partSix = "e de " + nameOfMother.toUpperCase() + " ";
        String partSeven = "no ano lectivo " + calendar.get(Calendar.YEAR) + "/"
                + (calendar.get(Calendar.YEAR) + 1) + " ESTÁ INSCRITO no curso de "
                + degreeName.toUpperCase() + " deste instituto.";

        List allMonths = Data.getMonths();
        LabelValueBean label = (LabelValueBean) allMonths.get(calendar.get(Calendar.MONTH) + 1);
        String month = label.getLabel();

        String partEight = "Secretaria dos Serviços Académicos do Instituto Superior Técnico,<br><br>em Lisboa, "
                + calendar.get(Calendar.DAY_OF_MONTH)
                + " de "
                + month
                + " de "
                + calendar.get(Calendar.YEAR);

        String partOne1 = completeLine(partOne, columnNumber);
        String partTwo1 = adjustLine(partTwo, columnNumber, true);
        String partThree1 = completeLine(partThree, columnNumber);
        String partFour1 = adjustLine(partFour, columnNumber, true);
        String partFive1 = completeLine(partFive, columnNumber);
        String partSix1 = completeLine(partSix, columnNumber);
        String partSeven1 = adjustLine(partSeven, columnNumber, false);

        request.setAttribute("partOne1", partOne1);
        request.setAttribute("partTwo1", partTwo1);
        request.setAttribute("partThree1", partThree1);
        request.setAttribute("partFour1", partFour1);
        request.setAttribute("partFive1", partFive1);
        request.setAttribute("partSix1", partSix1);
        request.setAttribute("partSeven1", partSeven1);
        request.setAttribute("partEight1", partEight);

        return mapping.findForward("sucess");
    }

    public ActionForward logOff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

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
            throw new InvalidPasswordServiceException("Invalid New Password!");
        }
        if (!PasswordEncryptor.areEquals(infoPerson.getPassword(), oldPassword)) {
            throw new InvalidPasswordServiceException("Invalid Existing Password!");
        }
    }

    private String completeLine(String line, int columnNumber) {
        StringBuffer completedLineBuffer = new StringBuffer(line);

        int columnCouter = columnNumber;
        if (line.length() > columnCouter)
            columnCouter += columnCouter;

        for (int iter = line.length(); iter < columnCouter; iter++)
            completedLineBuffer.append('-');

        return completedLineBuffer.toString();
    }

    private String adjustLine(String line, int columnNumber, boolean bool) {

        String adjustedLine = new String();
        if (line.length() < columnNumber)
            return completeLine(line, columnNumber);
        String partLine = line.substring(0, columnNumber);
        if (line.charAt(columnNumber + 1) != ' ') {
            int lastSpace = partLine.lastIndexOf(" ", columnNumber);
            adjustedLine = line.substring(0, lastSpace);
            adjustedLine = completeLine(adjustedLine + " ", columnNumber);
            adjustedLine += "<tr><td>";
            if (bool)
                adjustedLine += completeLine(line.substring(lastSpace + 1, line.length()), columnNumber);
            else
                adjustedLine += line.substring(lastSpace + 1, line.length());

            adjustedLine += "</td></tr>";
        } else {
            adjustedLine += line.substring(0, columnNumber + 1);
            adjustedLine += "</td></tr><tr><td>";
            adjustedLine += line.substring(columnNumber + 1, line.length());
        }

        return adjustedLine;
    }

}