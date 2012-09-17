package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ReductionService extends ReductionService_Base {

    private static final BigDecimal MAX_CREDITS_REDUCTION = new BigDecimal(3);

    public ReductionService(final TeacherService teacherService, final BigDecimal creditsReduction) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	if (teacherService == null) {
	    throw new DomainException("arguments can't be null");
	}
	setTeacherService(teacherService);
	setCreditsReduction(creditsReduction);
    }

    @Override
    public void setCreditsReduction(BigDecimal creditsReduction) {
	if (creditsReduction == null || creditsReduction.compareTo(BigDecimal.ZERO) < 0) {
	    creditsReduction = BigDecimal.ZERO;
	}
	if (creditsReduction.compareTo(MAX_CREDITS_REDUCTION) > 0) {
	    throw new DomainException("label.creditsReduction.exceededMaxAllowed");
	}
	super.setCreditsReduction(creditsReduction);
    }
}
