/*
 * Created on 14/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice;

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

import DataBeans.InfoCandidateSituation;
import DataBeans.InfoCountry;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.EstadoCivil;
import Util.Sexo;
import Util.SituationName;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class ListCandidatesDispatchAction extends DispatchAction {

	public ActionForward prepareChoose(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {

			DynaActionForm listCandidatesForm = (DynaActionForm) form;

			String action = request.getParameter("action");
			
			listCandidatesForm.set("degree", null);			
			listCandidatesForm.set("specialization", null);			
			listCandidatesForm.set("candidateSituation", null);			
			listCandidatesForm.set("candidateNumber", null);			
			
			if (action.equals("visualize")) {
				session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION);
				session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION, "label.action.visualize");
			}
			else if (action.equals("edit")) {
				session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION);
				session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION, "label.action.edit");
											
		    }
			GestorServicos serviceManager = GestorServicos.manager();
			
			
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Create the Degree Type List
			ArrayList specializations = Specialization.toArrayList();
			session.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
			
			// Get the Degree List
			
			ArrayList degreeList = null; 			
			try {
				degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", null);
			} catch (Exception e) {
				throw new Exception(e);
			}

			session.setAttribute(SessionConstants.DEGREE_LIST, degreeList);
						
			// Create the Candidate Situation List
			session.setAttribute(SessionConstants.CANDIDATE_SITUATION_LIST, SituationName.toArrayList());  
			
			return mapping.findForward("PrepareReady");
		  } else
			throw new Exception();   

	}
		

	public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm listCandidatesForm = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			String degreeTypeTemp = (String) listCandidatesForm.get("specialization");
			String degreeName = (String) listCandidatesForm.get("degree");
			String candidateSituationTemp = (String) listCandidatesForm.get("candidateSituation");
			String candidateNumberTemp = (String) listCandidatesForm.get("candidateNumber");
			
			Integer candidateNumber = null;
			Specialization specialization = null;
			SituationName situationName = null;
			
			if (degreeName.length() == 0)
				degreeName = null;
			if (candidateNumberTemp.length() != 0)
				candidateNumber = Integer.valueOf(candidateNumberTemp);
			if (degreeTypeTemp != null && degreeTypeTemp.length() != 0)
				specialization = new Specialization(degreeTypeTemp);
			if (candidateSituationTemp != null && candidateSituationTemp.length() != 0)
				situationName = new SituationName(candidateSituationTemp);

			Object args[] = { degreeName, specialization, situationName, candidateNumber };
	  		List result = null;
	  		
	  		try {
				result = (List) serviceManager.executar(userView, "ReadCandidateList", args);
			} catch (Exception e) {
				throw new Exception(e);
			}

			if (result.size() == 1) {
				InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) result.get(0);
				session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);
				session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
				return mapping.findForward("ActionReady");
			}
		  // Create find query String
		  String query = new String();
		  if (degreeName == null && specialization == null && situationName == null && candidateNumber == null)
		  	query = "  - Todos os criterios";
		  else if (degreeName != null) query += "\n  - Curso";
		  else if (specialization != null) query += "\n  - Tipo de Especialização: " + specialization.toString();
		  else if (situationName != null) query += "\n  - Situação do Candidato: " + situationName.toString();
		  else if (candidateNumber != null) query += "\n  - Número de Candidato: " + candidateNumber;
		  
		  session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST);
		  session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_QUERY);
		  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST, result);
		  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_QUERY, query);
		  
		  return mapping.findForward("ChooseCandidate");
		} else
		  throw new Exception();   
	  }
	  
	public ActionForward chooseCandidate(ActionMapping mapping, ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm listCandidatesForm = (DynaActionForm) form;


			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			List candidateList = (List) session.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST);
			

			Integer choosenCandidatePosition = Integer.valueOf(request.getParameter("candidatePosition"));
			
			
			// Put the selected Candidate in Session
			InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) candidateList.get(choosenCandidatePosition.intValue());
		
			session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
			return mapping.findForward("ActionReady");
			
		} else
	  		throw new Exception();  
	}
	  


	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm editCandidateForm = (DynaActionForm) form;

			GestorServicos serviceManager = GestorServicos.manager();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) session.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE); 
			
			// Fill in The Form
			
			InfoPerson infoPerson = infoMasterDegreeCandidate.getInfoPerson();
			
			editCandidateForm.set("identificationDocumentNumber", infoPerson.getNumeroDocumentoIdentificacao());
			editCandidateForm.set("identificationDocumentType", infoPerson.getTipoDocumentoIdentificacao().toString());
			editCandidateForm.set("identificationDocumentIssuePlace", infoPerson.getLocalEmissaoDocumentoIdentificacao());
			editCandidateForm.set("name", infoPerson.getNome());

			Calendar birthDate = Calendar.getInstance();
			if (infoPerson.getNascimento() == null) {
				editCandidateForm.set("birthDay", Data.OPTION_DEFAULT.toString());
				editCandidateForm.set("birthMonth", Data.OPTION_DEFAULT.toString());
				editCandidateForm.set( "birthYear", Data.OPTION_DEFAULT.toString());
			} else {
				birthDate.setTime(infoPerson.getNascimento());
				editCandidateForm.set("birthDay", new Integer(birthDate.get(Calendar.DAY_OF_MONTH)).toString());
				editCandidateForm.set("birthMonth", new Integer(birthDate.get(Calendar.MONTH)).toString());
				editCandidateForm.set("birthYear", new Integer(birthDate.get(Calendar.YEAR)).toString());
			}

			Calendar identificationDocumentIssueDate = Calendar.getInstance();
			if (infoPerson.getDataEmissaoDocumentoIdentificacao() == null) {
				editCandidateForm.set("idIssueDateDay", Data.OPTION_DEFAULT.toString());
				editCandidateForm.set("idIssueDateMonth", Data.OPTION_DEFAULT.toString());
				editCandidateForm.set("idIssueDateYear", Data.OPTION_DEFAULT.toString());
			} else {
				identificationDocumentIssueDate.setTime(infoPerson.getDataEmissaoDocumentoIdentificacao());
				editCandidateForm.set("idIssueDateDay", new Integer(identificationDocumentIssueDate.get(Calendar.DAY_OF_MONTH)).toString());
				editCandidateForm.set("idIssueDateMonth", new Integer(identificationDocumentIssueDate.get(Calendar.MONTH)).toString());
				editCandidateForm.set("idIssueDateYear", new Integer(identificationDocumentIssueDate.get(Calendar.YEAR)).toString());
			}

			Calendar identificationDocumentExpirationDate = Calendar.getInstance();
			if (infoPerson.getDataValidadeDocumentoIdentificacao() == null) {
				editCandidateForm.set("idExpirationDateDay", Data.OPTION_DEFAULT.toString());
				editCandidateForm.set("idExpirationDateMonth", Data.OPTION_DEFAULT.toString());
				editCandidateForm.set("idExpirationDateYear", Data.OPTION_DEFAULT.toString());
			} else {
				identificationDocumentExpirationDate.setTime(infoPerson.getDataValidadeDocumentoIdentificacao());
				editCandidateForm.set("idExpirationDateDay", new Integer(identificationDocumentExpirationDate.get(Calendar.DAY_OF_MONTH)).toString());
				editCandidateForm.set("idExpirationDateMonth", new Integer(identificationDocumentExpirationDate.get(Calendar.MONTH)).toString());
				editCandidateForm.set("idExpirationDateYear",new Integer(identificationDocumentExpirationDate.get(Calendar.YEAR)).toString());
			}

			editCandidateForm.set("fatherName", infoPerson.getNomePai());
			editCandidateForm.set("motherName", infoPerson.getNomeMae());
			editCandidateForm.set("nationality", infoPerson.getInfoPais().getName());
			editCandidateForm.set("birthPlaceParish", infoPerson.getFreguesiaNaturalidade());
			editCandidateForm.set("birthPlaceMunicipality", infoPerson.getConcelhoNaturalidade());
			editCandidateForm.set("birthPlaceDistrict", infoPerson.getDistritoNaturalidade());
			editCandidateForm.set("address", infoPerson.getMorada());
			editCandidateForm.set("place", infoPerson.getLocalidade());
			editCandidateForm.set("postCode", infoPerson.getCodigoPostal());
			editCandidateForm.set("addressParish",infoPerson.getFreguesiaMorada());
			editCandidateForm.set("addressMunicipality",infoPerson.getConcelhoMorada());
			editCandidateForm.set("addressDistrict",infoPerson.getDistritoMorada());
			editCandidateForm.set("telephone", infoPerson.getTelefone());
			editCandidateForm.set("mobilePhone", infoPerson.getTelemovel());
			editCandidateForm.set("email", infoPerson.getEmail());
			editCandidateForm.set("webSite", infoPerson.getEnderecoWeb());
			editCandidateForm.set("contributorNumber",infoPerson.getNumContribuinte());
			editCandidateForm.set("occupation", infoPerson.getProfissao());
			editCandidateForm.set("username", infoPerson.getUsername());
			editCandidateForm.set("areaOfAreaCode",infoPerson.getLocalidadeCodigoPostal());

			if (infoPerson.getSexo() != null)
				editCandidateForm.set("sex", infoPerson.getSexo().toString());
			if (infoPerson.getEstadoCivil() != null)
				editCandidateForm.set("maritalStatus",infoPerson.getEstadoCivil().toString());

			// Get List of available Countries
			Object result = null;
			result = serviceManager.executar(userView, "ReadAllCountries", null);
			ArrayList country = (ArrayList) result;

			// Build List of Countries for the Form
			Iterator iterador = country.iterator();

			ArrayList nationalityList = new ArrayList();
			while (iterador.hasNext()) {
				InfoCountry countryTemp = (InfoCountry) iterador.next();
				nationalityList.add(
					new LabelValueBean(countryTemp.getNationality(),countryTemp.getNationality()));
			}
		
			session.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);
			session.setAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY, new EstadoCivil().toArrayList());
			session.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY, TipoDocumentoIdentificacao.toArrayList());
			session.setAttribute(SessionConstants.SEX_LIST_KEY, new Sexo().toArrayList());
			session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
			session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
			session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());

			session.setAttribute(SessionConstants.EXPIRATION_YEARS_KEY, Data.getExpirationYears());

			session.setAttribute(SessionConstants.CANDIDATE_SITUATION_LIST, SituationName.toArrayList());

			editCandidateForm.set("average", infoMasterDegreeCandidate.getAverage().toString());
			editCandidateForm.set("majorDegree", infoMasterDegreeCandidate.getMajorDegree());
			editCandidateForm.set("majorDegreeSchool", infoMasterDegreeCandidate.getMajorDegreeSchool());
			editCandidateForm.set("majorDegreeYear", infoMasterDegreeCandidate.getMajorDegreeYear());
		
			return mapping.findForward("PrepareReady");
			
		} else
			throw new Exception();  
	}
	  

	public ActionForward change(ActionMapping mapping, ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm editCandidateForm = (DynaActionForm) form;

			GestorServicos serviceManager = GestorServicos.manager();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			InfoMasterDegreeCandidate infoMasterDegreeCandidateInSession = (InfoMasterDegreeCandidate) session.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE); 


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

			

			// FIXME: Check All if fields are empty 

			Calendar birthDate = Calendar.getInstance();
			Calendar idDocumentIssueDate = Calendar.getInstance();
			Calendar idDocumentExpirationDate = Calendar.getInstance();
		
			birthDate.set(new Integer(((String) editCandidateForm.get("birthYear"))).intValue(),
				new Integer(((String) editCandidateForm.get("birthMonth"))).intValue(),
				new Integer(((String) editCandidateForm.get("birthDay"))).intValue());
		
			idDocumentIssueDate.set(new Integer(((String) editCandidateForm.get("idIssueDateYear"))).intValue(),
				new Integer(((String) editCandidateForm.get("idIssueDateMonth"))).intValue(),
				new Integer(((String) editCandidateForm.get("idIssueDateDay"))).intValue());
		
			idDocumentExpirationDate.set(new Integer(((String) editCandidateForm.get("idExpirationDateYear"))).intValue(),
				new Integer(((String) editCandidateForm.get("idExpirationDateMonth"))).intValue(),
				new Integer(((String) editCandidateForm.get("idExpirationDateDay"))).intValue());
		
			InfoCountry nationality = new InfoCountry();
			nationality.setNationality((String) editCandidateForm.get("nationality"));
		
			InfoPerson infoPerson = new InfoPerson();
		
			infoPerson.setDataEmissaoDocumentoIdentificacao(idDocumentIssueDate.getTime());
			infoPerson.setDataValidadeDocumentoIdentificacao(idDocumentExpirationDate.getTime());
			infoPerson.setNascimento(birthDate.getTime());
		
			infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao((String) editCandidateForm.get("identificationDocumentType")));
			infoPerson.setNumeroDocumentoIdentificacao((String) editCandidateForm.get("identificationDocumentNumber"));
			infoPerson.setLocalEmissaoDocumentoIdentificacao((String) editCandidateForm.get("identificationDocumentIssuePlace"));
			infoPerson.setNome((String) editCandidateForm.get("name"));
			infoPerson.setSexo(new Sexo((String) editCandidateForm.get("sex")));
			infoPerson.setEstadoCivil(new EstadoCivil((String) editCandidateForm.get("maritalStatus")));
			infoPerson.setInfoPais(nationality);
			infoPerson.setNomePai((String) editCandidateForm.get("fatherName"));
			infoPerson.setNomeMae((String) editCandidateForm.get("motherName"));
			infoPerson.setFreguesiaNaturalidade((String) editCandidateForm.get("birthPlaceParish"));
			infoPerson.setConcelhoNaturalidade((String) editCandidateForm.get("birthPlaceMunicipality"));
			infoPerson.setDistritoNaturalidade((String) editCandidateForm.get("birthPlaceDistrict"));
			infoPerson.setMorada((String) editCandidateForm.get("address"));
			infoPerson.setLocalidade((String) editCandidateForm.get("place"));
			infoPerson.setCodigoPostal((String) editCandidateForm.get("postCode"));
			infoPerson.setFreguesiaMorada((String) editCandidateForm.get("addressParish"));
			infoPerson.setConcelhoMorada((String) editCandidateForm.get("addressMunicipality"));
			infoPerson.setDistritoMorada((String) editCandidateForm.get("addressDistrict"));
			infoPerson.setTelefone((String) editCandidateForm.get("telephone"));
			infoPerson.setTelemovel((String) editCandidateForm.get("mobilePhone"));
			infoPerson.setEmail((String) editCandidateForm.get("email"));
			infoPerson.setEnderecoWeb((String) editCandidateForm.get("webSite"));
			infoPerson.setNumContribuinte((String) editCandidateForm.get("contributorNumber"));
			infoPerson.setProfissao((String) editCandidateForm.get("occupation"));
			infoPerson.setUsername((String) editCandidateForm.get("username"));
			infoPerson.setLocalidadeCodigoPostal((String) editCandidateForm.get("areaOfAreaCode"));

			InfoMasterDegreeCandidate newCandidate = new InfoMasterDegreeCandidate();
			newCandidate.setInfoPerson(infoPerson);
			
			String averageString = (String) editCandidateForm.get("average");
			if ((averageString != null) && (averageString.length() != 0))
				newCandidate.setAverage(new Double(averageString));
			else newCandidate.setAverage(null);
			
			String situation = (String) editCandidateForm.get("situation");
			String situationRemarks = (String) editCandidateForm.get("situationRemarks");
			InfoCandidateSituation infoCandidateSituation = new InfoCandidateSituation();
			infoCandidateSituation.setRemarks(situationRemarks);
			infoCandidateSituation.setSituation(situation);
			infoMasterDegreeCandidateInSession.setInfoCandidateSituation(infoCandidateSituation);

			Object args[] = {infoMasterDegreeCandidateInSession, newCandidate};
			InfoMasterDegreeCandidate infoMasterDegreeCandidateChanged = null;
			try {
				infoMasterDegreeCandidateChanged = (InfoMasterDegreeCandidate) serviceManager.executar(userView, "ChangeCandidate", args);
			} catch(FenixServiceException e){
				throw new FenixActionException(e);
			}
			
			session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);
			session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidateChanged);
			return mapping.findForward("ChangeSuccess");
			
		} else
			throw new Exception();  
	}
	  
}
