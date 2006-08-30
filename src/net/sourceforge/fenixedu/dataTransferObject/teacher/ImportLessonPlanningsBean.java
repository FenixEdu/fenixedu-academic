package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;

public class ImportLessonPlanningsBean extends ImportContentBean implements Serializable {

    private ImportType importType;

    public ImportType getImportType() {
        return importType;
    }

    public void setImportType(ImportType importType) {
        this.importType = importType;
    }    
    
    public static enum ImportType{
        
        SUMMARIES,
        
        PLANNING;
        
        public String getName() {
            return name();
        }           
    }
    
}
