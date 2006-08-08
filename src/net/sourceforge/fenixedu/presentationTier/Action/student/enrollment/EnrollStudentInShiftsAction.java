package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.EnrollStudentInShifts.StudentNotFoundServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EnrollStudentInShiftsAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {

        final IUserView userView = getUserView(request);
        
        final Integer shiftId = Integer.valueOf(request.getParameter("shiftId"));
        if(!StringUtils.isEmpty(request.getParameter("executionCourseID"))) {            
            request.setAttribute("executionCourseID", request.getParameter("executionCourseID"));
        }        

        try {
            ShiftEnrollmentErrorReport errorReport = (ShiftEnrollmentErrorReport) ServiceUtils
					.executeService(userView, "EnrollStudentInShifts", new Object[] {
							getStudent(userView), shiftId });

            if (errorReport.getUnAvailableShifts().size() > 0) {
            	for (final Shift shift : (List<Shift>) errorReport.getUnAvailableShifts()) {
            		addActionMessage(request, "error.shift.enrollment.capacityExceded", shift.getNome());
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
        
        return mapping.findForward("enrollmentConfirmation");
    }
    
	private Student getStudent(final IUserView userView) {
		return userView.getPerson().getStudentByUsername();
	}
}