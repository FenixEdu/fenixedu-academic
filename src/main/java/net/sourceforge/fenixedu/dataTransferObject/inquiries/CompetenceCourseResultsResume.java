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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;

import org.apache.commons.beanutils.BeanComparator;

public class CompetenceCourseResultsResume implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CurricularCourseResumeResult> curricularCourseResumeResults;
    private CompetenceCourse competenceCourse;

    public CompetenceCourseResultsResume(CompetenceCourse competenceCourse) {
        setCompetenceCourse(competenceCourse);
    }

    @Override
    public int hashCode() {
        return getCompetenceCourse().hashCode();
    }

    public void addCurricularCourseResumeResult(CurricularCourseResumeResult curricularCourseResumeResult) {
        if (getCurricularCourseResumeResults() == null) {
            setCurricularCourseResumeResults(new ArrayList<CurricularCourseResumeResult>());
        }
        getCurricularCourseResumeResults().add(curricularCourseResumeResult);
    }

    public List<CurricularCourseResumeResult> getOrderedCurricularCourseResumes() {
        Collections.sort(getCurricularCourseResumeResults(), new BeanComparator("firstPresentationName"));
        return getCurricularCourseResumeResults();
    }

    public void setCurricularCourseResumeResults(List<CurricularCourseResumeResult> curricularCourseResumeResults) {
        this.curricularCourseResumeResults = curricularCourseResumeResults;
    }

    public List<CurricularCourseResumeResult> getCurricularCourseResumeResults() {
        return curricularCourseResumeResults;
    }

    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }
}
