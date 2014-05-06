package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherApplication.TeacherTeachingApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.collect.Ordering;

@StrutsFunctionality(app = TeacherTeachingApp.class, path = "manage-execution-course", titleKey = "link.manage.executionCourse")
@Mapping(module = "teacher", path = "/showProfessorships")
@Forwards(@Forward(name = "list", path = "/teacher/listProfessorships.jsp"))
public class ShowProfessorshipsDA extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final String executionPeriodIDString = request.getParameter("executionPeriodID");

        final ExecutionSemester selectedExecutionPeriod;
        if (executionPeriodIDString == null) {
            selectedExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else if (executionPeriodIDString.isEmpty()) {
            selectedExecutionPeriod = null;
        } else {
            selectedExecutionPeriod = FenixFramework.getDomainObject(executionPeriodIDString);
        }
        request.setAttribute("executionPeriod", selectedExecutionPeriod);

        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        request.setAttribute("executionCourses", executionCourses);

        final Person person = AccessControl.getPerson();
        final SortedSet<ExecutionSemester> executionSemesters =
                new TreeSet<ExecutionSemester>(Ordering.from(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR).reverse());
        if (person != null) {
            for (final Professorship professorship : person.getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();

                executionSemesters.add(executionSemester);
                if (selectedExecutionPeriod == null || selectedExecutionPeriod == executionSemester) {
                    executionCourses.add(executionCourse);
                }
            }
        }
        executionSemesters.add(ExecutionSemester.readActualExecutionSemester());

        request.setAttribute("semesters", executionSemesters);

        return mapping.findForward("list");
    }
}