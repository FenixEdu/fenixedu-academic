package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class DepartmentTeacherResultsResume implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TeacherShiftTypeGroupsResumeResult> teacherShiftTypeGroupsResumeResults;
    private Teacher teacher;
    private Person president;
    private ResultPersonCategory personCategory;
    private boolean backToResume;

    public DepartmentTeacherResultsResume(Teacher teacher, Person president, ResultPersonCategory personCategory,
	    boolean backToResume) {
	setTeacher(teacher);
	setPresident(president);
	setPersonCategory(personCategory);
	setBackToResume(backToResume);
    }

    @Override
    public int hashCode() {
	return getTeacher().hashCode();
    }

    public void addTeacherShiftTypeGroupsResumeResult(TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult) {
	if (getTeacherShiftTypeGroupsResumeResults() == null) {
	    setTeacherShiftTypeGroupsResumeResults(new ArrayList<TeacherShiftTypeGroupsResumeResult>());
	}
	getTeacherShiftTypeGroupsResumeResults().add(teacherShiftTypeGroupsResumeResult);
    }

    public void setTeacherShiftTypeGroupsResumeResults(
	    List<TeacherShiftTypeGroupsResumeResult> teacherShiftTypeGroupsResumeResults) {
	this.teacherShiftTypeGroupsResumeResults = teacherShiftTypeGroupsResumeResults;
    }

    public List<TeacherShiftTypeGroupsResumeResult> getTeacherShiftTypeGroupsResumeResults() {
	return teacherShiftTypeGroupsResumeResults;
    }

    public List<TeacherShiftTypeGroupsResumeResult> getOrderedTeacherShiftResumes() {
	Collections.sort(getTeacherShiftTypeGroupsResumeResults(), new BeanComparator("professorship.executionCourse.name"));
	Collections.sort(getTeacherShiftTypeGroupsResumeResults(), new BeanComparator("shiftType"));
	return getTeacherShiftTypeGroupsResumeResults();
    }

    public Collection<List<TeacherShiftTypeGroupsResumeResult>> getTeacherShiftResumesByUC() {
	Map<Professorship, List<TeacherShiftTypeGroupsResumeResult>> result = new HashMap<Professorship, List<TeacherShiftTypeGroupsResumeResult>>();
	for (TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult : getOrderedTeacherShiftResumes()) {
	    List<TeacherShiftTypeGroupsResumeResult> teacherShiftTypeGroupsResumeResultList = result
		    .get(teacherShiftTypeGroupsResumeResult.getProfessorship());
	    if (teacherShiftTypeGroupsResumeResultList == null) {
		teacherShiftTypeGroupsResumeResultList = new ArrayList<TeacherShiftTypeGroupsResumeResult>();
		result.put(teacherShiftTypeGroupsResumeResult.getProfessorship(), teacherShiftTypeGroupsResumeResultList);
	    }
	    teacherShiftTypeGroupsResumeResultList.add(teacherShiftTypeGroupsResumeResult);
	}
	return result.values();
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher;
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public Person getPresident() {
	return president;
    }

    public void setPresident(Person president) {
	this.president = president;
    }

    public ResultPersonCategory getPersonCategory() {
	return personCategory;
    }

    public void setPersonCategory(ResultPersonCategory personCategory) {
	this.personCategory = personCategory;
    }

    public void setBackToResume(boolean backToResume) {
	this.backToResume = backToResume;
    }

    public boolean isBackToResume() {
	return backToResume;
    }
}
