/*
 * Created on 04/Dec/2003
 */

package ServidorApresentacao.Action.grant.owner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantOwnerAction extends DispatchAction
{

	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantOwnerForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		String personUsername = null;
		Integer idInternal = null;
		InfoGrantOwner infoGrantOwner = new InfoGrantOwner();

		personUsername = request.getParameter("personUsername");
		if (request.getParameter("idInternal") != null)
			idInternal = new Integer(request.getParameter("idInternal"));

		/*
		 * 3 cases: personId and idInternal not null = grant owner exists personId not null and
		 * idInternal null = person exists, but grant owner doesn't personId and idInternal null =
		 * person doesn't exists
		 */
		if (idInternal != null)
		{
			//Read the grant owner
			Object[] args = { idInternal };
			infoGrantOwner =
				(InfoGrantOwner) ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"ReadGrantOwner",
					args);

		} else if (personUsername != null)
		{
			//Read the person
			Object[] args = { personUsername };
			InfoPerson infoPerson = null;
			infoPerson =
				(InfoPerson) ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"ReadPersonByUsername",
					args);
			infoGrantOwner.setPersonInfo(infoPerson);
		}

		/*
		 * Fill the form (grant owner e person information)
		 */
		DynaValidatorForm grantOwnerInformationForm = (DynaValidatorForm) form;
		if (infoGrantOwner.getIdInternal() != null)
		{
			setFormGrantOwnerInformation(grantOwnerInformationForm, infoGrantOwner);
		}
		if (infoGrantOwner.getPersonInfo() != null
			&& infoGrantOwner.getPersonInfo().getIdInternal() != null
			&& infoGrantOwner.getPersonInfo().getIdInternal() != new Integer(0))
		{
			setFormPersonalInformation(grantOwnerInformationForm, infoGrantOwner);
		}

		/*
		 * Tipos de documento de identificação
		 */
		List documentTypeList = TipoDocumentoIdentificacao.toIntegerArrayList();
		request.setAttribute("documentTypeList", documentTypeList);
		/*
		 * Tipos de estado civil
		 */
		List maritalStatusList = EstadoCivil.toIntegerArrayList();
		request.setAttribute("maritalStatusList", maritalStatusList);
		/*
		 * Pais
		 */
		List countryList =
			(List) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"ReadAllCountries",
				null);
		request.setAttribute("countryList", countryList);

		return mapping.findForward("edit-grant-owner-form");
	}

	private void setFormPersonalInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner)
	{
		InfoPerson infoPerson = infoGrantOwner.getPersonInfo();

		if (infoPerson.getNome() != null)
			form.set("name", infoPerson.getNome());
		form.set("idNumber", infoPerson.getNumeroDocumentoIdentificacao());
		form.set("idType", infoPerson.getTipoDocumentoIdentificacao().getTipo());
		if (infoPerson.getLocalEmissaoDocumentoIdentificacao() != null)
			form.set("idLocation", infoPerson.getLocalEmissaoDocumentoIdentificacao());
		if (infoPerson.getDataEmissaoDocumentoIdentificacao() != null)
			form.set("idDate", infoPerson.getDataEmissaoDocumentoIdentificacao().toString());
		if (infoPerson.getDataValidadeDocumentoIdentificacao() != null)
			form.set("idValidDate", infoPerson.getDataValidadeDocumentoIdentificacao().toString());
		if (infoPerson.getNascimento() != null)
			form.set("birthdate", infoPerson.getNascimento().toString());
		if (infoPerson.getNomePai() != null)
			form.set("fatherName", infoPerson.getNomePai());
		if (infoPerson.getNomeMae() != null)
			form.set("motherName", infoPerson.getNomeMae());
		if (infoPerson.getNacionalidade() != null)
			form.set("nationality", infoPerson.getNacionalidade());
		if (infoPerson.getFreguesiaNaturalidade() != null)
			form.set("parishOfBirth", infoPerson.getFreguesiaNaturalidade());
		if (infoPerson.getConcelhoNaturalidade() != null)
			form.set("districtSubBirth", infoPerson.getConcelhoNaturalidade());
		if (infoPerson.getDistritoNaturalidade() != null)
			form.set("districtBirth", infoPerson.getDistritoNaturalidade());
		if (infoPerson.getMorada() != null)
			form.set("address", infoPerson.getMorada());
		if (infoPerson.getLocalidade() != null)
			form.set("area", infoPerson.getLocalidade());
		if (infoPerson.getCodigoPostal() != null)
			form.set("areaCode", infoPerson.getCodigoPostal());
		if (infoPerson.getLocalidadeCodigoPostal() != null)
			form.set("areaOfAreaCode", infoPerson.getLocalidadeCodigoPostal());
		if (infoPerson.getFreguesiaMorada() != null)
			form.set("addressParish", infoPerson.getFreguesiaMorada());
		if (infoPerson.getConcelhoMorada() != null)
			form.set("addressDistrictSub", infoPerson.getConcelhoMorada());
		if (infoPerson.getDistritoMorada() != null)
			form.set("addressDistrict", infoPerson.getDistritoMorada());
		if (infoPerson.getTelefone() != null)
			form.set("phone", infoPerson.getTelefone());
		if (infoPerson.getTelemovel() != null)
			form.set("cellphone", infoPerson.getTelemovel());
		if (infoPerson.getEmail() != null)
			form.set("email", infoPerson.getEmail());
		if (infoPerson.getEnderecoWeb() != null)
			form.set("homepage", infoPerson.getEnderecoWeb());
		if (infoPerson.getNumContribuinte() != null)
			form.set("socialSecurityNumber", infoPerson.getNumContribuinte());
		if (infoPerson.getProfissao() != null)
			form.set("profession", infoPerson.getProfissao());
		if (infoPerson.getCodigoFiscal() != null)
			form.set("fiscalCode", infoPerson.getCodigoFiscal());
		if (infoPerson.getSexo() != null)
			form.set("sex", infoPerson.getSexo().getSexo());
		if (infoPerson.getEstadoCivil() != null)
			form.set("maritalStatus", infoPerson.getEstadoCivil().getEstadoCivil());
		if (infoPerson.getInfoPais() != null)
			form.set("country", infoPerson.getInfoPais().getIdInternal());
		if (infoPerson.getUsername() != null)
			form.set("personUsername", infoPerson.getUsername());
		if (infoPerson.getPassword() != null)
			form.set("password", infoPerson.getPassword());

	}

	private void setFormGrantOwnerInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner)
	{
		if (infoGrantOwner.getDateSendCGD() != null)
			form.set("dateSendCGD", infoGrantOwner.getDateSendCGD().toString());
		if (infoGrantOwner.getCardCopyNumber() != null)
			form.set("cardCopyNumber", infoGrantOwner.getCardCopyNumber().toString());
		if (infoGrantOwner.getIdInternal() != null)
			form.set("idInternal", infoGrantOwner.getIdInternal().toString());
	}

	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		DynaValidatorForm editGrantOwnerForm = (DynaValidatorForm) form;
		InfoGrantOwner infoGrantOwner = null;
		infoGrantOwner = populateInfoFromForm(editGrantOwnerForm);

		Object[] args = { infoGrantOwner };
		IUserView userView = SessionUtils.getUserView(request);
		ServiceUtils.executeService(userView, "CreateGrantOwner", args);

		//Read the grant owner by person
		Object[] args2 = { infoGrantOwner.getPersonInfo().getUsername()};
		System.out.println(
			"##############Username to edit: " + infoGrantOwner.getPersonInfo().getUsername());
		infoGrantOwner =
			(InfoGrantOwner) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"ReadGrantOwnerByPerson",
				args2);

		if (infoGrantOwner != null)
			request.setAttribute("idInternal", infoGrantOwner.getIdInternal());
		else
			System.out.println("###########Nao editou bem!");
		ActionForward actionForward = mapping.findForward("manage-grant-owner");
		return actionForward;
	}

	private InfoGrantOwner populateInfoFromForm(DynaValidatorForm editGrantOwnerForm)
	//throws FenixServiceException
	{
		InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
		InfoPerson infoPerson = new InfoPerson();

		//Format of date in the form
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/*
		 * try { //TODO: tirar estes comentários if (editGrantOwnerForm.get("idDate") != null ||
		 * editGrantOwnerForm.get("idDate").equals("")) infoPerson.setDataEmissaoDocumentoIdentificacao(
		 * sdf.parse((String) editGrantOwnerForm.get("idDate"))); if
		 * (editGrantOwnerForm.get("idValidDate") != null ||
		 * editGrantOwnerForm.get("idValidDate").equals(""))
		 * infoPerson.setDataValidadeDocumentoIdentificacao( sdf.parse((String)
		 * editGrantOwnerForm.get("idValidDate"))); if (editGrantOwnerForm.get("birthDate") != null ||
		 * editGrantOwnerForm.get("birthDate").equals("")) infoPerson.setNascimento(sdf.parse((String)
		 * editGrantOwnerForm.get("birthdate"))); if (editGrantOwnerForm.get("dateSendCGD") != null ||
		 * editGrantOwnerForm.get("dateSendCGD").equals(""))
		 * infoGrantOwner.setDateSendCGD(sdf.parse((String) editGrantOwnerForm.get("dateSendCGD"))); }
		 * catch (ParseException e) { throw new FenixServiceException();
		 */

		infoGrantOwner.setCardCopyNumber(new Integer((String) editGrantOwnerForm.get("cardCopyNumber")));
		infoPerson.setNome((String) editGrantOwnerForm.get("name"));
		infoPerson.setNumeroDocumentoIdentificacao((String) editGrantOwnerForm.get("idNumber"));
		TipoDocumentoIdentificacao tipoDocumentoIdentificacao = new TipoDocumentoIdentificacao();
		tipoDocumentoIdentificacao.setTipo((Integer) editGrantOwnerForm.get("idType"));
		infoPerson.setTipoDocumentoIdentificacao(tipoDocumentoIdentificacao);
		infoPerson.setLocalEmissaoDocumentoIdentificacao((String) editGrantOwnerForm.get("idLocation"));
		infoPerson.setNomePai((String) editGrantOwnerForm.get("fatherName"));
		infoPerson.setNomeMae((String) editGrantOwnerForm.get("motherName"));
		infoPerson.setNacionalidade((String) editGrantOwnerForm.get("nationality"));
		infoPerson.setFreguesiaNaturalidade((String) editGrantOwnerForm.get("parishOfBirth"));
		infoPerson.setConcelhoNaturalidade((String) editGrantOwnerForm.get("districtSubBirth"));
		infoPerson.setDistritoNaturalidade((String) editGrantOwnerForm.get("districtBirth"));
		infoPerson.setMorada((String) editGrantOwnerForm.get("address"));
		infoPerson.setLocalidade((String) editGrantOwnerForm.get("area"));
		infoPerson.setCodigoPostal((String) editGrantOwnerForm.get("areaCode"));
		infoPerson.setLocalidadeCodigoPostal((String) editGrantOwnerForm.get("areaOfAreaCode"));
		infoPerson.setFreguesiaMorada((String) editGrantOwnerForm.get("addressParish"));
		infoPerson.setConcelhoMorada((String) editGrantOwnerForm.get("addressDistrictSub"));
		infoPerson.setDistritoMorada((String) editGrantOwnerForm.get("addressDistrict"));
		infoPerson.setTelefone((String) editGrantOwnerForm.get("phone"));
		infoPerson.setTelemovel((String) editGrantOwnerForm.get("cellphone"));
		infoPerson.setEmail((String) editGrantOwnerForm.get("email"));
		infoPerson.setEnderecoWeb((String) editGrantOwnerForm.get("homepage"));
		infoPerson.setNumContribuinte((String) editGrantOwnerForm.get("socialSecurityNumber"));
		infoPerson.setProfissao((String) editGrantOwnerForm.get("profession"));
		infoPerson.setCodigoFiscal((String) editGrantOwnerForm.get("fiscalCode"));
		Sexo sexo = new Sexo();
		sexo.setSexo((Integer) editGrantOwnerForm.get("sex"));
		infoPerson.setSexo(sexo);
		EstadoCivil estadoCivil = new EstadoCivil();
		estadoCivil.setEstadoCivil((Integer) editGrantOwnerForm.get("maritalStatus"));
		infoPerson.setEstadoCivil(estadoCivil);
		InfoCountry infoCountry = new InfoCountry();
		infoCountry.setIdInternal((Integer) editGrantOwnerForm.get("country"));
		infoPerson.setInfoPais(infoCountry);
		infoPerson.setPassword((String) editGrantOwnerForm.get("password"));
		infoPerson.setUsername((String) editGrantOwnerForm.get("personUsername"));

		infoGrantOwner.setPersonInfo(infoPerson);
		//infoGrantOwner.setIdInternal(new Integer((String)editGrantOwnerForm.get("idInternal")));
		return infoGrantOwner;
	}
}