package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ReductionService extends ReductionService_Base {

    private static final BigDecimal MAX_CREDITS_REDUCTION = new BigDecimal(3);

    public ReductionService(final TeacherService teacherService, final BigDecimal creditsReduction) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	if (teacherService == null || creditsReduction == null) {
	    throw new DomainException("arguments can't be null");
	}
	if (creditsReduction.compareTo(MAX_CREDITS_REDUCTION) > 0) {
	    throw new DomainException("reduction can't be more than 3");
	}
	setTeacherService(teacherService);
	setCreditsReduction(creditsReduction);
    }
}
