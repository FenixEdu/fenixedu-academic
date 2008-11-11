package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class StudentInquiriesTeachingResult extends StudentInquiriesTeachingResult_Base {

    public StudentInquiriesTeachingResult() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isUnsatisfactory() {
	return getUnsatisfactoryResultsAssiduity() || getUnsatisfactoryResultsAuditable()
		|| getUnsatisfactoryResultsPedagogicalCapacity() || getUnsatisfactoryResultsPresencialLearning()
		|| getUnsatisfactoryResultsStudentInteraction();
    }

    @Override
    public Boolean getUnsatisfactoryResultsAssiduity() {
	return super.getUnsatisfactoryResultsAssiduity() != null && super.getUnsatisfactoryResultsAssiduity();
    }

    @Override
    public Boolean getUnsatisfactoryResultsAuditable() {
	return super.getUnsatisfactoryResultsAuditable() != null && super.getUnsatisfactoryResultsAuditable();
    }

    @Override
    public Boolean getUnsatisfactoryResultsPedagogicalCapacity() {
	return super.getUnsatisfactoryResultsPedagogicalCapacity() != null && super.getUnsatisfactoryResultsPedagogicalCapacity();
    }

    @Override
    public Boolean getUnsatisfactoryResultsPresencialLearning() {
	return super.getUnsatisfactoryResultsPresencialLearning() != null && super.getUnsatisfactoryResultsPresencialLearning();
    }

    @Override
    public Boolean getUnsatisfactoryResultsStudentInteraction() {
	return super.getUnsatisfactoryResultsStudentInteraction() != null && super.getUnsatisfactoryResultsStudentInteraction();
    }

    @Override
    public Boolean getInternalDegreeDisclosure() {
	return super.getInternalDegreeDisclosure() != null && super.getInternalDegreeDisclosure();
    }
}
