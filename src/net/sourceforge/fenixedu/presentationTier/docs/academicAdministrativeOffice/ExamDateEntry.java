package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

public class ExamDateEntry {
    private String curricularCourseName;

    private String examDate;
    
    public ExamDateEntry(){
	
    }

    public ExamDateEntry(final String curricularCourseName, final String examDate) {
	this.curricularCourseName = curricularCourseName;
	this.examDate = examDate;

    }

    public String getCurricularCourseName() {
	return curricularCourseName;
    }

    public void setCurricularCourseName(String curricularCourseName) {
	this.curricularCourseName = curricularCourseName;
    }

    public String getExamDate() {
	return examDate;
    }

    public void setExamDate(String examDate) {
	this.examDate = examDate;
    }

}
