package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;

import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.EnrolmentStateFilterType;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.ViewType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdCurriculumFilterOptions implements Serializable {

    static private final long serialVersionUID = 1L;

    private Registration registration;

    private StudentCurricularPlan selectedStudentCurricularPlan;
    private ViewType viewType;
    private EnrolmentStateFilterType enrolmentStateType;
    private OrganizationType organizationType;
    private Boolean detailed;

    public PhdCurriculumFilterOptions() {
	setViewType(ViewType.ALL);
	setEnrolmentStateType(EnrolmentStateFilterType.APPROVED_OR_ENROLED);
	setOrganizationType(OrganizationType.GROUPS);
	setDetailed(Boolean.FALSE);
    }

    public PhdCurriculumFilterOptions(final Registration registration) {
	this();
	setRegistration(registration);
	setSelectedStudentCurricularPlan(registration.getLastStudentCurricularPlan());
    }

    private Registration getRegistration() {
	return registration;
    }

    private void setRegistration(Registration registration) {
	this.registration = registration;
    }

    public StudentCurricularPlan getSelectedStudentCurricularPlan() {
	return selectedStudentCurricularPlan;
    }

    public void setSelectedStudentCurricularPlan(StudentCurricularPlan selectedStudentCurricularPlan) {
	this.selectedStudentCurricularPlan = selectedStudentCurricularPlan;
    }

    public List<StudentCurricularPlan> getStudentCurricularPlans() {
	if (getSelectedStudentCurricularPlan() == null) {
	    return getRegistration().getSortedStudentCurricularPlans();
	}
	return Collections.singletonList(getSelectedStudentCurricularPlan());
    }

    public ViewType getViewType() {
	return viewType;
    }

    public void setViewType(ViewType viewType) {
	this.viewType = viewType;
    }

    public EnrolmentStateFilterType getEnrolmentStateType() {
	return enrolmentStateType;
    }

    public void setEnrolmentStateType(EnrolmentStateFilterType enrolmentStateType) {
	this.enrolmentStateType = enrolmentStateType;
    }

    public OrganizationType getOrganizationType() {
	return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
	this.organizationType = organizationType;
    }

    public Boolean getDetailed() {
	return detailed;
    }

    public void setDetailed(Boolean detailed) {
	this.detailed = detailed;
    }

    static public class PhdStudentCurricularPlansFilterProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    final PhdCurriculumFilterOptions filter = (PhdCurriculumFilterOptions) source;
	    return filter.getRegistration().getSortedStudentCurricularPlans();
	}
    }
}
