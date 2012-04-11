package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.enrollments;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.EnrollStudentInShifts.StudentNotFoundServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "resourceAllocationManager", path = "/enrollStudentInShifts", input = "/studentShiftEnrollmentManagerLoockup.do?method=Escolher Turnos&page=0", attribute = "studentShiftEnrollmentForm", formBean = "studentShiftEnrollmentForm", scope = "request", validate = false)
@Forwards(value = { @Forward(name = "enrollmentConfirmation", path = "/studentShiftEnrollmentManagerLoockup.do?method=Escolher Turnos") })
public class EnrollStudentInShiftsAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException {

	final IUserView userView = getUserView(request);

	final Integer shiftId = Integer.valueOf(request.getParameter("shiftId"));
	if (!StringUtils.isEmpty(request.getParameter("executionCourseID"))) {
	    request.setAttribute("executionCourseID", request.getParameter("executionCourseID"));
	}

	try {
	    ShiftEnrollmentErrorReport errorReport = (ShiftEnrollmentErrorReport) ServiceUtils.executeService(
		    "EnrollStudentInShifts", new Object[] { getRegistration(request), shiftId });

	    if (errorReport.getUnAvailableShifts().size() > 0) {
		for (final Shift shift : (List<Shift>) errorReport.getUnAvailableShifts()) {
		    if (shift.getLotacao().intValue() == 0) {
			addActionMessage(request, "error.shift.enrollment.capacityLocked", shift.getNome());
		    } else {
			addActionMessage(request, "error.shift.enrollment.capacityExceded", shift.getNome());
		    }
		}
	    }
	    if (errorReport.getUnExistingShifts().size() > 0) {
		addActionMessage(request, "error.shift.enrollment.nonExistingShift");
	    }
	} catch (StudentNotFoundServiceException e) {
	    e.printStackTrace();
	    addActionMessage(request, "error.shift.enrollment.nonExistingStudent");
	    return mapping.getInputForward();

	} catch (FenixServiceException e) {
	    e.printStackTrace();
	    addActionMessage(request, e.getMessage());
	    return mapping.getInputForward();
	}

	saveMessages(request);
	return mapping.findForward("enrollmentConfirmation");
    }

    private Registration getRegistration(HttpServletRequest request) {
	return rootDomainObject.readRegistrationByOID(Integer.valueOf(request.getParameter("registrationOID")));
    }

    private Registration getStudent(final IUserView userView) {
	Registration registration = userView.getPerson().getStudentByUsername();
	if (registration == null) {
	    registration = userView.getPerson().getStudentByType(DegreeType.DEGREE);
	}
	return registration;
    }
}
