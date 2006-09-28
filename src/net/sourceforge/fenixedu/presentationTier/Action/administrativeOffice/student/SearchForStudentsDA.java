package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentsSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchForStudentsDA extends FenixDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	
        StudentsSearchBean studentsSearchBean = (StudentsSearchBean) getRenderedObject();
        
        if (studentsSearchBean == null) { //1st time
            studentsSearchBean = new StudentsSearchBean();
        } else {
            Unit workingPlace = AccessControl.getUserView().getPerson().getEmployee().getCurrentWorkingPlace();
            final Set<Student> students = studentsSearchBean.searchForOffice(workingPlace);
        
            if(students.size() == 1){
                request.setAttribute("student", students.iterator().next());
                return mapping.findForward("viewStudentDetails");
            }
            request.setAttribute("students", students);
        }

        request.setAttribute("studentsSearchBean", studentsSearchBean);
        return mapping.findForward("search");
    }
    

}
