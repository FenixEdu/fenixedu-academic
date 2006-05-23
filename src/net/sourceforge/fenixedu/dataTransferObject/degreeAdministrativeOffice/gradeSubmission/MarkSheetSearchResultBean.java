package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetState;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class MarkSheetSearchResultBean {
    
    private int totalNumberOfStudents;
    private List<MarkSheet> markSheets;
    
    public MarkSheetSearchResultBean() {
        markSheets = new ArrayList<MarkSheet>();  
    }

    public List<MarkSheet> getMarkSheets() {
        return markSheets;
    }
    
    public Collection<MarkSheet> getMarkSheetsSortedByEvaluationDate() {
        final String dateFormat = "dd/MM/yyyy";
        Collections.sort(getMarkSheets(), new Comparator<MarkSheet>() {
            public int compare(MarkSheet o1, MarkSheet o2) {
                return DateFormatUtil.compareDates(dateFormat, o1.getEvaluationDateDateTime().toDate(),
                        o2.getEvaluationDateDateTime().toDate()); 
            }
        });
        return getMarkSheets();
    }

    public void setMarkSheets(List<MarkSheet> markSheets) {
        this.markSheets = markSheets;
    }

    public void addMarkSheet(MarkSheet markSheet) {
        getMarkSheets().add(markSheet);
    }

    public int getTotalNumberOfEnroledStudents() {
        int totalNumberOfEnroledStudents = 0;
        for (MarkSheet markSheet : getMarkSheets()) {
            MarkSheetState markSheetState = markSheet.getMarkSheetState();
            if (markSheetState == MarkSheetState.RECTIFICATION || markSheetState == MarkSheetState.RECTIFICATION_NOT_CONFIRMED) {
                continue;
            }
            totalNumberOfEnroledStudents += markSheet.getEnrolmentEvaluationsCount();            
        }
        return totalNumberOfEnroledStudents;
    }

    public int getTotalNumberOfStudents() {
        return totalNumberOfStudents;
    }

    public void setTotalNumberOfStudents(int totalNumberStudents) {
        this.totalNumberOfStudents = totalNumberStudents;
    }

}
