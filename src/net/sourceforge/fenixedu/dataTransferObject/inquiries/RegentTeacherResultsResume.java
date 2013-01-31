package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.beanutils.BeanComparator;

public class RegentTeacherResultsResume implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TeacherShiftTypeGroupsResumeResult> teacherShiftTypeGroupsResumeResults;
	private Professorship professorship;

	public RegentTeacherResultsResume(Professorship professorship) {
		setProfessorship(professorship);
	}

	@Override
	public int hashCode() {
		return getProfessorship().hashCode();
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
		Collections.sort(getTeacherShiftTypeGroupsResumeResults(), new BeanComparator("shiftType"));
		return getTeacherShiftTypeGroupsResumeResults();
	}

	public void setProfessorship(Professorship professorship) {
		this.professorship = professorship;
	}

	public Professorship getProfessorship() {
		return professorship;
	}
}
