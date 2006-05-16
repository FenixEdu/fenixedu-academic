/*
 * Created on May 4, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSearchResultBean;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Teacher;

public class SearchMarkSheets extends Service {
    
    public Map<MarkSheetType, MarkSheetSearchResultBean> run(MarkSheetManagementSearchBean searchBean) {
        
        if (searchBean.getTeacherNumber() != null) {
            searchBean.setTeacher(Teacher.readByNumber(searchBean.getTeacherNumber()));
        }
        Collection<MarkSheet> markSheets = searchBean.getCurricularCourse().searchMarkSheets(
                searchBean.getExecutionPeriod(), searchBean.getTeacher(),
                searchBean.getEvaluationDate(), searchBean.getMarkSheetState(),
                searchBean.getMarkSheetType());
        
        Map<MarkSheetType, MarkSheetSearchResultBean> result = new TreeMap<MarkSheetType, MarkSheetSearchResultBean>();
        for (MarkSheet sheet : markSheets) {
            addToMap(result, sheet);
        }
        
        // TODO: calculate values
        
        
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
