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
 
package ServidorApresentacao.Action.masterDegree.candidate;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCountry;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import Util.Data;

public class ChangeCandidateApplicationFormAction extends ServidorApresentacao.Action.FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

    DynaActionForm changePersonalInformationForm = (DynaActionForm) form;

	HttpSession sessao = request.getSession(false);
	
	if (sessao != null) {
      IUserView userView = (IUserView) sessao.getAttribute("UserView");
      GestorServicos gestor = GestorServicos.manager();
 
 	  
 	  // Verify last errors in form
	  if (!validateForm(changePersonalInformationForm, request))
	  	return mapping.getInputForward();
 
 	  
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
					
 	  InfoCountry country = new InfoCountry();
 	  country.setName((String) changePersonalInformationForm.get("country"));
 	  
 	  InfoCountry nationality = new InfoCountry();
 	  nationality.setNationality((String) changePersonalInformationForm.get("nationality"));
 
 	  InfoExecutionYear applicationYear = new InfoExecutionYear();
	  applicationYear.setYear((String) changePersonalInformationForm.get("applicationYear"));
 
      Object changeArgs[] = new Object[1];
      InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate( 
						   (String) changePersonalInformationForm.get("name"), 
						   (String) changePersonalInformationForm.get("password"), 
						   (String) changePersonalInformationForm.get("majorDegree"), 
						   (String) changePersonalInformationForm.get("majorDegreeSchool"), 
						   ((Integer) changePersonalInformationForm.get("majorDegreeYear")), 
						   (String) changePersonalInformationForm.get("fatherName"), 
						   (String) changePersonalInformationForm.get("motherName"), 
						   (String) changePersonalInformationForm.get("birthPlaceParish"), 
						   (String) changePersonalInformationForm.get("birthPlaceMunicipality"), 
						   (String) changePersonalInformationForm.get("birthPlaceDistrict"), 
      					   (String) changePersonalInformationForm.get("identificationDocumentNumber"), 
						   (String) changePersonalInformationForm.get("identificationDocumentIssuePlace"), 
						   (String) changePersonalInformationForm.get("address"), 
						   (String) changePersonalInformationForm.get("place"), 
						   (String) changePersonalInformationForm.get("postCode"), 
						   (String) changePersonalInformationForm.get("addressParish"), 
						   (String) changePersonalInformationForm.get("addressMunicipality"), 
						   (String) changePersonalInformationForm.get("addressDistrict"), 
						   (String) changePersonalInformationForm.get("telephone"), 
						   (String) changePersonalInformationForm.get("mobilePhone"), 
						   (String) changePersonalInformationForm.get("email"), 
						   (String) changePersonalInformationForm.get("webSite"), 
						   (String) changePersonalInformationForm.get("contributorNumber"), 
						   (String) changePersonalInformationForm.get("occupation"), 
						   (String) changePersonalInformationForm.get("sex"),
						   (String) changePersonalInformationForm.get("identificationDocumentType"),
						   (String) changePersonalInformationForm.get("maritalStatus"),
						   country, 
						   nationality, 
						   (String) changePersonalInformationForm.get("specialization"),
						   applicationYear,
						   ((Integer) changePersonalInformationForm.get("candidateNumber")),
						   (new Double(Double.parseDouble((String) changePersonalInformationForm.get("average")))),
						   birthDate.getTime(),
						   idDocumentIssueDate.getTime(),
						   idDocumentExpirationDate.getTime());
           
      	InfoMasterDegreeCandidate sessionInfoMasterDegreeCandidate = (InfoMasterDegreeCandidate) sessao.getAttribute("candidateInformation"); 
		
		infoMasterDegreeCandidate.setInfoDegree(sessionInfoMasterDegreeCandidate.getInfoDegree());
      
		changeArgs[0] = infoMasterDegreeCandidate;

        gestor.executar(userView, "ChangeMasterDegreeCandidate", changeArgs);

      return mapping.findForward("Success");
    } else
      throw new Exception();  
  }

  
  
  private boolean validateForm(DynaActionForm form, HttpServletRequest request){
  	boolean result = true;

	ActionErrors actionErrors = new ActionErrors();

	// Validate the new Password
	String password = (String) form.get("password");
	String reTypePassword = (String) form.get("reTypePassword");
	
	if (!password.equals(reTypePassword)){
		result = false;
		actionErrors.add("diferentePasswords", new ActionError("errors.different.passwords"));
	}
	
	Integer year = new Integer(((String) form.get("birthYear")));
	Integer month = new Integer(((String) form.get("birthMonth")));
	Integer day = new Integer(((String) form.get("birthDay")));
					
  	// Validate the birth Date
  	
  	if (!Data.validDate(day, month, year)){
  		result = false;
		actionErrors.add("invalidBirthDate", new ActionError("errors.invalid.date", "Data de Nascimento"));
  	}
  		
  	// Validate the Identification Document Issue Date  

	year = new Integer(((String) form.get("idIssueDateYear")));
	month = new Integer(((String) form.get("idIssueDateMonth")));
	day = new Integer(((String) form.get("idIssueDateDay")));

	if (!Data.validDate(day, month, year)){
		result = false;
		actionErrors.add("invalidIdIssueDate", new ActionError("errors.invalid.date", "Data de Emissão do Documento de Identificação"));
	}

	
	// Validate the document expiration date 
	year = new Integer(((String) form.get("idExpirationDateYear")));
	month = new Integer(((String) form.get("idExpirationDateMonth")));
	day = new Integer(((String) form.get("idExpirationDateDay")));

	if (!Data.validDate(day, month, year)){
		result = false;
		actionErrors.add("invalidIdExpirationDate", new ActionError("errors.invalid.date", "Data de Validade do Documento de Identificação"));
	}

  	
  	if (!result) 
		saveErrors(request, actionErrors);
  	return result;
  }



}
