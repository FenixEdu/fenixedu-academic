package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.struts.action.ActionForm;

public class ExecutionCourseManagementForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String[] curricularCoursesToTransferIds;
    private String[] shiftsToTransferIds;

    public ExecutionCourseManagementForm() {
    }

    public String[] getCurricularCoursesToTransferIds() {
        return curricularCoursesToTransferIds;
    }

    public void setCurricularCoursesToTransferIds(String[] curricularCoursesToTransferIds) {
        this.curricularCoursesToTransferIds = curricularCoursesToTransferIds;
    }

    public String[] getShiftsToTransferIds() {
        return shiftsToTransferIds;
    }

    public void setShiftsToTransferIds(String[] shiftsToTransferIds) {
        this.shiftsToTransferIds = shiftsToTransferIds;
    }

    public List<CurricularCourse> readCurricularCoursesToTransfer() {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        for (String id : curricularCoursesToTransferIds) {
            result.add((CurricularCourse) CurricularCourse.fromExternalId(id));
        }

        return result;
    }

    public List<Shift> readShifts() {
        List<Shift> result = new ArrayList<Shift>();

        for (String id : shiftsToTransferIds) {
            result.add((Shift) Shift.fromExternalId(id));
        }

        return result;
    }

}
