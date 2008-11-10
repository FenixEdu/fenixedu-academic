package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class StudentInquiriesCourseResult extends StudentInquiriesCourseResult_Base {

    public StudentInquiriesCourseResult() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isUnsatisfactory() {
	return getUnsatisfactoryResultsCUEvaluation() || getUnsatisfactoryResultsCUOrganization();
    }

    @Override
    public Boolean getUnsatisfactoryResultsCUEvaluation() {
	return super.getUnsatisfactoryResultsCUEvaluation() != null && super.getUnsatisfactoryResultsCUEvaluation();
    }

    @Override
    public Boolean getUnsatisfactoryResultsCUOrganization() {
	return super.getUnsatisfactoryResultsCUOrganization() != null && super.getUnsatisfactoryResultsCUOrganization();
    }

}
