/*
 * Created on 20/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import DataBeans.grant.contract.InfoGrantProject;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantProjectAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantProjectForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer idGrantProject = null;
        if (verifyParameterInRequest(request, "idGrantProject")) {
            idGrantProject = new Integer(request.getParameter("idGrantProject"));
        }

        if (idGrantProject != null) { //Edit
            try {
                DynaValidatorForm grantProjectForm = (DynaValidatorForm) form;
                IUserView userView = SessionUtils.getUserView(request);

                //Read the grant project
                Object[] args = { idGrantProject };
                InfoGrantProject infoGrantProject = (InfoGrantProject) ServiceUtils.executeService(
                        userView, "ReadGrantPaymentEntity", args);

                //Populate the form
                setFormGrantProject(grantProjectForm, infoGrantProject);
            } catch (FenixServiceException e) {
                return setError(request, mapping, "errors.grant.project.read", null, null);
            }
        }
        return mapping.findForward("edit-grant-project");
    }

    /*
     * Edit Grant Project
     */
    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoGrantProject infoGrantProject = null;
        try {
            IUserView userView = SessionUtils.getUserView(request);

            DynaValidatorForm editGrantProjectForm = (DynaValidatorForm) form;
            infoGrantProject = populateInfoFromForm(editGrantProjectForm);

            //Check if teacher exists
            InfoTeacher infoTeacher = null;
            if (infoGrantProject.getInfoResponsibleTeacher() != null) {
                Object[] argsTeacher = { infoGrantProject.getInfoResponsibleTeacher().getTeacherNumber() };
                infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber",
                        argsTeacher);
            }

            if (infoTeacher == null) {
                return setError(request, mapping, "errors.grant.paymententity.unknownTeacher", null,
                        editGrantProjectForm.get("responsibleTeacherNumber"));
            }

            infoGrantProject.setInfoResponsibleTeacher(infoTeacher);

            //Check if grant cost center exists
            InfoGrantCostCenter infoGrantCostCenter = null;
            if (infoGrantProject.getInfoGrantCostCenter() != null) {
                Object[] argsCostCenter = { infoGrantProject.getInfoGrantCostCenter().getNumber(),
                        InfoGrantPaymentEntity.getGrantCostCenterOjbConcreteClass() };
                infoGrantCostCenter = (InfoGrantCostCenter) ServiceUtils.executeService(userView,
                        "ReadPaymentEntityByNumberAndClass", argsCostCenter);
            }

            if (infoGrantCostCenter == null) {
                return setError(request, mapping, "errors.grant.paymententity.unknownCostCenter", null,
                        editGrantProjectForm.get("grantCostCenterNumber"));
            }

            infoGrantProject.setInfoGrantCostCenter(infoGrantCostCenter);

            //Edit-Create the project
            Object[] args = { infoGrantProject };
            ServiceUtils.executeService(userView, "EditGrantPaymentEntity", args);

            return mapping.findForward("manage-grant-project");
        } catch (ExistingServiceException e) {
            return setError(request, mapping, "errors.grant.project.duplicateEntry", null,
                    infoGrantProject.getNumber());
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.project.bd.create", null, null);
        }
    }

    /*
     * Populates form from InfoProject
     */
    private void setFormGrantProject(DynaValidatorForm form, InfoGrantProject infoGrantProject)
            throws Exception {
        BeanUtils.copyProperties(form, infoGrantProject);
        if (infoGrantProject.getInfoResponsibleTeacher() != null)
            form.set("responsibleTeacherNumber", infoGrantProject.getInfoResponsibleTeacher()
                    .getTeacherNumber().toString());
        if (infoGrantProject.getInfoGrantCostCenter() != null)
            form.set("grantCostCenterNumber", infoGrantProject.getInfoGrantCostCenter().getNumber());
    }

    /*
     * Populates Info from Form
     */
    private InfoGrantProject populateInfoFromForm(DynaValidatorForm editGrantProjectForm)
            throws Exception {
        InfoGrantProject infoGrantProject = new InfoGrantProject();

        BeanUtils.copyProperties(infoGrantProject, editGrantProjectForm);
        infoGrantProject.setOjbConcreteClass(InfoGrantPaymentEntity.getGrantProjectOjbConcreteClass());

        //Copy the teacher Number
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer((String) editGrantProjectForm
                .get("responsibleTeacherNumber")));
        infoGrantProject.setInfoResponsibleTeacher(infoTeacher);

        //Copy the cost center Number
        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
        infoGrantCostCenter.setNumber((String) editGrantProjectForm.get("grantCostCenterNumber"));
        infoGrantProject.setInfoGrantCostCenter(infoGrantCostCenter);

        return infoGrantProject;
    }
}