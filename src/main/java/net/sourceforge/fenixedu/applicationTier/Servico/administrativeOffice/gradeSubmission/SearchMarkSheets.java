/*
 * Created on May 4, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSearchResultBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;

public class SearchMarkSheets {

    @Atomic
    public static Map<MarkSheetType, MarkSheetSearchResultBean> run(MarkSheetManagementSearchBean searchBean)
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
                        searchBean.getEvaluationDate(), searchBean.getMarkSheetState(), searchBean.getMarkSheetType());

        Map<MarkSheetType, MarkSheetSearchResultBean> result = new TreeMap<MarkSheetType, MarkSheetSearchResultBean>();
        for (MarkSheet sheet : markSheets) {
            addToMap(result, sheet);
        }

        for (Entry<MarkSheetType, MarkSheetSearchResultBean> entry : result.entrySet()) {

            MarkSheetSearchResultBean searchResultBean = entry.getValue();
            searchResultBean.setShowStatistics(entry.getKey() != MarkSheetType.SPECIAL_AUTHORIZATION);
            if (searchResultBean.isShowStatistics()) {
                searchResultBean.setTotalNumberOfStudents(getNumberOfStudentsNotEnrolled(searchBean, curricularCourse, entry)
                        + searchResultBean.getNumberOfEnroledStudents());
            }
        }

        return result;
    }

    private static int getNumberOfStudentsNotEnrolled(MarkSheetManagementSearchBean searchBean,
            CurricularCourse curricularCourse, Entry<MarkSheetType, MarkSheetSearchResultBean> entry) {
        int studentsNotEnrolled =
                curricularCourse.getEnrolmentsNotInAnyMarkSheet(entry.getKey(), searchBean.getExecutionPeriod()).size();
        return studentsNotEnrolled;
    }

    private static void addToMap(Map<MarkSheetType, MarkSheetSearchResultBean> result, MarkSheet sheet) {
        getResultBeanForMarkSheetType(result, sheet.getMarkSheetType()).addMarkSheet(sheet);
    }

    private static MarkSheetSearchResultBean getResultBeanForMarkSheetType(Map<MarkSheetType, MarkSheetSearchResultBean> result,
            MarkSheetType sheetType) {
        MarkSheetSearchResultBean markSheetSearchResultBean = result.get(sheetType);
        if (markSheetSearchResultBean == null) {
            markSheetSearchResultBean = new MarkSheetSearchResultBean();
            result.put(sheetType, markSheetSearchResultBean);
        }
        return markSheetSearchResultBean;
    }

}
