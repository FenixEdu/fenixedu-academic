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

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;


public class ChangePersonalInfoDispatchAction extends DispatchAction {

  public ActionForward change(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {


	SessionUtils.validSessionVerification(request, mapping);
	HttpSession session = request.getSession(false);
	
	if (session != null) {
	  DynaActionForm changePersonalInformationForm = (DynaActionForm) form;
      IUserView userView = (IUserView) session.getAttribute("UserView");
      GestorServicos gestor = GestorServicos.manager();
 
 	  // Create Dates
 	  
	  Calendar birthDate = Calendar.getInstance();
	  Calendar idDocumentIssueDate = Calendar.getInstance();
	  Calendar idDocumentExpirationDate = Calendar.getInstance();
 	  
 	  birthDate.set(new Integer(((String) changePersonalInformationForm.get("birthYear"))).intValue(), 
					new Integer(((String) changePersonalInformationForm.get("birthMonth"))).intValue(),
					new Integer(((String) changePersonalInformationForm.get("birthDay"))).intValue());
					
	  idDocumentIssueDate.set(new Integer(((String) changePersonalInformationForm.get("idIssueDateYear"))).intValue(), 
							  new Integer(((String) changePersonalInformationForm.get("idIssueDateMonth"))).intValue(),
							  new Integer(((String) changePersonalInformationForm.get("idIssueDateDay"))).intValue());

      idDocumentExpirationDate.set(new Integer(((String) changePersonalInformationForm.get("idExpirationDateYear"))).intValue(), 
						   		   new Integer(((String) changePersonalInformationForm.get("idExpirationDateMonth"))).intValue(),
								   new Integer(((String) changePersonalInformationForm.get("idExpirationDateDay"))).intValue());
					
 	  InfoCountry nationality = new InfoCountry();
 	  nationality.setNationality((String) changePersonalInformationForm.get("nationality"));
 
      Object changeArgs[] = new Object[2];
      InfoPerson infoPerson = new InfoPerson();
      
      infoPerson.setDataEmissaoDocumentoIdentificacao(idDocumentIssueDate.getTime());
      infoPerson.setDataValidadeDocumentoIdentificacao(idDocumentExpirationDate.getTime());
      infoPerson.setNascimento(birthDate.getTime());
      
      infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao((String) changePersonalInformationForm.get("identificationDocumentType")));
      infoPerson.setNumeroDocumentoIdentificacao((String) changePersonalInformationForm.get("identificationDocumentNumber"));
      infoPerson.setLocalEmissaoDocumentoIdentificacao((String) changePersonalInformationForm.get("identificationDocumentIssuePlace"));
	  infoPerson.setNome((String) changePersonalInformationForm.get("name"));
	  infoPerson.setSexo(new Sexo((String) changePersonalInformationForm.get("sex")));
	  infoPerson.setEstadoCivil(new EstadoCivil((String) changePersonalInformationForm.get("maritalStatus")));	  
	  infoPerson.setInfoPais(nationality);
	  infoPerson.setNomePai((String) changePersonalInformationForm.get("fatherName"));
	  infoPerson.setNomeMae((String) changePersonalInformationForm.get("motherName"));
	  infoPerson.setFreguesiaNaturalidade((String) changePersonalInformationForm.get("birthPlaceParish"));
	  infoPerson.setConcelhoNaturalidade((String) changePersonalInformationForm.get("birthPlaceMunicipality"));
	  infoPerson.setDistritoNaturalidade((String) changePersonalInformationForm.get("birthPlaceDistrict"));
	  infoPerson.setMorada((String) changePersonalInformationForm.get("address"));
	  infoPerson.setLocalidade((String) changePersonalInformationForm.get("place"));
	  infoPerson.setCodigoPostal((String) changePersonalInformationForm.get("postCode"));
	  infoPerson.setFreguesiaMorada((String) changePersonalInformationForm.get("addressParish"));
	  infoPerson.setConcelhoMorada((String) changePersonalInformationForm.get("addressMunicipality"));
	  infoPerson.setDistritoMorada((String) changePersonalInformationForm.get("addressDistrict"));
	  infoPerson.setTelefone((String) changePersonalInformationForm.get("telephone"));
	  infoPerson.setTelemovel((String) changePersonalInformationForm.get("mobilePhone"));
	  infoPerson.setEmail((String) changePersonalInformationForm.get("email"));
	  infoPerson.setEnderecoWeb((String) changePersonalInformationForm.get("webSite"));
	  infoPerson.setNumContribuinte((String) changePersonalInformationForm.get("contributorNumber"));
	  infoPerson.setProfissao((String) changePersonalInformationForm.get("occupation"));
	  infoPerson.setUsername((String) changePersonalInformationForm.get("username"));
           
	  changeArgs[0] = infoPerson;
	  changeArgs[1] = userView;

      userView = (IUserView) gestor.executar(userView, "ChangePersonalInfo", changeArgs);

	  session.setAttribute(SessionConstants.U_VIEW, userView);
  
      return mapping.findForward("Success");
    } else
      throw new Exception();  
  }
  
  
  
  public ActionForward prepare(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);
	  	
		if (session != null) {
		  DynaActionForm changePersonalInfoForm = (DynaActionForm) form;

		  IUserView userView = (IUserView) session.getAttribute("UserView");
		  GestorServicos gestor = GestorServicos.manager();
	
		  Object changeArgs[] = new Object[1];
		  changeArgs[0] = userView;
	
		  Object result = null;
	       
		  result = gestor.executar(userView, "ReadPersonByUsername", changeArgs);
		
		  InfoPerson infoPerson = (InfoPerson) result; 

		  changePersonalInfoForm.set("identificationDocumentNumber", infoPerson.getNumeroDocumentoIdentificacao());
		  changePersonalInfoForm.set("identificationDocumentType", infoPerson.getTipoDocumentoIdentificacao().toString());
		  changePersonalInfoForm.set("identificationDocumentIssuePlace", infoPerson.getLocalEmissaoDocumentoIdentificacao());
		  changePersonalInfoForm.set("name", infoPerson.getNome());
		  
		  Calendar birthDate = Calendar.getInstance();
		  if (infoPerson.getNascimento() == null){
			  changePersonalInfoForm.set("birthDay", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("birthMonth", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("birthYear", Data.OPTION_DEFAULT.toString());
		  } else {
			  birthDate.setTime(infoPerson.getNascimento());
			  changePersonalInfoForm.set("birthDay", new Integer(birthDate.get(Calendar.DAY_OF_MONTH)).toString());
			  changePersonalInfoForm.set("birthMonth", new Integer(birthDate.get(Calendar.MONTH)).toString());
			  changePersonalInfoForm.set("birthYear", new Integer(birthDate.get(Calendar.YEAR)).toString());
		  }

		  		  
		  Calendar identificationDocumentIssueDate = Calendar.getInstance();
		  if (infoPerson.getDataEmissaoDocumentoIdentificacao() == null){
			  changePersonalInfoForm.set("idIssueDateDay", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idIssueDateMonth", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idIssueDateYear", Data.OPTION_DEFAULT.toString());
		  } else {
			  identificationDocumentIssueDate.setTime(infoPerson.getDataEmissaoDocumentoIdentificacao());
			  changePersonalInfoForm.set("idIssueDateDay", new Integer(identificationDocumentIssueDate.get(Calendar.DAY_OF_MONTH)).toString());
			  changePersonalInfoForm.set("idIssueDateMonth", new Integer(identificationDocumentIssueDate.get(Calendar.MONTH)).toString());
			  changePersonalInfoForm.set("idIssueDateYear", new Integer(identificationDocumentIssueDate.get(Calendar.YEAR)).toString());
		  }

		  Calendar identificationDocumentExpirationDate = Calendar.getInstance();
		  if (infoPerson.getDataValidadeDocumentoIdentificacao() == null){
			  changePersonalInfoForm.set("idExpirationDateDay", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idExpirationDateMonth", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idExpirationDateYear", Data.OPTION_DEFAULT.toString());
		  } else {
			  identificationDocumentExpirationDate.setTime(infoPerson.getDataValidadeDocumentoIdentificacao());
			  changePersonalInfoForm.set("idExpirationDateDay", new Integer(identificationDocumentExpirationDate.get(Calendar.DAY_OF_MONTH)).toString());
			  changePersonalInfoForm.set("idExpirationDateMonth", new Integer(identificationDocumentExpirationDate.get(Calendar.MONTH)).toString());
			  changePersonalInfoForm.set("idExpirationDateYear", new Integer(identificationDocumentExpirationDate.get(Calendar.YEAR)).toString());
		  }

		  changePersonalInfoForm.set("fatherName", infoPerson.getNomePai()); 
		  changePersonalInfoForm.set("motherName", infoPerson.getNomeMae()); 
		  changePersonalInfoForm.set("nationality", infoPerson.getInfoPais().getName());
		  changePersonalInfoForm.set("birthPlaceParish", infoPerson.getFreguesiaNaturalidade());
		  changePersonalInfoForm.set("birthPlaceMunicipality", infoPerson.getConcelhoNaturalidade()); 
		  changePersonalInfoForm.set("birthPlaceDistrict", infoPerson.getDistritoNaturalidade()); 
		  changePersonalInfoForm.set("address", infoPerson.getMorada());
		  changePersonalInfoForm.set("place", infoPerson.getLocalidade()); 
		  changePersonalInfoForm.set("postCode", infoPerson.getCodigoPostal());
		  changePersonalInfoForm.set("addressParish", infoPerson.getFreguesiaMorada());
		  changePersonalInfoForm.set("addressMunicipality", infoPerson.getConcelhoMorada()); 
		  changePersonalInfoForm.set("addressDistrict", infoPerson.getDistritoMorada()); 
		  changePersonalInfoForm.set("telephone", infoPerson.getTelefone()); 
		  changePersonalInfoForm.set("mobilePhone", infoPerson.getTelemovel()); 
		  changePersonalInfoForm.set("email", infoPerson.getEmail()); 
		  changePersonalInfoForm.set("webSite", infoPerson.getEnderecoWeb());
		  changePersonalInfoForm.set("contributorNumber", infoPerson.getNumContribuinte());
		  changePersonalInfoForm.set("occupation", infoPerson.getProfissao());
		  changePersonalInfoForm.set("username", infoPerson.getUsername());
		  
		  System.out.println(infoPerson);
		  
		  if (infoPerson.getSexo() != null)
		  	changePersonalInfoForm.set("sex", infoPerson.getSexo().toString());
		  if (infoPerson.getEstadoCivil() != null)
			changePersonalInfoForm.set("maritalStatus", infoPerson.getEstadoCivil().toString());

		  // Get List of available Countries
		  result = null;
		  result = gestor.executar(userView, "ReadAllCountries", null);
		  ArrayList country = (ArrayList) result;
		  
		  // Build List of Countries for the Form
		  Iterator iterador = country.iterator();
    	  
		  ArrayList nationalityList = new ArrayList();
		  while (iterador.hasNext()) {
			InfoCountry countryTemp = (InfoCountry) iterador.next();
			nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp.getNationality()));
		  }
		
		  session.setAttribute("nationalityList", nationalityList);
		  session.setAttribute("maritalStatusList", new EstadoCivil().toArrayList());
		  session.setAttribute("identificationDocumentTypeList", TipoDocumentoIdentificacao.toArrayList());
		  session.setAttribute("sexList", new Sexo().toArrayList());   		 
		  session.setAttribute("monthDays", Data.getMonthDays());
		  session.setAttribute("months", Data.getMonths());
		  session.setAttribute("years", Data.getYears());
		  
		  session.setAttribute("expirationYears", Data.getExpirationYears());

		  session.setAttribute("personalInfo", infoPerson);
		  		  
		  return mapping.findForward("prepareReady");
		} else
		  throw new Exception();  
	}

  
}
