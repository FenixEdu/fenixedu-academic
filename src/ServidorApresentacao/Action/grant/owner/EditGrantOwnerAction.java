/*
 * Created on 04/Dec/2003
 */

package ServidorApresentacao.Action.grant.owner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica 
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
        //Get the information to search
		String personUsername = null;
		Integer idInternal = null;
		if (request.getParameter("personUsername") != null)
			personUsername = request.getParameter("personUsername");
		if (request.getParameter("idInternal") != null)
			idInternal = new Integer(request.getParameter("idInternal"));

		/*
		 * 3 cases: personId and idInternal not null = grant owner exists personId not null and
		 * idInternal null = person exists, but grant owner doesn't personId and idInternal null =
		 * person doesn't exists
		 */
        InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
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
			//Read the person (grant owner doesn't exist)
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
			//If Grant Owner exists
			setFormGrantOwnerInformation(grantOwnerInformationForm, infoGrantOwner);
		}
		if (infoGrantOwner.getPersonInfo() != null
			&& infoGrantOwner.getPersonInfo().getIdInternal() != null
			&& !infoGrantOwner.getPersonInfo().getIdInternal().equals(new Integer(0)))
		{
			//If the person exists
			setFormPersonalInformation(grantOwnerInformationForm, infoGrantOwner);
		}

		List documentTypeList = TipoDocumentoIdentificacao.toIntegerArrayList();
		request.setAttribute("documentTypeList", documentTypeList);

        List maritalStatusList = EstadoCivil.toIntegerArrayList();
		request.setAttribute("maritalStatusList", maritalStatusList);
		
		List countryList =
			(List) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"ReadAllCountries",
				null);
		request.setAttribute("countryList", countryList);

		if (infoGrantOwner.getIdInternal() != null)
			request.setAttribute("idInternalGrantOwner", infoGrantOwner.getIdInternal().toString());

		return mapping.findForward("edit-grant-owner-form");
	}

	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		DynaValidatorForm editGrantOwnerForm = (DynaValidatorForm) form;
		InfoGrantOwner infoGrantOwner = populateInfoFromForm(editGrantOwnerForm);

        //Edit Grant Owner
		Object[] args = { infoGrantOwner };
		IUserView userView = SessionUtils.getUserView(request);
		ServiceUtils.executeService(userView, "CreateGrantOwner", args);

		//Read the grant owner by person
		Object[] args2 = { infoGrantOwner.getPersonInfo().getIdInternal()};
		infoGrantOwner =
			(InfoGrantOwner) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"ReadGrantOwnerByPerson",
				args2);

		if (infoGrantOwner != null)
			request.setAttribute("idInternal", infoGrantOwner.getIdInternal());
		else
        {
		    //TODO... excepcao
        }         

		return mapping.findForward("manage-grant-owner");
	}

	/*
	 * Populates form from InfoPerson
	 */
	private void setFormPersonalInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner)
		throws Exception
	{
		InfoPerson infoPerson = infoGrantOwner.getPersonInfo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		form.set("idInternalPerson", infoPerson.getIdInternal().toString());
		form.set("idNumber", infoPerson.getNumeroDocumentoIdentificacao());
		if (infoPerson.getLocalEmissaoDocumentoIdentificacao() != null)
			form.set("idLocation", infoPerson.getLocalEmissaoDocumentoIdentificacao());
		if (infoPerson.getDataEmissaoDocumentoIdentificacao() != null)
			form.set("idDate", infoPerson.getDataEmissaoDocumentoIdentificacao().toString());
		if (infoPerson.getDataValidadeDocumentoIdentificacao() != null) 
			form.set("idValidDate", sdf.format(infoPerson.getDataValidadeDocumentoIdentificacao()));
		if (infoPerson.getNome() != null)
			form.set("name", infoPerson.getNome());
		if (infoPerson.getNascimento() != null)
			form.set("birthdate", sdf.format(infoPerson.getNascimento()));
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
		if (infoPerson.getUsername() != null)
			form.set("personUsername", infoPerson.getUsername());
		if (infoPerson.getPassword() != null)
			form.set("password", infoPerson.getPassword());
		if (infoPerson.getCodigoFiscal() != null)
			form.set("fiscalCode", infoPerson.getCodigoFiscal());
		form.set("idType", infoPerson.getTipoDocumentoIdentificacao().getTipo());
		if (infoPerson.getSexo() != null)
			form.set("sex", infoPerson.getSexo().getSexo());
		if (infoPerson.getEstadoCivil() != null)
			form.set("maritalStatus", infoPerson.getEstadoCivil().getEstadoCivil());
		if (infoPerson.getInfoPais() != null)
			form.set("country", infoPerson.getInfoPais().getIdInternal());
	}
	/*
	 * Populates form from InfoGrantOwner
	 */
	private void setFormGrantOwnerInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner)
		throws Exception
	{
		form.set("idInternal", infoGrantOwner.getIdInternal().toString());
		form.set("grantOwnerNumber", infoGrantOwner.getGrantOwnerNumber().toString());
		if (infoGrantOwner.getCardCopyNumber() != null)
			form.set("cardCopyNumber", infoGrantOwner.getCardCopyNumber().toString());
		if (infoGrantOwner.getDateSendCGD() != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			form.set("dateSendCGD", sdf.format(infoGrantOwner.getDateSendCGD()));
		}
	}

	/*
	 * Populate infoGrantOwner from form
	 */
	private InfoGrantOwner populateInfoFromForm(DynaValidatorForm editGrantOwnerForm)
		throws FenixServiceException
	{
		InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
		InfoPerson infoPerson = new InfoPerson();

		//Format of date in the form
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try
		{
			if (editGrantOwnerForm.get("idDate") != null
				&& !editGrantOwnerForm.get("idDate").equals(""))
				infoPerson.setDataEmissaoDocumentoIdentificacao(
					sdf.parse((String) editGrantOwnerForm.get("idDate")));
			if (editGrantOwnerForm.get("idValidDate") != null
				&& !editGrantOwnerForm.get("idValidDate").equals(""))
				infoPerson.setDataValidadeDocumentoIdentificacao(
					sdf.parse((String) editGrantOwnerForm.get("idValidDate")));
			if (editGrantOwnerForm.get("birthdate") != null
				&& !editGrantOwnerForm.get("birthdate").equals(""))
				infoPerson.setNascimento(sdf.parse((String) editGrantOwnerForm.get("birthdate")));
			if (editGrantOwnerForm.get("dateSendCGD") != null
				&& !editGrantOwnerForm.get("dateSendCGD").equals(""))
				infoGrantOwner.setDateSendCGD(sdf.parse((String) editGrantOwnerForm.get("dateSendCGD")));
		} catch (ParseException e)
		{
			throw new FenixServiceException();
		}

		//GrantOwner
		infoGrantOwner.setIdInternal(new Integer((String) editGrantOwnerForm.get("idInternal")));
		infoGrantOwner.setGrantOwnerNumber(
			new Integer((String) editGrantOwnerForm.get("grantOwnerNumber")));
		infoGrantOwner.setCardCopyNumber(new Integer((String) editGrantOwnerForm.get("cardCopyNumber")));

		//Person
		infoGrantOwner.setIdInternal(new Integer((String) editGrantOwnerForm.get("idInternalPerson")));
		infoPerson.setNumeroDocumentoIdentificacao((String) editGrantOwnerForm.get("idNumber"));
		infoPerson.setLocalEmissaoDocumentoIdentificacao((String) editGrantOwnerForm.get("idLocation"));
		infoPerson.setNome((String) editGrantOwnerForm.get("name"));
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
		infoPerson.setUsername((String) editGrantOwnerForm.get("personUsername"));
		infoPerson.setPassword((String) editGrantOwnerForm.get("password"));
		infoPerson.setCodigoFiscal((String) editGrantOwnerForm.get("fiscalCode"));

		TipoDocumentoIdentificacao tipoDocumentoIdentificacao = new TipoDocumentoIdentificacao();
		tipoDocumentoIdentificacao.setTipo((Integer) editGrantOwnerForm.get("idType"));
		infoPerson.setTipoDocumentoIdentificacao(tipoDocumentoIdentificacao);

		Sexo sexo = new Sexo();
		sexo.setSexo((Integer) editGrantOwnerForm.get("sex"));
		infoPerson.setSexo(sexo);

		EstadoCivil estadoCivil = new EstadoCivil();
		estadoCivil.setEstadoCivil((Integer) editGrantOwnerForm.get("maritalStatus"));
		infoPerson.setEstadoCivil(estadoCivil);

		InfoCountry infoCountry = new InfoCountry();
		infoCountry.setIdInternal((Integer) editGrantOwnerForm.get("country"));
		infoPerson.setInfoPais(infoCountry);

		infoGrantOwner.setPersonInfo(infoPerson);
		return infoGrantOwner;
	}
}