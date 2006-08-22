/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantProject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

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

        if (idGrantProject != null) { // Edit
            try {
                DynaValidatorForm grantProjectForm = (DynaValidatorForm) form;
                IUserView userView = SessionUtils.getUserView(request);

                // Read the grant project
                Object[] args = { idGrantProject };
                InfoGrantProject infoGrantProject = (InfoGrantProject) ServiceUtils.executeService(
                        userView, "ReadGrantPaymentEntity", args);

                // Populate the form
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

        InfoGrantProject infoGrantProject = new InfoGrantProject();
        try {
            IUserView userView = SessionUtils.getUserView(request);

            DynaValidatorForm editGrantProjectForm = (DynaValidatorForm) form;
            infoGrantProject = populateInfoFromForm(editGrantProjectForm);

            // Check if teacher exists
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

            // Check if grant cost center exists
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

            // Edit-Create the project
            Object[] args = { infoGrantProject };
            ServiceUtils.executeService(userView, "EditGrantPaymentEntity", args);

            return mapping.findForward("manage-grant-project");
        } catch (ExistingServiceException e) {
            return setError(request, mapping, "errors.grant.project.duplicateEntry", null,
                    infoGrantProject.getNumber());
        } catch (FenixServiceException e) {
            e.printStackTrace();
            return setError(request, mapping, "errors.grant.project.bd.create", null, null);
        }
    }

    /*
     * Populates form from InfoProject
     */
    private void setFormGrantProject(DynaValidatorForm form, InfoGrantProject infoGrantProject)
            throws Exception {
        form.set("idInternal", infoGrantProject.getIdInternal());
        form.set("designation", infoGrantProject.getDesignation());
        form.set("number", infoGrantProject.getNumber());
        if (infoGrantProject.getInfoResponsibleTeacher() != null)
            form.set("responsibleTeacherNumber", infoGrantProject.getInfoResponsibleTeacher()
                    .getTeacherNumber().toString());
        if (infoGrantProject.getInfoGrantCostCenter() != null)
            form.set("grantCostCenterNumber", infoGrantProject.getInfoGrantCostCenter().getNumber());
        form.set("ojbConcreteClass", InfoGrantPaymentEntity.getGrantProjectOjbConcreteClass());
    }

    /*
     * Populates Info from Form
     */
    private InfoGrantProject populateInfoFromForm(DynaValidatorForm editGrantProjectForm)
            throws Exception {
        InfoGrantProject infoGrantProject = new InfoGrantProject();
        infoGrantProject.setIdInternal((Integer) editGrantProjectForm.get("idInternal"));
        infoGrantProject.setDesignation((String) editGrantProjectForm.get("designation"));
        infoGrantProject.setNumber((String) editGrantProjectForm.get("number"));
        infoGrantProject.setOjbConcreteClass(InfoGrantPaymentEntity.getGrantProjectOjbConcreteClass());

        // Copy the teacher Number
        InfoTeacher infoTeacher = new InfoTeacher(Teacher.readByNumber(new Integer((String) editGrantProjectForm
                .get("responsibleTeacherNumber"))));
        infoGrantProject.setInfoResponsibleTeacher(infoTeacher);

        // Copy the cost center Number
        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
        infoGrantCostCenter.setNumber((String) editGrantProjectForm.get("grantCostCenterNumber"));
        infoGrantProject.setInfoGrantCostCenter(infoGrantCostCenter);

        return infoGrantProject;
    }
}