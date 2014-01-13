package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/ShowStudentStatutes", module = "student")
@Forwards({ @Forward(name = "studentStatutes", path = "/student/showStudentStatutes.jsp", tileProperties = @Tile(
        title = "private.student.view.statutes")) })
public class ShowStudentStatutesDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        User userView = getUserView(request);
        Student student = userView.getPerson().getStudent();
        ArrayList<StudentStatute> studentStatutes = new ArrayList<StudentStatute>(student.getStudentStatutes());
        Collections.sort(studentStatutes, new BeanComparator("beginExecutionPeriod.beginDateYearMonthDay"));
        request.setAttribute("studentStatutes", studentStatutes);
        return mapping.findForward("studentStatutes");
    }

}
