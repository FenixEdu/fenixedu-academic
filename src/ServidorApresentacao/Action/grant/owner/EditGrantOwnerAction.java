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
            request.setAttribute("infoGrantOwner", infoGrantOwner);
            return mapping.findForward("manage-grant-owner-form");
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
            setFormGrantOwnerInformation(grantOwnerInformationForm, infoGrantOwner);
        if (infoGrantOwner.getPersonInfo() != null
            && infoGrantOwner.getPersonInfo().getIdInternal() != null
            && infoGrantOwner.getPersonInfo().getIdInternal() != new Integer(0))
            setFormPersonalInformation(grantOwnerInformationForm, infoGrantOwner);

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

        /*
         * Variaveis da string de procura para o butao back
         */
        //String searchName = (String) request.getAttribute("name");
        //String searchIdNumber = (String) request.getAttribute("idNumber");
        //Integer searchIdType = (Integer) request.getAttribute("idType");
        //grantOwnerInformationForm.set("searchName", searchName);
        //grantOwnerInformationForm.set("searchIdNumber", searchIdNumber);
        //grantOwnerInformationForm.set("searchIdType", searchIdType);

        return mapping.findForward("edit-grant-owner-form");
    }

    private void setFormPersonalInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner)
    {
        InfoPerson infoPerson = infoGrantOwner.getPersonInfo();
        form.set("name", infoPerson.getNome());
        form.set("idNumber", infoPerson.getNumeroDocumentoIdentificacao());
        form.set("idType", infoPerson.getTipoDocumentoIdentificacao().getTipo());
        form.set("idLocation", infoPerson.getLocalEmissaoDocumentoIdentificacao());
        form.set("idDate", infoPerson.getDataEmissaoDocumentoIdentificacao().toString());
        form.set("idValidDate", infoPerson.getDataValidadeDocumentoIdentificacao().toString());
        form.set("birthdate", infoPerson.getNascimento().toString());
        form.set("fatherName", infoPerson.getNomePai());
        form.set("motherName", infoPerson.getNomeMae());
        form.set("nationality", infoPerson.getNacionalidade());
        form.set("parishOfBirth", infoPerson.getFreguesiaNaturalidade());
        form.set("districtSubBirth", infoPerson.getConcelhoNaturalidade());
        form.set("districtBirth", infoPerson.getDistritoNaturalidade());
        form.set("address", infoPerson.getMorada());
        form.set("area", infoPerson.getLocalidade());
        form.set("areaCode", infoPerson.getCodigoPostal());
        form.set("areaOfAreaCode", infoPerson.getLocalidadeCodigoPostal());
        form.set("addressParish", infoPerson.getFreguesiaMorada());
        form.set("addressDistrictSub", infoPerson.getConcelhoMorada());
        form.set("addressDistrict", infoPerson.getDistritoMorada());
        form.set("phone", infoPerson.getTelefone());
        form.set("cellphone", infoPerson.getTelemovel());
        form.set("email", infoPerson.getEmail());
        form.set("homepage", infoPerson.getEnderecoWeb());
        form.set("socialSecurityNumber", infoPerson.getNumContribuinte());
        form.set("profession", infoPerson.getProfissao());
        form.set("fiscalCode", infoPerson.getCodigoFiscal());
        form.set("sex", infoPerson.getSexo().getSexo());
        form.set("maritalStatus", infoPerson.getEstadoCivil().getEstadoCivil());
		if(infoPerson.getInfoPais() != null)
			form.set("country", infoPerson.getInfoPais().getIdInternal());
    }

    private void setFormGrantOwnerInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner)
    {
        if (infoGrantOwner.getDateSendCGD() != null)
            form.set("dateSendCGD", infoGrantOwner.getDateSendCGD().toString());
        if (infoGrantOwner.getCardCopyNumber() != null)
            form.set("cardCopyNumber", infoGrantOwner.getCardCopyNumber().toString());
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

        //Read Grant Owner By Person Id
        //TODO: service doesn't exist
        Integer grantOwnerId = new Integer(0);
        request.setAttribute("grantOwnerId", grantOwnerId);

        ActionForward actionForward = mapping.findForward("manage-grant-owner");
        return actionForward;
    }

    private InfoGrantOwner populateInfoFromForm(DynaValidatorForm editGrantOwnerForm)
        throws FenixServiceException
    {
        InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
        InfoPerson infoPerson = new InfoPerson();

        //Format of date in the form
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            infoPerson.setDataEmissaoDocumentoIdentificacao(
                sdf.parse((String) editGrantOwnerForm.get("idDate")));
            infoPerson.setDataValidadeDocumentoIdentificacao(
                sdf.parse((String) editGrantOwnerForm.get("idValidDate")));
            infoPerson.setNascimento(sdf.parse((String) editGrantOwnerForm.get("birthdate")));

            infoGrantOwner.setDateSendCGD(sdf.parse((String) editGrantOwnerForm.get("dateSendCGD")));
        } catch (ParseException e)
        {
            throw new FenixServiceException();
        }

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
        infoCountry.setIdInternal((Integer)editGrantOwnerForm.get("country"));
        infoPerson.setInfoPais(infoCountry);

        infoGrantOwner.setPersonInfo(infoPerson);

        return infoGrantOwner;
    }
}