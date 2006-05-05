/*
 * Created on May 4, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.MarkSheet;

public class MarkSheetSearchResultBean {
    
    //TODO: 
    private int totalNumberOfEnroledStudents;
    private int totalNumberStudents;
    private boolean showTotalNumbers;
    
    private Collection<MarkSheet> markSheets;
    
    public MarkSheetSearchResultBean() {
        markSheets = new HashSet<MarkSheet>();
    }

    public Collection<MarkSheet> getMarkSheets() {
        return markSheets;
    }

    public void setMarkSheets(Collection<MarkSheet> markSheets) {
        this.markSheets = markSheets;
    }

    public void addMarkSheet(MarkSheet markSheet) {
        getMarkSheets().add(markSheet);
    }

}
