/*
 * ChangeCandidateApplicationFormAction.java
 *
 * 
 * Created on 14 de Dezembro de 2002, 12:31
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorApresentacao.Action.person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

public class ChangePersonalInfoDispatchAction extends DispatchAction {

	public ActionForward change(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		DynaActionForm changePersonalInformationForm = (DynaActionForm) form;
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		// Clear the Session
		session.removeAttribute(SessionConstants.NATIONALITY_LIST_KEY);
		session.removeAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY);
		session.removeAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY);
		session.removeAttribute(SessionConstants.SEX_LIST_KEY);
		session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
		session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
		session.removeAttribute(SessionConstants.YEARS_KEY);
		session.removeAttribute(SessionConstants.EXPIRATION_YEARS_KEY);
		session.removeAttribute(SessionConstants.CANDIDATE_SITUATION_LIST);


		// Create Dates

		Calendar birthDate = Calendar.getInstance();
		Calendar idDocumentIssueDate = Calendar.getInstance();
		Calendar idDocumentExpirationDate = Calendar.getInstance();

		InfoPerson infoPerson = new InfoPerson();


		String dayString = (String) changePersonalInformationForm.get("birthDay");
		String monthString = (String) changePersonalInformationForm.get("birthMonth");
		String yearString = (String) changePersonalInformationForm.get("birthYear");


		Integer day = null;
		Integer month = null;
		Integer year = null;
		
		if ((dayString == null) || (monthString == null) || (yearString == null) || 
			(dayString.length() == 0) || (monthString.length() == 0) || (yearString.length() == 0)){
				infoPerson.setNascimento(null);
		} else {
			day = new Integer((String) changePersonalInformationForm.get("birthDay"));
			month = new Integer((String) changePersonalInformationForm.get("birthMonth"));
			year = new Integer((String) changePersonalInformationForm.get("birthYear"));
		
			birthDate.set(year.intValue(),month.intValue(), day.intValue());
			infoPerson.setNascimento(birthDate.getTime());			
		}

		dayString = (String) changePersonalInformationForm.get("idIssueDateDay");
		monthString = (String) changePersonalInformationForm.get("idIssueDateMonth");
		yearString = (String) changePersonalInformationForm.get("idIssueDateYear");

	
		if ((dayString == null) || (monthString == null) || (yearString == null) || 
			(dayString.length() == 0) || (monthString.length() == 0) || (yearString.length() == 0)){
				infoPerson.setDataEmissaoDocumentoIdentificacao(null);
		} else {
			day = new Integer((String) changePersonalInformationForm.get("idIssueDateDay"));
			month = new Integer((String) changePersonalInformationForm.get("idIssueDateMonth"));
			year = new Integer((String) changePersonalInformationForm.get("idIssueDateYear"));
			
			idDocumentIssueDate.set(year.intValue(),month.intValue(), day.intValue());
			infoPerson.setDataEmissaoDocumentoIdentificacao(idDocumentIssueDate.getTime());
		}
		
		dayString = (String) changePersonalInformationForm.get("idExpirationDateDay");
		monthString = (String) changePersonalInformationForm.get("idExpirationDateMonth");
		yearString = (String) changePersonalInformationForm.get("idExpirationDateYear");
			

		if ((dayString == null) || (monthString == null) || (yearString == null) || 
			(dayString.length() == 0) || (monthString.length() == 0) || (yearString.length() == 0)){
				infoPerson.setDataValidadeDocumentoIdentificacao(null);
		} else {
			day = new Integer((String) changePersonalInformationForm.get("idExpirationDateDay"));
			month = new Integer((String) changePersonalInformationForm.get("idExpirationDateMonth"));
			year = new Integer((String) changePersonalInformationForm.get("idExpirationDateYear"));

			idDocumentExpirationDate.set(year.intValue(),month.intValue(), day.intValue());
			infoPerson.setDataValidadeDocumentoIdentificacao(idDocumentExpirationDate.getTime());
		}
			

		InfoCountry nationality = new InfoCountry();
		nationality.setNationality(
			(String) changePersonalInformationForm.get("nationality"));

		Object changeArgs[] = new Object[2];

		infoPerson.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				(String) changePersonalInformationForm.get(
					"identificationDocumentType")));
		infoPerson.setNumeroDocumentoIdentificacao(
			(String) changePersonalInformationForm.get(
				"identificationDocumentNumber"));
		infoPerson.setLocalEmissaoDocumentoIdentificacao(
			(String) changePersonalInformationForm.get(
				"identificationDocumentIssuePlace"));
		infoPerson.setNome((String) changePersonalInformationForm.get("name"));
		
		String aux = (String) changePersonalInformationForm.get("sex"); 
		if ((aux == null) || (aux.length() == 0))
			infoPerson.setSexo(null);
		else 
			infoPerson.setSexo(new Sexo(aux));
			
		aux = (String) changePersonalInformationForm.get("maritalStatus"); 
		if ((aux == null) || (aux.length() == 0))
			infoPerson.setEstadoCivil(null);
		else 
			infoPerson.setEstadoCivil(new EstadoCivil(aux));


		infoPerson.setInfoPais(nationality);
		infoPerson.setNomePai(
			(String) changePersonalInformationForm.get("fatherName"));
		infoPerson.setNomeMae(
			(String) changePersonalInformationForm.get("motherName"));
		infoPerson.setFreguesiaNaturalidade(
			(String) changePersonalInformationForm.get("birthPlaceParish"));
		infoPerson.setConcelhoNaturalidade(
			(String) changePersonalInformationForm.get(
				"birthPlaceMunicipality"));
		infoPerson.setDistritoNaturalidade(
			(String) changePersonalInformationForm.get("birthPlaceDistrict"));
		infoPerson.setMorada(
			(String) changePersonalInformationForm.get("address"));
		infoPerson.setLocalidade(
			(String) changePersonalInformationForm.get("place"));
		infoPerson.setCodigoPostal(
			(String) changePersonalInformationForm.get("postCode"));
		infoPerson.setFreguesiaMorada(
			(String) changePersonalInformationForm.get("addressParish"));
		infoPerson.setConcelhoMorada(
			(String) changePersonalInformationForm.get("addressMunicipality"));
		infoPerson.setDistritoMorada(
			(String) changePersonalInformationForm.get("addressDistrict"));
		infoPerson.setTelefone(
			(String) changePersonalInformationForm.get("telephone"));
		infoPerson.setTelemovel(
			(String) changePersonalInformationForm.get("mobilePhone"));
		infoPerson.setEmail(
			(String) changePersonalInformationForm.get("email"));
		infoPerson.setEnderecoWeb(
			(String) changePersonalInformationForm.get("webSite"));
		infoPerson.setNumContribuinte(
			(String) changePersonalInformationForm.get("contributorNumber"));
		infoPerson.setProfissao(
			(String) changePersonalInformationForm.get("occupation"));
		infoPerson.setUsername(
			(String) changePersonalInformationForm.get("username"));
		infoPerson.setLocalidadeCodigoPostal(
			(String) changePersonalInformationForm.get("areaOfAreaCode"));

		changeArgs[0] = infoPerson;
		changeArgs[1] = userView;

		userView =
			(IUserView) ServiceManagerServiceFactory.executeService(
				userView,
				"ChangePersonalInfo",
				changeArgs);

		session.setAttribute(SessionConstants.U_VIEW, userView);

		return mapping.findForward("Success");

	}

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		

		HttpSession session = request.getSession(false);

		DynaActionForm changePersonalInfoForm = (DynaActionForm) form;

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Object changeArgs[] = new Object[1];
		changeArgs[0] = userView;

		Object result = null;
		try {
			result = ServiceManagerServiceFactory.executeService(userView, "ReadPersonByUsername", changeArgs);
		} catch(FenixServiceException e) {
			throw new FenixActionException(e);
		}

		InfoPerson infoPerson = (InfoPerson) result;

		changePersonalInfoForm.set(
			"identificationDocumentNumber",
			infoPerson.getNumeroDocumentoIdentificacao());
		changePersonalInfoForm.set(
			"identificationDocumentType",
			infoPerson.getTipoDocumentoIdentificacao().toString());
		changePersonalInfoForm.set(
			"identificationDocumentIssuePlace",
			infoPerson.getLocalEmissaoDocumentoIdentificacao());
		changePersonalInfoForm.set("name", infoPerson.getNome());

		Calendar birthDate = Calendar.getInstance();
		if (infoPerson.getNascimento() == null) {
			changePersonalInfoForm.set(
				"birthDay",
				Data.OPTION_DEFAULT);
			changePersonalInfoForm.set(
				"birthMonth",
				Data.OPTION_DEFAULT);
			changePersonalInfoForm.set(
				"birthYear",
				Data.OPTION_DEFAULT);
		} else {
			birthDate.setTime(infoPerson.getNascimento());
			changePersonalInfoForm.set(
				"birthDay",
				new Integer(birthDate.get(Calendar.DAY_OF_MONTH)).toString());
			changePersonalInfoForm.set(
				"birthMonth",
				new Integer(birthDate.get(Calendar.MONTH)).toString());
			changePersonalInfoForm.set(
				"birthYear",
				new Integer(birthDate.get(Calendar.YEAR)).toString());
		}

		Calendar identificationDocumentIssueDate = Calendar.getInstance();
		if (infoPerson.getDataEmissaoDocumentoIdentificacao() == null) {
			changePersonalInfoForm.set(
				"idIssueDateDay",
				Data.OPTION_DEFAULT);
			changePersonalInfoForm.set(
				"idIssueDateMonth",
				Data.OPTION_DEFAULT);
			changePersonalInfoForm.set(
				"idIssueDateYear",
				Data.OPTION_DEFAULT);
		} else {
			identificationDocumentIssueDate.setTime(
				infoPerson.getDataEmissaoDocumentoIdentificacao());
			changePersonalInfoForm.set(
				"idIssueDateDay",
				new Integer(
					identificationDocumentIssueDate.get(Calendar.DAY_OF_MONTH))
					.toString());
			changePersonalInfoForm.set(
				"idIssueDateMonth",
				new Integer(
					identificationDocumentIssueDate.get(Calendar.MONTH))
					.toString());
			changePersonalInfoForm.set(
				"idIssueDateYear",
				new Integer(identificationDocumentIssueDate.get(Calendar.YEAR))
					.toString());
		}

		Calendar identificationDocumentExpirationDate = Calendar.getInstance();
		if (infoPerson.getDataValidadeDocumentoIdentificacao() == null) {
			changePersonalInfoForm.set(
				"idExpirationDateDay",
				Data.OPTION_DEFAULT);
			changePersonalInfoForm.set(
				"idExpirationDateMonth",
				Data.OPTION_DEFAULT);
			changePersonalInfoForm.set(
				"idExpirationDateYear",
				Data.OPTION_DEFAULT);
		} else {
			identificationDocumentExpirationDate.setTime(
				infoPerson.getDataValidadeDocumentoIdentificacao());
			changePersonalInfoForm.set(
				"idExpirationDateDay",
				new Integer(
					identificationDocumentExpirationDate.get(
						Calendar.DAY_OF_MONTH))
					.toString());
			changePersonalInfoForm.set(
				"idExpirationDateMonth",
				new Integer(
					identificationDocumentExpirationDate.get(Calendar.MONTH))
					.toString());
			changePersonalInfoForm.set(
				"idExpirationDateYear",
				new Integer(
					identificationDocumentExpirationDate.get(Calendar.YEAR))
					.toString());
		}

		changePersonalInfoForm.set("fatherName", infoPerson.getNomePai());
		changePersonalInfoForm.set("motherName", infoPerson.getNomeMae());
		
		if (infoPerson.getInfoPais() == null) {
			changePersonalInfoForm.set("nationality", null);
		} else {
			changePersonalInfoForm.set(
				"nationality",
				infoPerson.getInfoPais().getNationality());
		}
		changePersonalInfoForm.set(
			"birthPlaceParish",
			infoPerson.getFreguesiaNaturalidade());
		changePersonalInfoForm.set(
			"birthPlaceMunicipality",
			infoPerson.getConcelhoNaturalidade());
		changePersonalInfoForm.set(
			"birthPlaceDistrict",
			infoPerson.getDistritoNaturalidade());
		changePersonalInfoForm.set("address", infoPerson.getMorada());
		changePersonalInfoForm.set("place", infoPerson.getLocalidade());
		changePersonalInfoForm.set("postCode", infoPerson.getCodigoPostal());
		changePersonalInfoForm.set(
			"addressParish",
			infoPerson.getFreguesiaMorada());
		changePersonalInfoForm.set(
			"addressMunicipality",
			infoPerson.getConcelhoMorada());
		changePersonalInfoForm.set(
			"addressDistrict",
			infoPerson.getDistritoMorada());
		changePersonalInfoForm.set("telephone", infoPerson.getTelefone());
		changePersonalInfoForm.set("mobilePhone", infoPerson.getTelemovel());
		changePersonalInfoForm.set("email", infoPerson.getEmail());
		changePersonalInfoForm.set("webSite", infoPerson.getEnderecoWeb());
		changePersonalInfoForm.set(
			"contributorNumber",
			infoPerson.getNumContribuinte());
		changePersonalInfoForm.set("occupation", infoPerson.getProfissao());
		changePersonalInfoForm.set("username", infoPerson.getUsername());
		changePersonalInfoForm.set(
			"areaOfAreaCode",
			infoPerson.getLocalidadeCodigoPostal());

		if (infoPerson.getSexo() != null)
			changePersonalInfoForm.set("sex", infoPerson.getSexo().toString());
		if (infoPerson.getEstadoCivil() != null)
			changePersonalInfoForm.set("maritalStatus", infoPerson.getEstadoCivil().toString());

		// Get List of available Countries
		result = null;
		result = ServiceManagerServiceFactory.executeService(userView, "ReadAllCountries", null);
		ArrayList country = (ArrayList) result;

		// Build List of Countries for the Form
		Iterator iterador = country.iterator();

		ArrayList nationalityList = new ArrayList();
		while (iterador.hasNext()) {
			InfoCountry countryTemp = (InfoCountry) iterador.next();
			nationalityList.add(
				new LabelValueBean(
					countryTemp.getNationality(),
					countryTemp.getNationality()));
		}
		
		session.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);
		session.setAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY,new EstadoCivil().toArrayList());
		session.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY,TipoDocumentoIdentificacao.toArrayList());
		session.setAttribute(SessionConstants.SEX_LIST_KEY, new Sexo().toArrayList());
		session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
		session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
		session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());

		session.setAttribute(SessionConstants.EXPIRATION_YEARS_KEY, Data.getExpirationYears());

		session.setAttribute(SessionConstants.PERSONAL_INFO_KEY, infoPerson);

		return mapping.findForward("prepareReady");
	}

}
