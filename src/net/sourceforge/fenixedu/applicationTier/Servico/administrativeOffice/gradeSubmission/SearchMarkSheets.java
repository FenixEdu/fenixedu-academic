/*
 * Created on May 4, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSearchResultBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Teacher;

public class SearchMarkSheets extends Service {
    
    public Map<MarkSheetType, MarkSheetSearchResultBean> run(MarkSheetManagementSearchBean searchBean)
            throws InvalidArgumentsServiceException {
        
        if (searchBean.getTeacherNumber() != null) {
            searchBean.setTeacher(Teacher.readByNumber(searchBean.getTeacherNumber()));
        }
        
        CurricularCourse curricularCourse = searchBean.getCurricularCourse();
        if (curricularCourse == null) {
            throw new InvalidArgumentsServiceException("error.noCurricularCourse");
        }
        
        Collection<MarkSheet> markSheets = curricularCourse.searchMarkSheets(
                searchBean.getExecutionPeriod(), searchBean.getTeacher(),
                searchBean.getEvaluationDate(), searchBean.getMarkSheetState(),
                searchBean.getMarkSheetType());
        
        Map<MarkSheetType, MarkSheetSearchResultBean> result = new TreeMap<MarkSheetType, MarkSheetSearchResultBean>();
        for (MarkSheet sheet : markSheets) {
            addToMap(result, sheet);
        }

        for (Entry<MarkSheetType, MarkSheetSearchResultBean> entry : result.entrySet()) {
            
            MarkSheetSearchResultBean searchResultBean = entry.getValue();
            searchResultBean.setShowStatistics(entry.getKey() != MarkSheetType.SPECIAL_AUTHORIZATION);
            if (searchResultBean.isShowStatistics()) {
                searchResultBean.setTotalNumberOfStudents(
                        getNumberOfStudentsNotEnrolled(searchBean, curricularCourse, entry) + 
                        searchResultBean.getNumberOfEnroledStudents());
            }
        }
        
        return result;
    }

    private int getNumberOfStudentsNotEnrolled(MarkSheetManagementSearchBean searchBean, CurricularCourse curricularCourse, Entry<MarkSheetType, MarkSheetSearchResultBean> entry) {
        int studentsNotEnrolled = curricularCourse.getEnrolmentsNotInAnyMarkSheet(entry.getKey(), searchBean.getExecutionPeriod()).size();
        return studentsNotEnrolled;
    }

    private void addToMap(Map<MarkSheetType, MarkSheetSearchResultBean> result, MarkSheet sheet) {
        getResultBeanForMarkSheetType(result, sheet.getMarkSheetType()).addMarkSheet(sheet);
    }

    private MarkSheetSearchResultBean getResultBeanForMarkSheetType(Map<MarkSheetType, MarkSheetSearchResultBean> result, MarkSheetType sheetType) {
        MarkSheetSearchResultBean markSheetSearchResultBean = result.get(sheetType);
        if(markSheetSearchResultBean == null) {
            markSheetSearchResultBean = new MarkSheetSearchResultBean();
            result.put(sheetType, markSheetSearchResultBean);
        }
        return markSheetSearchResultBean;
    }

}
