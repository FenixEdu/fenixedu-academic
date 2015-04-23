/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on May 4, 2006
 */
package org.fenixedu.academic.service.services.administrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetSearchResultBean;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;

import pt.ist.fenixframework.Atomic;

public class SearchMarkSheets {

    @Atomic
    public static Map<EvaluationSeason, MarkSheetSearchResultBean> run(MarkSheetManagementSearchBean searchBean)
            throws InvalidArgumentsServiceException {

        if (searchBean.getTeacherId() != null) {
            searchBean.setTeacher(Teacher.readByIstId(searchBean.getTeacherId()));
        }

        CurricularCourse curricularCourse = searchBean.getCurricularCourse();
        if (curricularCourse == null) {
            throw new InvalidArgumentsServiceException("error.noCurricularCourse");
        }

        Collection<MarkSheet> markSheets =
                curricularCourse.searchMarkSheets(searchBean.getExecutionPeriod(), searchBean.getTeacher(),
                        searchBean.getEvaluationDate(), searchBean.getMarkSheetState(), searchBean.getEvaluationSeason());

        Map<EvaluationSeason, MarkSheetSearchResultBean> result = new TreeMap<EvaluationSeason, MarkSheetSearchResultBean>();
        for (MarkSheet sheet : markSheets) {
            addToMap(result, sheet);
        }

        for (Entry<EvaluationSeason, MarkSheetSearchResultBean> entry : result.entrySet()) {

            MarkSheetSearchResultBean searchResultBean = entry.getValue();
            searchResultBean.setShowStatistics(!entry.getKey().isSpecialAuthorization());
            if (searchResultBean.isShowStatistics()) {
                searchResultBean.setTotalNumberOfStudents(getNumberOfStudentsNotEnrolled(searchBean, curricularCourse, entry)
                        + searchResultBean.getNumberOfEnroledStudents());
            }
        }

        return result;
    }

    private static int getNumberOfStudentsNotEnrolled(MarkSheetManagementSearchBean searchBean,
            CurricularCourse curricularCourse, Entry<EvaluationSeason, MarkSheetSearchResultBean> entry) {
        int studentsNotEnrolled =
                curricularCourse.getEnrolmentsNotInAnyMarkSheet(entry.getKey(), searchBean.getExecutionPeriod()).size();
        return studentsNotEnrolled;
    }

    private static void addToMap(Map<EvaluationSeason, MarkSheetSearchResultBean> result, MarkSheet sheet) {
        getResultBeanForSeason(result, sheet.getEvaluationSeason()).addMarkSheet(sheet);
    }

    private static MarkSheetSearchResultBean getResultBeanForSeason(Map<EvaluationSeason, MarkSheetSearchResultBean> result,
            EvaluationSeason sheetType) {
        MarkSheetSearchResultBean markSheetSearchResultBean = result.get(sheetType);
        if (markSheetSearchResultBean == null) {
            markSheetSearchResultBean = new MarkSheetSearchResultBean();
            result.put(sheetType, markSheetSearchResultBean);
        }
        return markSheetSearchResultBean;
    }

}
