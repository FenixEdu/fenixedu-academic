package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

public class DepartmentTeacherDetailsBean extends GlobalCommentsResultsBean {

    private static final long serialVersionUID = 1L;
    private Person teacher;
    private ExecutionSemester executionSemester;

    public DepartmentTeacherDetailsBean(Person teacher, ExecutionSemester executionSemester, Person president,
	    InquiryGlobalComment globalComment, boolean backToResume) {
	super(null, president, globalComment, backToResume);
	setTeacher(teacher);
	setExecutionSemester(executionSemester);
	initTeachersResults(teacher);
    }

    @Override
    protected InquiryGlobalComment createGlobalComment() {
	return new InquiryGlobalComment(getTeacher(), getExecutionSemester());
    }

    @Override
    protected ResultPersonCategory getPersonCategory() {
	return ResultPersonCategory.DEPARTMENT_PRESIDENT;
    }

    protected List<Professorship> getProfessorships() {
	if (getTeacher() == null) {
	    return new ArrayList<Professorship>();
	}
	return getTeacher().getProfessorships(getExecutionSemester());
    }

    public void setTeacher(Person teacher) {
	this.teacher = teacher;
    }

    public Person getTeacher() {
	return teacher;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }
}
