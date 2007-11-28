package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Grade;

public class EquivalanteCurriculumEntry extends CurriculumEntry {

    private final DomainReference<CurricularCourse> curricularCourseDomainReference;

    private final Set<SimpleCurriculumEntry> entries;

    public EquivalanteCurriculumEntry(final CurricularCourse curricularCourse,
	    final Set<SimpleCurriculumEntry> simpleEntries) {
	super();
	this.curricularCourseDomainReference = new DomainReference<CurricularCourse>(curricularCourse);
	this.entries = new HashSet<SimpleCurriculumEntry>(simpleEntries);
    }

    @Override
    public boolean isEquivalentEnrolmentEntry() {
	return true;
    }

    public CurricularCourse getCurricularCourse() {
	return curricularCourseDomainReference.getObject();
    }

    public Set<SimpleCurriculumEntry> getEntries() {
	return entries;
    }

    public BigDecimal getEctsCreditsForCurriculum() {
	BigDecimal result = BigDecimal.ZERO;
	for (final SimpleCurriculumEntry simpleEntry : getEntries()) {
	    result = result.add(simpleEntry.getEctsCreditsForCurriculum());
	}
	return result;
    }

    public BigDecimal getWeigthForCurriculum() {
	BigDecimal result = BigDecimal.ZERO;
	for (final SimpleCurriculumEntry simpleEntry : getEntries()) {
	    if (!simpleEntry.isNotNeedToEnrolEntry()) {
		result = result.add(simpleEntry.getWeigthForCurriculum());
	    }
	}
	return result;
    }

    @Override
    public BigDecimal getWeigthTimesGrade() {
	BigDecimal result = BigDecimal.ZERO;
	for (final SimpleCurriculumEntry simpleEntry : getEntries()) {
	    if (simpleEntry.getWeigthTimesGrade() != null) {
		result = result.add(simpleEntry.getWeigthTimesGrade());
	    }
	}

	return result.compareTo(BigDecimal.ZERO) == 0 ? null : result;
    }

    @Override
    public Grade getGrade() {
	return null;
    }

    public Integer getIdInternal() {
	return hashCode();
    }

}
