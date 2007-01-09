package net.sourceforge.fenixedu.domain.student.curriculum;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;

public class EquivalanteCurriculumEntry extends CurriculumEntry {

    private final DomainReference<CurricularCourse> curricularCourseDomainReference;
    private final Set<SimpleCurriculumEntry> entries;

    public EquivalanteCurriculumEntry(final CurricularCourse curricularCourse, final Set<SimpleCurriculumEntry> simpleEntries) {
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

	@Override
	public double getEctsCredits() {
	    double ectsCredits = 0;
	    for (final SimpleCurriculumEntry simpleEntry : getEntries()) {
		ectsCredits += simpleEntry.getEctsCredits();
	    }
	    return ectsCredits;
	}

	@Override
	public double getWeigth() {
	    double result = 0;
	    for (final SimpleCurriculumEntry simpleEntry : getEntries()) {
		if (!simpleEntry.isNotNeedToEnrolEntry()) {
		    result += simpleEntry.getWeigth();    
		}
	    }
	    return result;
	}
	
	@Override
	public Double getWeigthTimesClassification() {
	    double result = 0;
	    for (final SimpleCurriculumEntry simpleEntry : getEntries()) {
		if (simpleEntry.getWeigthTimesClassification() != null) {
		    result += simpleEntry.getWeigthTimesClassification().doubleValue();
		}
	    }
	    
	    return result == 0 ? null : result;
	}

}
