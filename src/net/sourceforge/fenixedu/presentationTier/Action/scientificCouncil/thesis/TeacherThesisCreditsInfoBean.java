package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;

public class TeacherThesisCreditsInfoBean implements Serializable{

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private DomainReference<Student> student;
    private String dissertationTitle;
    private ThesisParticipationType teacherParticipationType;
    private Integer creditsPercentage;
    
    public TeacherThesisCreditsInfoBean (Student student, String dissertationTitle, ThesisParticipationType teacherParticipationType,
    		Integer creditsPercentage) {
    	
    		setStudent(student);
    		setDissertationTitle(dissertationTitle);
    		setTeacherParticipationType(teacherParticipationType);
    		setCreditsPercentage(creditsPercentage);
    }

	public DomainReference<Student> getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = new DomainReference<Student>(student);
	}

	public String getDissertationTitle() {
		return dissertationTitle;
	}

	public void setDissertationTitle(String dissertationTitle) {
		this.dissertationTitle = dissertationTitle;
	}

	public ThesisParticipationType getTeacherParticipationType() {
		return teacherParticipationType;
	}

	public void setTeacherParticipationType(
			ThesisParticipationType teacherParticipationType) {
		this.teacherParticipationType = teacherParticipationType;
	}

	public Integer getCreditsPercentage() {
		return creditsPercentage;
	}

	public void setCreditsPercentage(Integer creditsPercentage) {
		this.creditsPercentage = creditsPercentage;
	}


}
