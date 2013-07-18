package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.framework.SearchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

public class TeacherSearchForSummariesManagement extends SearchAction {

    @Override
    protected Collection searchIt(ActionForm form, HttpServletRequest request) throws Exception {
        String teacherId = (String) ((DynaActionForm) form).get("teacherId");
        Teacher teacher = Teacher.readTeacherByUsername(teacherId);

        String employeeDepartment = AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getName();

        if (teacher == null || !employeeDepartment.equals(teacher.getCurrentWorkingDepartment().getName())) {
            return new ArrayList();
        } else {
            ArrayList list = new ArrayList();
            list.add(InfoTeacher.newInfoFromDomain(teacher));
            return list;
        }
    }

    @Override
    protected String getObjectAttribute() {
        return "infoTeacher";
    }

    @Override
    protected String getListAttribute() {
        return "infoTeacherList";
    }

    @Override
    protected String getNotFoundMessageKey() {
        return "errors.teacher.not.found";
    }

    @Override
    protected String getDefaultSortBy() {
        return null;
    }

}
