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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCountry;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class ChangeCandidateApplicationFormAction extends ServidorApresentacao.Action.FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

	SessionUtils.validSessionVerification(request, mapping);
	HttpSession sessao = request.getSession(false);
	
	if (sessao != null) {
	  DynaActionForm changePersonalInformationForm = (DynaActionForm) form;
      IUserView userView = (IUserView) sessao.getAttribute("UserView");
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
					
 	  InfoCountry country = new InfoCountry();
 	  country.setName((String) changePersonalInformationForm.get("country"));
 	  
 	  InfoCountry nationality = new InfoCountry();
 	  nationality.setNationality((String) changePersonalInformationForm.get("nationality"));
 
 	  InfoExecutionYear applicationYear = new InfoExecutionYear();
	  applicationYear.setYear((String) changePersonalInformationForm.get("applicationYear"));
 
      Object changeArgs[] = new Object[1];
      InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate( 
						   (String) changePersonalInformationForm.get("name"), 
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
						   ((Integer) changePersonalInformationForm.get("candidateNumber")),
						   (new Double(Double.parseDouble((String) changePersonalInformationForm.get("average")))),
						   birthDate.getTime(),
						   idDocumentIssueDate.getTime(),
						   idDocumentExpirationDate.getTime());
           
      	InfoMasterDegreeCandidate sessionInfoMasterDegreeCandidate = (InfoMasterDegreeCandidate) sessao.getAttribute("candidateInformation"); 
		
		infoMasterDegreeCandidate.setInfoExecutionDegree(sessionInfoMasterDegreeCandidate.getInfoExecutionDegree());
      
		changeArgs[0] = infoMasterDegreeCandidate;

        gestor.executar(userView, "ChangeMasterDegreeCandidate", changeArgs);

      return mapping.findForward("Success");
    } else
      throw new Exception();  
  }
  
}
