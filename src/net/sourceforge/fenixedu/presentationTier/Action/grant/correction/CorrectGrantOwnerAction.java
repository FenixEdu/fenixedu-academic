/*
 * Created on May 17, 2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.correction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Pica
 * @author Barbosa
 */
public class CorrectGrantOwnerAction extends FenixDispatchAction {

    public ActionForward prepareForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        /*List documentTypeList = TipoDocumentoIdentificacao.toIntegerArrayList();
        request.setAttribute("documentTypeList", documentTypeList);*/
        return mapping.findForward("correct-grant-owner");
    }

    /*
     * Associate a Grant Owner to another person (that isn't a Grant Owner)
     */
    public ActionForward changeAssociatedPerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        //Read the values from the Form
        Integer grantOwnerNumber = null;
        Integer documentIdNumber = null;
        IDDocumentType documentIdType = null;

        try {
            DynaValidatorForm correctGrantOwnerForm = (DynaValidatorForm) form;
            grantOwnerNumber = new Integer((String) correctGrantOwnerForm.get("grantOwnerNumber"));
            documentIdNumber = new Integer((String) correctGrantOwnerForm.get("documentIdNumber"));
            documentIdType = IDDocumentType.valueOf((String) correctGrantOwnerForm.get("documentIdType"));

            /*if (documentIdType.equals(new Integer(0))) {
                throw new Exception();
            }*/
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.correction.fillAllFields", null, null);
        }

        //Do the action
        try {
            IUserView userView = SessionUtils.getUserView(request);

            //Read the grant owner
            Object[] argsGrantOwner = { null, null, null, grantOwnerNumber, new Boolean(true), null };
            List infoGrantOwnerList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner",
                    argsGrantOwner);

            if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
                return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null,
                        null);
            }
            InfoGrantOwner infoGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);

            //Read the new person
            Object[] argsPerson = { null, documentIdNumber.toString(), documentIdType, null,
                    new Boolean(false), null };
            List infoPersonList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner",
                    argsPerson);

            if (infoPersonList.isEmpty() || infoPersonList.size() > 1) {
                return setError(request, mapping, "errors.grant.correction.unknownPerson", null, null);
            }

            //Verify if the new person is already a grant owner
            InfoGrantOwner newInfoGrantOwner = (InfoGrantOwner) infoPersonList.get(0);
            InfoPerson oldInfoPerson = null;

            if (newInfoGrantOwner.getIdInternal() != null) {
                return setError(request, mapping, "errors.grant.correction.personAlreadyGrantOwner",
                        null, null);
            }

            oldInfoPerson = infoGrantOwner.getPersonInfo();
            infoGrantOwner.setPersonInfo(newInfoGrantOwner.getPersonInfo());

            //Change username of the old person if is a "b***" to
            // INA(NumeroDocumentoIdentificacao)
            if (oldInfoPerson.getUsername().charAt(0) == 'B') {
                String newUsernameOldPerson = "INA";
                newUsernameOldPerson += oldInfoPerson.getNumeroDocumentoIdentificacao();

                Object[] argsChangeUsername = { newUsernameOldPerson, oldInfoPerson.getIdInternal() };
                ServiceUtils.executeService(userView, "ChangePersonUsername", argsChangeUsername);
            }

            //Change username of the new person if is a "INA***" to
            // B(GrantOwnerNumber)
            if (newInfoGrantOwner.getPersonInfo().getUsername().charAt(0) == 'I') {
                InfoPerson infoPerson = newInfoGrantOwner.getPersonInfo();
                String newUsernameNewPerson = "B";
                newUsernameNewPerson += infoGrantOwner.getGrantOwnerNumber().toString();
                infoPerson.setUsername(newUsernameNewPerson);

                Object[] argsChangeUsername = { newUsernameNewPerson, infoPerson.getIdInternal() };
                ServiceUtils.executeService(userView, "ChangePersonUsername", argsChangeUsername);
            }

            //Associate the new person with the grant owner, and save the
            // changes
            Object[] argsNewGrantOwner = { infoGrantOwner };
            ServiceUtils.executeService(userView, "EditGrantOwner", argsNewGrantOwner);

            //Set of the request variables and return
            //List documentTypeList = TipoDocumentoIdentificacao.toIntegerArrayList();
            //request.setAttribute("documentTypeList", documentTypeList);
            request.setAttribute("correctionNumber1", "yes");
            return mapping.findForward("correct-grant-owner");
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
    }
}