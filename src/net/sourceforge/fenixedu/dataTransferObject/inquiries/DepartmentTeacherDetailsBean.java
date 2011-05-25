package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class DepartmentTeacherDetailsBean extends GlobalCommentsResultsBean {

    private static final long serialVersionUID = 1L;
    private Person teacher;
    private ExecutionSemester executionSemester;
    private Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsToImproveMap;

    public DepartmentTeacherDetailsBean(Person teacher, ExecutionSemester executionSemester, Person president,
	    InquiryGlobalComment globalComment, boolean backToResume) {
	super(null, president, globalComment, backToResume);
	setTeacher(teacher);
	setExecutionSemester(executionSemester);
	initTeacherResults(teacher);
    }

    private void initTeacherResults(Person teacher) {
	setTeachersResultsMap(new HashMap<Professorship, List<TeacherShiftTypeResultsBean>>());
	setTeachersResultsToImproveMap(new HashMap<Professorship, List<TeacherShiftTypeResultsBean>>());
	for (Professorship teacherProfessorship : getProfessorships()) {
	    ArrayList<TeacherShiftTypeResultsBean> teachersResults = new ArrayList<TeacherShiftTypeResultsBean>();
	    List<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResults();
	    if (!professorshipResults.isEmpty()) {
		for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		    List<InquiryResult> teacherShiftResults = teacherProfessorship.getInquiryResults(shiftType);
		    if (!teacherShiftResults.isEmpty()) {
			teachersResults.add(new TeacherShiftTypeResultsBean(teacherProfessorship, shiftType, teacherProfessorship
				.getExecutionCourse().getExecutionPeriod(), teacherShiftResults, teacher, getPersonCategory()));
		    }
		}
		Collections.sort(teachersResults, new BeanComparator("professorship.person.name"));
		Collections.sort(teachersResults, new BeanComparator("shiftType"));
		if (teacherProfessorship.hasResultsToImprove()) {
		    getTeachersResultsToImproveMap().put(teacherProfessorship, teachersResults);
		} else {
		    getTeachersResultsMap().put(teacherProfessorship, teachersResults);
		}
	    }
	}
    }

    public List<InquiryResultComment> getAllTeacherComments() {
	List<InquiryResultComment> commentsMade = getExecutionSemester().getAuditCommentsMadeOnTeacher(getTeacher());
	Collections.sort(commentsMade, new BeanComparator("person.name"));
	return commentsMade;
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

    public void setTeachersResultsToImproveMap(Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsToImproveMap) {
	this.teachersResultsToImproveMap = teachersResultsToImproveMap;
    }

    public Map<Professorship, List<TeacherShiftTypeResultsBean>> getTeachersResultsToImproveMap() {
	return teachersResultsToImproveMap;
    }
}
