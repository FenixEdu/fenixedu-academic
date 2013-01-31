package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;

import org.apache.commons.beanutils.BeanComparator;

public class CompetenceCourseResultsResume implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<CurricularCourseResumeResult> curricularCourseResumeResults;
	private CompetenceCourse competenceCourse;

	public CompetenceCourseResultsResume(CompetenceCourse competenceCourse) {
		setCompetenceCourse(competenceCourse);
	}

	@Override
	public int hashCode() {
		return getCompetenceCourse().hashCode();
	}

	public void addCurricularCourseResumeResult(CurricularCourseResumeResult curricularCourseResumeResult) {
		if (getCurricularCourseResumeResults() == null) {
			setCurricularCourseResumeResults(new ArrayList<CurricularCourseResumeResult>());
		}
		getCurricularCourseResumeResults().add(curricularCourseResumeResult);
	}

	public List<CurricularCourseResumeResult> getOrderedCurricularCourseResumes() {
		Collections.sort(getCurricularCourseResumeResults(), new BeanComparator("firstPresentationName"));
		return getCurricularCourseResumeResults();
	}

	public void setCurricularCourseResumeResults(List<CurricularCourseResumeResult> curricularCourseResumeResults) {
		this.curricularCourseResumeResults = curricularCourseResumeResults;
	}

	public List<CurricularCourseResumeResult> getCurricularCourseResumeResults() {
		return curricularCourseResumeResults;
	}

	public void setCompetenceCourse(CompetenceCourse competenceCourse) {
		this.competenceCourse = competenceCourse;
	}

	public CompetenceCourse getCompetenceCourse() {
		return competenceCourse;
	}
}
