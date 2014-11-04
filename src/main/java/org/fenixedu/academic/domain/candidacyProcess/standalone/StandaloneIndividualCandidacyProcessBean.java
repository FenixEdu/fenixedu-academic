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
package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.commons.CurricularCourseByExecutionSemesterBean;
import net.sourceforge.fenixedu.dataTransferObject.commons.SearchCurricularCourseByDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;

import org.joda.time.LocalDate;

public class StandaloneIndividualCandidacyProcessBean extends IndividualCandidacyProcessBean {

    private SearchCurricularCourseByDegree searchCurricularCourseByDegree;
    private List<CurricularCourseByExecutionSemesterBean> curricularCourseBeans;

    public StandaloneIndividualCandidacyProcessBean() {
        setCandidacyDate(new LocalDate());
        setCurricularCourseBeans(new ArrayList<CurricularCourseByExecutionSemesterBean>(4));
    }

    public StandaloneIndividualCandidacyProcessBean(final StandaloneIndividualCandidacyProcess process) {
        setCandidacyDate(process.getCandidacyDate());
        setCurricularCourseBeans(process.getCurricularCourseBeans());
    }

    @Override
    public StandaloneCandidacyProcess getCandidacyProcess() {
        return (StandaloneCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    public ExecutionSemester getCandidacyExecutionInterval() {
        return (ExecutionSemester) super.getCandidacyExecutionInterval();
    }

    public SearchCurricularCourseByDegree getSearchCurricularCourseByDegree() {
        return searchCurricularCourseByDegree;
    }

    public void setSearchCurricularCourseByDegree(SearchCurricularCourseByDegree searchCurricularCourseByDegree) {
        this.searchCurricularCourseByDegree = searchCurricularCourseByDegree;
    }

    public List<CurricularCourseByExecutionSemesterBean> getCurricularCourseBeans() {
        return curricularCourseBeans;
    }

    public void setCurricularCourseBeans(List<CurricularCourseByExecutionSemesterBean> curricularCourseBeans) {
        this.curricularCourseBeans = curricularCourseBeans;
    }

    public List<CurricularCourse> getCurricularCourses() {
        final List<CurricularCourse> result = new ArrayList<CurricularCourse>(getCurricularCourseBeans().size());
        for (final CurricularCourseByExecutionSemesterBean bean : getCurricularCourseBeans()) {
            result.add(bean.getCurricularCourse());
        }
        return result;
    }

    public void addSelectedCurricularCourseToResult() {
        if (getSearchCurricularCourseByDegree().hasCurricularCourseBean() && !containsCurricularCourseByDegree()) {
            this.curricularCourseBeans.add(getSearchCurricularCourseByDegree().getCurricularCourseBean());
        }
    }

    private boolean containsCurricularCourseByDegree() {
        return this.curricularCourseBeans.contains(getSearchCurricularCourseByDegree().getCurricularCourseBean());
    }

    public void removeCurricularCourseFromResult(CurricularCourseByExecutionSemesterBean bean) {
        this.curricularCourseBeans.remove(bean);
    }

    public String getTotalEctsCredits() {
        double total = 0d;
        for (final CurricularCourseByExecutionSemesterBean bean : curricularCourseBeans) {
            total += bean.getCurricularCourseEcts().doubleValue();
        }
        return String.valueOf(total);
    }

    @Override
    public boolean isStandalone() {
        return true;
    }
}
