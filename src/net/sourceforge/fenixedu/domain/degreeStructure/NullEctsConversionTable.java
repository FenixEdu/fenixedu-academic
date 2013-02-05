package net.sourceforge.fenixedu.domain.degreeStructure;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainObject;

public class NullEctsConversionTable implements IEctsConversionTable, Serializable {
    private static final long serialVersionUID = 1612946655483538738L;

    private final DomainObject target;

    private CycleType cycle;

    private CurricularYear curricularYear;

    public NullEctsConversionTable(DomainObject target) {
        this.target = target;
    }

    public NullEctsConversionTable(DomainObject target, CycleType cycle) {
        this(target);
        this.cycle = cycle;
    }

    public NullEctsConversionTable(DomainObject target, CurricularYear curricularYear) {
        this(target);
        this.curricularYear = curricularYear;
    }

    public NullEctsConversionTable(DomainObject target, CycleType cycle, CurricularYear curricularYear) {
        this(target);
        this.cycle = cycle;
        this.curricularYear = curricularYear;
    }

    @Override
    public DomainObject getTargetEntity() {
        return target;
    }

    @Override
    public EctsComparabilityTable getEctsTable() {
        return new NullEctsComparabilityTable();
    }

    @Override
    public EctsComparabilityPercentages getPercentages() {
        return new NullEctsComparabilityPercentages();
    }

    @Override
    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    @Override
    public CycleType getCycle() {
        return cycle;
    }
}
