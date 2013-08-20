package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class MasterDegreeThesisOperations extends FenixDispatchAction {

    public boolean getStudentByNumberAndDegreeType(ActionForm form, HttpServletRequest request, ActionErrors actionErrors)
            throws FenixActionException {

        DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;

        String scpID = getStringFromRequest(request, "scpID");
        if (scpID == null) {
            scpID = ((DynaActionForm) form).get("scpID").equals("") ? null : (String) ((DynaActionForm) form).get("scpID");
        }

        StudentCurricularPlan studentCurricularPlan = AbstractDomainObject.fromExternalId(scpID);

        return transportStudentCurricularPlan(form, request, actionErrors, studentCurricularPlan);
    }

    public boolean transportStudentCurricularPlan(ActionForm form, HttpServletRequest request, ActionErrors actionErrors,
            StudentCurricularPlan studentCurricularPlan) {

        if (studentCurricularPlan != null) {
            request.setAttribute(PresentationConstants.STUDENT, studentCurricularPlan.getRegistration());
            request.setAttribute("studentCurricularPlan", studentCurricularPlan);

            if (form != null) {
                DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
                getStudentByNumberAndDegreeTypeForm.set("degreeType", DegreeType.MASTER_DEGREE.name());
                getStudentByNumberAndDegreeTypeForm.set("scpID", studentCurricularPlan.getExternalId());
            }

            return true;
        }

        actionErrors.add("error.no.student.in.database", new ActionError("error.no.student.in.database"));
        return false;

    }

    public List<Teacher> getTeachersByNumbers(ActionForm form, HttpServletRequest request, String teachersNumbersListField,
            String sessionConstant, ActionErrors actionErrors) throws FenixActionException {

        final List<Teacher> teachers = Teacher.readByNumbers(getTeachersNumbers(form, teachersNumbersListField));
        if (!teachers.isEmpty()) {
            request.setAttribute(sessionConstant, teachers);
        }

        return teachers;

    }

    public List<String> getTeachersNumbers(ActionForm form, String teachersNumbersListField) {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;

        String[] teachersNumbersArray = (String[]) masterDegreeThesisForm.get(teachersNumbersListField);
        List<String> teachersNumbersList = CollectionUtils.toList(teachersNumbersArray);
        teachersNumbersList.remove(null);
        return teachersNumbersList;

    }

    public void getExternalPersonsByName(ActionForm form, HttpServletRequest request, String externalPersonNameField,
            String sessionConstant, ActionErrors actionErrors) throws FenixActionException {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;
        String externalAssistentGuiderName = (String) masterDegreeThesisForm.get(externalPersonNameField);

        List<ExternalContract> externalPersons = ExternalContract.readByPersonName(externalAssistentGuiderName);
        if (!externalPersons.isEmpty()) {
            request.setAttribute(sessionConstant, externalPersons);
        } else {
            actionErrors.add("label.masterDegree.administrativeOffice.searchResultsEmpty", new ActionError(
                    "label.masterDegree.administrativeOffice.searchResultsEmpty"));
        }

    }

    public List<String> getExternalPersonsIDs(ActionForm form, String externalPersonNameField) {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;

        String[] externalPersonsIDsArray = (String[]) masterDegreeThesisForm.get(externalPersonNameField);
        List<String> externalPersonsIDsList = Arrays.asList(externalPersonsIDsArray);
        externalPersonsIDsList.remove(null);
        return externalPersonsIDsList;

    }

    public List<ExternalContract> getExternalPersonsByIDs(ActionForm form, HttpServletRequest request,
            String externalPersonsIDsListField, String sessionConstant, ActionErrors actionErrors) {

        List<ExternalContract> externalPersons =
                ExternalContract.readByIDs(getExternalPersonsIDs(form, externalPersonsIDsListField));
        if (!externalPersons.isEmpty()) {
            request.setAttribute(sessionConstant, externalPersons);
        }

        return externalPersons;

    }

}