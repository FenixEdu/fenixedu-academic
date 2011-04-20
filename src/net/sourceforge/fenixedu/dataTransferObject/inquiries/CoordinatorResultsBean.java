package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class CoordinatorResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsMap;
    private List<BlockResultsSummaryBean> curricularBlockResults;
    private Person coordinator;
    private ExecutionCourse executionCourse;
    private ExecutionDegree executionDegree;
    private InquiryResultComment inquiryResultComment;
    private InquiryGlobalComment inquiryGlobalComment;
    private String comment;

    public CoordinatorResultsBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person coordinator,
	    InquiryGlobalComment globalComment) {
	setCoordinator(coordinator);
	setExecutionCourse(executionCourse);
	setExecutionDegree(executionDegree);
	initCurricularBlocksResults(executionCourse, executionDegree, coordinator);
	initTeachersResults(executionCourse, coordinator);
	initResultComment(coordinator, globalComment);
    }

    private void initResultComment(Person coordinator, InquiryGlobalComment globalComment) {
	setInquiryGlobalComment(globalComment);
	if (globalComment != null) {
	    for (InquiryResultComment inquiryResultComment : globalComment.getInquiryResultComments()) {
		if (inquiryResultComment.getPerson() == coordinator) {
		    setInquiryResultComment(inquiryResultComment);
		    setComment(inquiryResultComment.getComment());
		}
	    }
	}
    }

    private void initCurricularBlocksResults(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person person) {
	CurricularCourseInquiryTemplate courseInquiryTemplate = CurricularCourseInquiryTemplate
		.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
	setCurricularBlockResults(new ArrayList<BlockResultsSummaryBean>());
	List<InquiryResult> results = executionCourse.getInquiryResultsByExecutionDegreeAndForTeachers(executionDegree);
	if (results != null && results.size() > 5) {
	    for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocks()) {
		getCurricularBlockResults().add(
			new BlockResultsSummaryBean(inquiryBlock, results, person, ResultPersonCategory.DEGREE_COORDINATOR));
	    }
	}
	Collections.sort(getCurricularBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    private void initTeachersResults(ExecutionCourse executionCourse, Person person) {
	setTeachersResultsMap(new HashMap<Professorship, List<TeacherShiftTypeResultsBean>>());
	for (Professorship teacherProfessorship : executionCourse.getProfessorships()) {
	    ArrayList<TeacherShiftTypeResultsBean> teachersResults = new ArrayList<TeacherShiftTypeResultsBean>();
	    List<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResults();
	    if (!professorshipResults.isEmpty()) {
		for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		    List<InquiryResult> teacherShiftResults = teacherProfessorship.getInquiryResults(shiftType);
		    if (!teacherShiftResults.isEmpty()) {
			teachersResults.add(new TeacherShiftTypeResultsBean(teacherProfessorship, shiftType, teacherProfessorship
				.getExecutionCourse().getExecutionPeriod(), teacherShiftResults, person,
				ResultPersonCategory.DEGREE_COORDINATOR));
		    }
		}
		Collections.sort(teachersResults, new BeanComparator("professorship.person.name"));
		Collections.sort(teachersResults, new BeanComparator("shiftType"));
		getTeachersResultsMap().put(teacherProfessorship, teachersResults);
	    }
	}
    }

    private Set<ShiftType> getShiftTypes(List<InquiryResult> professorshipResults) {
	Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
	for (InquiryResult inquiryResult : professorshipResults) {
	    shiftTypes.add(inquiryResult.getShiftType());
	}
	return shiftTypes;
    }

    @Service
    public void saveComment() {
	if (!StringUtils.isEmpty(getComment())) {
	    if (getInquiryGlobalComment() != null) {
		if (getInquiryResultComment() != null) {
		    getInquiryResultComment().setComment(getComment());
		} else {
		    new InquiryResultComment(getInquiryGlobalComment(), getCoordinator(),
			    ResultPersonCategory.DEGREE_COORDINATOR,
			    getInquiryGlobalComment().getInquiryResultCommentsCount() + 1, getComment());
		}
	    } else {
		InquiryGlobalComment inquiryGlobalComment = new InquiryGlobalComment(getExecutionCourse(), getExecutionDegree());
		new InquiryResultComment(inquiryGlobalComment, getCoordinator(), ResultPersonCategory.DEGREE_COORDINATOR, 1,
			getComment());
	    }
	} else if (getInquiryResultComment() != null) {
	    getInquiryResultComment().setComment(getComment());
	}
    }

    public void setTeachersResultsMap(Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsMap) {
	this.teachersResultsMap = teachersResultsMap;
    }

    public Map<Professorship, List<TeacherShiftTypeResultsBean>> getTeachersResultsMap() {
	return teachersResultsMap;
    }

    public List<BlockResultsSummaryBean> getCurricularBlockResults() {
	return curricularBlockResults;
    }

    public void setCurricularBlockResults(List<BlockResultsSummaryBean> curricularBlockResults) {
	this.curricularBlockResults = curricularBlockResults;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree;
    }

    public void setCoordinator(Person coordinator) {
	this.coordinator = coordinator;
    }

    public Person getCoordinator() {
	return coordinator;
    }

    public void setInquiryResultComment(InquiryResultComment inquiryResultComment) {
	this.inquiryResultComment = inquiryResultComment;
    }

    public InquiryResultComment getInquiryResultComment() {
	return inquiryResultComment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getComment() {
	return comment;
    }

    public void setInquiryGlobalComment(InquiryGlobalComment inquiryGlobalComment) {
	this.inquiryGlobalComment = inquiryGlobalComment;
    }

    public InquiryGlobalComment getInquiryGlobalComment() {
	return inquiryGlobalComment;
    }
}
