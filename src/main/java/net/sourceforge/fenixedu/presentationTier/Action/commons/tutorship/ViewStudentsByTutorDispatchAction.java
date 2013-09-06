package net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.TutorTutorshipsHistoryBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;

public abstract class ViewStudentsByTutorDispatchAction extends FenixDispatchAction {

    protected void getTutorships(HttpServletRequest request, final Teacher teacher) {

        if (!teacher.getTutorships().isEmpty()) {
            TutorTutorshipsHistoryBean tutorshipHistory = new TutorTutorshipsHistoryBean(teacher);

            List<StudentsByTutorBean> activeTutorshipsByEntryYear = getTutorshipsByEntryYear(teacher.getActiveTutorships());
            tutorshipHistory.setActiveTutorshipsByEntryYear(activeTutorshipsByEntryYear);

            List<StudentsByTutorBean> pastTutorshipsByEntryYear = getTutorshipsByEntryYear(teacher.getPastTutorships());
            tutorshipHistory.setPastTutorshipsByEntryYear(pastTutorshipsByEntryYear);

            request.setAttribute("tutorshipHistory", tutorshipHistory);
        }
    }

    protected List<StudentsByTutorBean> getTutorshipsByEntryYear(List<Tutorship> tutorships) {
        Map<ExecutionYear, StudentsByTutorBean> tutorshipsMapByEntryYear = new HashMap<ExecutionYear, StudentsByTutorBean>();

        for (final Tutorship tutorship : tutorships) {
            ExecutionYear entryYear =
                    ExecutionYear.getExecutionYearByDate(tutorship.getStudentCurricularPlan().getRegistration().getStartDate());
            if (!tutorship.getStudentCurricularPlan().getRegistration().isCanceled()) {
                if (tutorshipsMapByEntryYear.containsKey(entryYear)) {
                    StudentsByTutorBean studentsByTutorBean = tutorshipsMapByEntryYear.get(entryYear);
                    studentsByTutorBean.getStudentsList().add(tutorship);
                    Collections.sort(studentsByTutorBean.getStudentsList(), Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);
                } else {
                    List<Tutorship> studentsByEntryYearList = new ArrayList<Tutorship>();
                    studentsByEntryYearList.add(tutorship);
                    StudentsByTutorBean studentsByTutorBean =
                            new StudentsByTutorBean(tutorship.getTeacher(), entryYear, studentsByEntryYearList);
                    tutorshipsMapByEntryYear.put(entryYear, studentsByTutorBean);
                }
            }
        }

        List<StudentsByTutorBean> tutorshipsByEntryYear = new ArrayList<StudentsByTutorBean>(tutorshipsMapByEntryYear.values());
        Collections.sort(tutorshipsByEntryYear, new BeanComparator("studentsEntryYear"));
        Collections.reverse(tutorshipsByEntryYear);

        return tutorshipsByEntryYear;
    }

}
