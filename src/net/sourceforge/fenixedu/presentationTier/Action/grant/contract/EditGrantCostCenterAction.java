/*
 * Created on 20/Jan/2004
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantCostCenterAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantCostCenterForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer idGrantCostCenter = null;
        if (verifyParameterInRequest(request, "idGrantCostCenter")) {
            idGrantCostCenter = new Integer(request.getParameter("idGrantCostCenter"));
        }

        if (idGrantCostCenter != null) //Edit
        {
            try {
                IUserView userView = SessionUtils.getUserView(request);

                //Read the grant cost center
                Object[] args = { idGrantCostCenter };
                InfoGrantCostCenter infoGrantCostCenter = (InfoGrantCostCenter) ServiceUtils
                        .executeService(userView, "ReadGrantPaymentEntity", args);

                //Populate the form
                setFormGrantCostCenter((DynaValidatorForm) form, infoGrantCostCenter);
            } catch (FenixServiceException e) {
                return setError(request, mapping, "errors.grant.costcenter.read", null, null);
            }
        }
        return mapping.findForward("edit-grant-costcenter");
    }

    /*
     * Edit a cost center
     */
    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InfoGrantCostCenter infoGrantCostCenter = null;
        try {
            infoGrantCostCenter = populateInfoFromForm((DynaValidatorForm) form);

            IUserView userView = SessionUtils.getUserView(request);
            //Check if teacher exists
            Object[] argTeacher = { infoGrantCostCenter.getInfoResponsibleTeacher().getTeacherNumber() };
            InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,
                    "ReadTeacherByNumber", argTeacher);
            if (infoTeacher == null) {
                return setError(request, mapping, "errors.grant.paymententity.unknownTeacher", null,
                        infoGrantCostCenter.getInfoResponsibleTeacher().getTeacherNumber());
            }
            infoGrantCostCenter.setInfoResponsibleTeacher(infoTeacher);

            //Edit-Create the payment entity
            Object[] argCostCenter = { infoGrantCostCenter };
            ServiceUtils.executeService(userView, "EditGrantPaymentEntity", argCostCenter);

            return mapping.findForward("manage-grant-costcenter");
        } catch (ExistingServiceException e) {
            return setError(request, mapping, "errors.grant.costcenter.duplicateEntry", null,
                    infoGrantCostCenter.getNumber());
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.costcenter.bd.create", null, null);
        }
    }

    /*
     * Populates form from InfoCostCenter
     */
    private void setFormGrantCostCenter(DynaValidatorForm form, InfoGrantCostCenter infoGrantCostCenter)
            throws Exception {
        BeanUtils.copyProperties(form, infoGrantCostCenter);
        if (infoGrantCostCenter.getInfoResponsibleTeacher() != null)
            form.set("responsibleTeacherNumber", infoGrantCostCenter.getInfoResponsibleTeacher()
                    .getTeacherNumber().toString());
    }

    /*
     * Populates Info from Form
     */
    private InfoGrantCostCenter populateInfoFromForm(DynaValidatorForm editGrantCostCenterForm)
            throws Exception {
        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
        BeanUtils.copyProperties(infoGrantCostCenter, editGrantCostCenterForm);

        infoGrantCostCenter.setOjbConcreteClass(GrantCostCenter.class.getName());

        //Build the teacher Number
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer((String) editGrantCostCenterForm
                .get("responsibleTeacherNumber")));
        infoGrantCostCenter.setInfoResponsibleTeacher(infoTeacher);

        return infoGrantCostCenter;
    }
}