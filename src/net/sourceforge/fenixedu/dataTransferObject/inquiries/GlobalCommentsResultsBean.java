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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public abstract class GlobalCommentsResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsMap;
    private Person person;
    private ExecutionCourse executionCourse;
    private InquiryResultComment inquiryResultComment;
    private InquiryGlobalComment inquiryGlobalComment;
    private String comment;
    private boolean backToResume;

    protected abstract ResultPersonCategory getPersonCategory();

    protected abstract InquiryGlobalComment createGlobalComment();

    public GlobalCommentsResultsBean(ExecutionCourse executionCourse, Person person, InquiryGlobalComment globalComment,
	    boolean backToResume) {
	setExecutionCourse(executionCourse);
	setPerson(person);
	initTeachersResults(person);
	initResultComment(person, globalComment);
	setBackToResume(backToResume);
    }

    private void initResultComment(Person person, InquiryGlobalComment globalComment) {
	setInquiryGlobalComment(globalComment);
	if (globalComment != null) {
	    for (InquiryResultComment inquiryResultComment : globalComment.getInquiryResultComments()) {
		if (inquiryResultComment.getPerson() == person
			&& getPersonCategory().equals(inquiryResultComment.getPersonCategory())) {
		    setInquiryResultComment(inquiryResultComment);
		    setComment(inquiryResultComment.getComment());
		}
	    }
	}
    }

    protected void initTeachersResults(Person person) {
	setTeachersResultsMap(new HashMap<Professorship, List<TeacherShiftTypeResultsBean>>());
	for (Professorship teacherProfessorship : getProfessorships()) {
	    ArrayList<TeacherShiftTypeResultsBean> teachersResults = new ArrayList<TeacherShiftTypeResultsBean>();
	    List<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResults();
	    if (!professorshipResults.isEmpty()) {
		for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		    List<InquiryResult> teacherShiftResults = teacherProfessorship.getInquiryResults(shiftType);
		    if (!teacherShiftResults.isEmpty()) {
			teachersResults.add(new TeacherShiftTypeResultsBean(teacherProfessorship, shiftType, teacherProfessorship
				.getExecutionCourse().getExecutionPeriod(), teacherShiftResults, person, getPersonCategory()));
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

    protected List<Professorship> getProfessorships() {
	return getExecutionCourse().getProfessorships();
    }

    @Service
    public void saveComment() {
	if (!StringUtils.isEmpty(getComment())) {
	    if (getInquiryGlobalComment() != null) {
		if (getInquiryResultComment() != null) {
		    getInquiryResultComment().setComment(getComment());
		} else {
		    new InquiryResultComment(getInquiryGlobalComment(), getPerson(), getPersonCategory(),
			    getInquiryGlobalComment().getInquiryResultCommentsCount() + 1, getComment());
		}
	    } else {
		InquiryGlobalComment inquiryGlobalComment = createGlobalComment();
		new InquiryResultComment(inquiryGlobalComment, getPerson(), getPersonCategory(), 1, getComment());
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

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public Person getPerson() {
	return person;
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

    public void setBackToResume(boolean backToResume) {
	this.backToResume = backToResume;
    }

    public boolean isBackToResume() {
	return backToResume;
    }
}
