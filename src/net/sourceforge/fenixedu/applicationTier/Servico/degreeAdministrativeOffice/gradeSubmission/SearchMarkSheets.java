/*
 * Created on May 4, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

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

        // calculate total numbers
        for (Entry<MarkSheetType, MarkSheetSearchResultBean> entry : result.entrySet()) {
            MarkSheetSearchResultBean searchResultBean = entry.getValue();
            searchResultBean.setTotalNumberOfStudents(curricularCourse.getEnrolmentsNotInAnyMarkSheet(
                    entry.getKey(), searchBean.getExecutionPeriod()).size()
                    + searchResultBean.getTotalNumberOfEnroledStudents());
        }
        
        return result;
    }

    private void addToMap(Map<MarkSheetType, MarkSheetSearchResultBean> result, MarkSheet sheet) {
        MarkSheetSearchResultBean markSheetSearchResultBean = result.get(sheet.getMarkSheetType());
        if(markSheetSearchResultBean == null) {
            markSheetSearchResultBean = new MarkSheetSearchResultBean();
            result.put(sheet.getMarkSheetType(), markSheetSearchResultBean);
        }
        markSheetSearchResultBean.addMarkSheet(sheet);
    }

}
