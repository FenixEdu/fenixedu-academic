package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class DegreeContext extends DegreeContext_Base {

    protected DegreeContext() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public DegreeContext(final DegreeCurricularPlan degreeCurricularPlan, final AcademicPeriod period,
            final AcademicInterval beginInterval, final AcademicInterval endInterval) {
        checkParameters(degreeCurricularPlan, period, beginInterval, endInterval);
        setBeginInterval(beginInterval);
        setEndInterval(endInterval);
        setPeriod(period);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan, AcademicPeriod period,
            AcademicInterval beginInterval, AcademicInterval endInterval) {
        if (degreeCurricularPlan == null || period == null || beginInterval == null) {
            throw new DomainException("error.degree.context.wrong.arguments");
        }
        // TODO verificar que beginInterval <= endInterval
        /*
         * if (endInterval != null && beginInterval.isA) { }
         */
    }

    @Deprecated
    public boolean hasPeriod() {
        return getPeriod() != null;
    }

    @Deprecated
    public boolean hasEndInterval() {
        return getEndInterval() != null;
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBeginInterval() {
        return getBeginInterval() != null;
    }

}
