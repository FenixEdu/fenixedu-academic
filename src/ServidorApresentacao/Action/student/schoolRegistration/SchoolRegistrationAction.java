/*
 * Created on Jul 21, 2004
 *
 */
package ServidorApresentacao.Action.student.schoolRegistration;

import java.util.ArrayList;
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

import DataBeans.InfoClass;
import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.EstadoCivil;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */

public class SchoolRegistrationAction extends TransactionalDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.createToken(request);

        return mapping.findForward("changePassword");
    }

    public ActionForward visualizeFirstTimeStudentPersonalInfoAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.validateToken(request, form, mapping, "error.transaction.schoolRegistration");

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoPerson infoPerson = null;

        Object args[] = {userView};

        infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView, "ReadPersonByUsername", args);

        request.removeAttribute("personalInfo");

        request.setAttribute("personalInfo", infoPerson);
        session.setAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY, new EstadoCivil().toArrayList());

        //		Get List of available Countries
        Object result = null;
        result = ServiceManagerServiceFactory.executeService(userView, "ReadAllCountries", null);
        ArrayList country = (ArrayList) result;

        //			Build List of Countries for the Form
        Iterator iterador = country.iterator();

        ArrayList nationalityList = new ArrayList();
        while (iterador.hasNext()) {
            InfoCountry countryTemp = (InfoCountry) iterador.next();
            nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp.getNationality()));
        }

        session.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);

        return mapping.findForward("Success");
    }

    public ActionForward viewInquiryQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        super.validateToken(request, form, mapping, "error.transaction.schoolRegistration");

        //IUserView userView = SessionUtils.getUserView(request);

        //DynaActionForm inquiryForm = (DynaActionForm) form;
        //HashMap answersMap = (HashMap) inquiryForm.get("answersMap");

        return mapping.findForward("viewQuestions");
    }

    public ActionForward enrollStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	 super.validateToken(request, form, mapping, "error.transaction.schoolRegistrationEnd");
    	
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm totalForm = (DynaActionForm) form;
        HashMap answersMap = (HashMap) totalForm.get("answersMap");

        Integer idInternal = new Integer((String) totalForm.get("idInternal"));
        String dayOfEmissionDateOfDocumentId = (String) totalForm.get("dayOfEmissionDateOfDocumentId");
        String monthOfEmissionDateOfDocumentId = (String) totalForm.get("monthOfEmissionDateOfDocumentId");
        String yearOfEmissionDateOfDocumentId = (String) totalForm.get("yearOfEmissionDateOfDocumentId");
        String dayOfExpirationDateOfDocumentId = (String) totalForm.get("dayOfExpirationDateOfDocumentId");
        String monthOfExpirationDateOfDocumentId = (String) totalForm.get("monthOfExpirationDateOfDocumentId");
        String yearOfExpirationDateOfDocumentId = (String) totalForm.get("yearOfExpirationDateOfDocumentId");
        String nameOfFather = (String) totalForm.get("nameOfFather");
        String nameOfMother = (String) totalForm.get("nameOfMother");
        String nacionality = (String) totalForm.get("nacionality");
        String parishOfBirth = (String) totalForm.get("parishOfBirth");
        //String districtSubvisionOfBirth = (String) totalForm.get("districtSubvisionOfBirth");
        String districtOfBirth = (String) totalForm.get("districtOfBirth");
        String address = (String) totalForm.get("address");
        String area = (String) totalForm.get("area");
        String areaCode = (String) totalForm.get("areaCode");
        String areaOfAreaCode = (String) totalForm.get("areaOfAreaCode");
        String parishOfResidence = (String) totalForm.get("parishOfResidence");
        String districtSubdivisionOfResidence = (String) totalForm.get("districtSubdivisionOfResidence");
        String districtOfResidence = (String) totalForm.get("districtOfResidence");
        Integer phone = new Integer((String) totalForm.get("phone"));
        Integer mobile = new Integer((String) totalForm.get("mobile"));
        String email = (String) totalForm.get("email");
        Boolean availableEmail = (Boolean) totalForm.get("availableEmail");
        String webAddress = (String) totalForm.get("webAddress");
        Boolean availableWebAdress = (Boolean) totalForm.get("availableWebAdress");
        String contributorNumber = (String) totalForm.get("contributorNumber");
        String occupation = (String) totalForm.get("occupation");
        String password = (String) totalForm.get("password");
        String maritalStatus = (String) totalForm.get("maritalStatus");
        
        if (availableEmail == null)
            availableEmail = new Boolean(false);
        if (availableWebAdress == null)
            availableWebAdress = new Boolean(false);
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
        infoPerson.setConcelhoNaturalidade(districtSubdivisionOfResidence);
        infoPerson.setDistritoNaturalidade(districtOfBirth);
        infoPerson.setMorada(address);
        infoPerson.setLocalidade(area);
        infoPerson.setCodigoPostal(areaCode);
        infoPerson.setLocalidadeCodigoPostal(areaOfAreaCode);
        infoPerson.setFreguesiaMorada(parishOfResidence);
        infoPerson.setConcelhoMorada(districtSubdivisionOfResidence);
        infoPerson.setDistritoMorada(districtOfResidence);
        infoPerson.setTelefone(phone.toString());
        infoPerson.setTelemovel(mobile.toString());
        infoPerson.setEmail(email);
        infoPerson.setAvailableEmail(availableEmail);
        infoPerson.setEnderecoWeb(webAddress);
        infoPerson.setAvailableWebSite(availableWebAdress);
        infoPerson.setNumContribuinte(contributorNumber);
        infoPerson.setProfissao(occupation);
        infoPerson.setPassword(password);
        infoPerson.setEstadoCivil(new EstadoCivil(maritalStatus));
        infoPerson.setIdInternal(idInternal);

        Object args[] = { userView, answersMap, infoPerson};
        ServiceUtils.executeService(userView, "SchoolRegistration", args);

        System.out.println("O id da pessoa na Accao é: " + infoPerson.getIdInternal());

        return mapping.findForward("viewEnrollments");
    }

    public ActionForward viewStudentEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    
    	 
    	IUserView userView = SessionUtils.getUserView(request);
    	
    	Object args[] = { userView };
        List result = (List) ServiceUtils.executeService(userView, "ReadStudentEnrollmentsAndClass", args);
    	
        List infoEnrollments = (List) result.get(0);
        request.setAttribute("infoEnrollments",infoEnrollments);
        InfoClass infoClass = (InfoClass) result.get(1);
        request.setAttribute("infoClass",infoClass);
    	
    	return mapping.findForward("Success");
}

}
