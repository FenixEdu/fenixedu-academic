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
/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author nmgo
 * @author lmre
 */
public class InfoCurriculumHistoricReport implements Serializable {

    int evaluated = 0;

    int approved = 0;

    Collection<InfoEnrolmentHistoricReport> enrolments;

    CurricularCourse curricularCourse;

    AcademicInterval academicInterval;

    public Integer getApproved() {
        return approved;
    }

    public Integer getEvaluated() {
        return evaluated;
    }

    public Collection<InfoEnrolmentHistoricReport> getEnrolments() {
        return enrolments;
    }

    public Integer getEnroled() {
        return getEnrolments().size();
    }

    public Integer getRatioApprovedEnroled() {
        return Math.round(((float) getApproved() / (float) getEnroled()) * 100);
    }

    public Integer getRatioApprovedEvaluated() {
        return Math.round(((float) getApproved() / (float) getEvaluated()) * 100);
    }

    public CurricularCourse getCurricularCourse() {
        return this.curricularCourse;
    }

    private void setCurricularCourse(final CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;

    }

    public InfoCurriculumHistoricReport(final AcademicInterval academicInterval, final CurricularCourse curricularCourse) {
        setAcademicInterval(academicInterval);
        setCurricularCourse(curricularCourse);

        init();
    }

    private void init() {
        this.enrolments =
                new TreeSet<InfoEnrolmentHistoricReport>(
                        new BeanComparator("enrolment.studentCurricularPlan.registration.number"));
        for (final Enrolment enrolment : getCurricularCourse().getEnrolmentsByAcademicInterval(academicInterval)) {
            if (!enrolment.isAnnulled()) {
                this.enrolments.add(new InfoEnrolmentHistoricReport(enrolment));

                if (enrolment.isEvaluated()) {
                    this.evaluated++;

                    if (enrolment.isEnrolmentStateApproved()) {
                        this.approved++;
                    }
                }
            }
        }
    }

    public AcademicInterval getAcademicInterval() {
        return academicInterval;
    }

    public void setAcademicInterval(AcademicInterval academicInterval) {
        this.academicInterval = academicInterval;
    }

}
