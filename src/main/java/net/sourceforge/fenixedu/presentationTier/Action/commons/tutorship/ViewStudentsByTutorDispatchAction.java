/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
