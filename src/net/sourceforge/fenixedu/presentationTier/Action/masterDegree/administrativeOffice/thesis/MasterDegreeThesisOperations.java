package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class MasterDegreeThesisOperations extends FenixDispatchAction {

    public boolean getStudentByNumberAndDegreeType(ActionForm form, HttpServletRequest request, ActionErrors actionErrors)
	    throws FenixActionException, FenixFilterException {

	DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;

	Integer scpID = getIntegerFromRequestOrForm(request, getStudentByNumberAndDegreeTypeForm, "scpID");
	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpID);

	return transportStudentCurricularPlan(form, request, actionErrors, studentCurricularPlan);
    }

    public boolean transportStudentCurricularPlan(ActionForm form, HttpServletRequest request, ActionErrors actionErrors,
	    StudentCurricularPlan studentCurricularPlan) {

	if (studentCurricularPlan != null) {
	    request.setAttribute(SessionConstants.STUDENT, studentCurricularPlan.getRegistration());
	    request.setAttribute("studentCurricularPlan", studentCurricularPlan);

	    if (form != null) {
		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		getStudentByNumberAndDegreeTypeForm.set("degreeType", DegreeType.MASTER_DEGREE.name());
		getStudentByNumberAndDegreeTypeForm.set("scpID", studentCurricularPlan.getIdInternal());
	    }

	    return true;
	}

	actionErrors.add("error.no.student.in.database", new ActionError("error.no.student.in.database"));
	return false;

    }

    public List<Teacher> getTeachersByNumbers(ActionForm form, HttpServletRequest request, String teachersNumbersListField,
	    String sessionConstant, ActionErrors actionErrors) throws FenixActionException, FenixFilterException {

	final List<Teacher> teachers = Teacher.readByNumbers(getTeachersNumbers(form, teachersNumbersListField));
	if (!teachers.isEmpty()) {
	    request.setAttribute(sessionConstant, teachers);
	}

	return teachers;

    }

    public List<Integer> getTeachersNumbers(ActionForm form, String teachersNumbersListField) {

	DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;

	Integer[] teachersNumbersArray = (Integer[]) masterDegreeThesisForm.get(teachersNumbersListField);
	List<Integer> teachersNumbersList = CollectionUtils.toList(teachersNumbersArray);
	teachersNumbersList.remove(new Integer(0));
	return teachersNumbersList;

    }

    public void getExternalPersonsByName(ActionForm form, HttpServletRequest request, String externalPersonNameField,
	    String sessionConstant, ActionErrors actionErrors) throws FenixActionException, FenixFilterException {

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

    public List<Integer> getExternalPersonsIDs(ActionForm form, String externalPersonNameField) {

	DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;

	Integer[] externalPersonsIDsArray = (Integer[]) masterDegreeThesisForm.get(externalPersonNameField);
	List<Integer> externalPersonsIDsList = CollectionUtils.toList(externalPersonsIDsArray);
	externalPersonsIDsList.remove(new Integer(0));
	return externalPersonsIDsList;

    }

    public List<ExternalContract> getExternalPersonsByIDs(ActionForm form, HttpServletRequest request,
	    String externalPersonsIDsListField, String sessionConstant, ActionErrors actionErrors) throws FenixActionException,
	    FenixFilterException {

	List<ExternalContract> externalPersons = ExternalContract.readByIDs(getExternalPersonsIDs(form,
		externalPersonsIDsListField));
	if (!externalPersons.isEmpty()) {
	    request.setAttribute(sessionConstant, externalPersons);
	}

	return externalPersons;

    }

}