package net.sourceforge.fenixedu.domain.executionCourse;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;

public class SummariesSearchBean implements Serializable {

    private final DomainReference<ExecutionCourse> executionCourseDomainReference;

    private ShiftType shiftType;
    private DomainReference<Shift> shiftDomainReference;
    private DomainReference<Professorship> professorshipDomainReference;
    private Boolean showOtherProfessors;

    public SummariesSearchBean(final ExecutionCourse executionCourse) {
        this.executionCourseDomainReference = new DomainReference<ExecutionCourse>(executionCourse);
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourseDomainReference.getObject();
    }

    public Professorship getProfessorship() {
        return professorshipDomainReference == null ? null : professorshipDomainReference.getObject();
    }
    public void setProfessorship(final Professorship professorship) {
        professorshipDomainReference = new DomainReference<Professorship>(professorship);
    }
    public Shift getShift() {
        return shiftDomainReference == null ? null : shiftDomainReference.getObject();
    }
    public void setShift(final Shift shift) {
        shiftDomainReference = new DomainReference<Shift>(shift);
    }
    public ShiftType getShiftType() {
        return shiftType;
    }
    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }
	public Boolean getShowOtherProfessors() {
		return showOtherProfessors;
	}
	public void setShowOtherProfessors(final Boolean showOtherProfessors) {
		this.showOtherProfessors = showOtherProfessors;
	}

    public SortedSet<Summary> search() {
        final SortedSet<Summary> summaries = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR);
        for (final Summary summary : getExecutionCourse().getAssociatedSummariesSet()) {
            final Shift shift = summary.getShift();
            if (getShift() == null || getShift() == shift) {
                if (getShiftType() == null || getShiftType() == shift.getTipo()) {
                	final Professorship professorship = summary.getProfessorship();
                	if (getProfessorship() == null && showOtherProfessors == null) {
                		summaries.add(summary);
                	} else if (professorship == null && showOtherProfessors != null && showOtherProfessors.booleanValue()) {
                		summaries.add(summary);
                	} else if (showOtherProfessors == null && ((getProfessorship() == null && professorship != null) || getProfessorship() == professorship)) {
                        summaries.add(summary);
                    }
                }
            }
        }
        return summaries;
    }

    public SortedSet<Summary> getSummaries() {
        return search();
    }

    public SortedSet<ShiftType> getShiftTypes() {
    	final SortedSet<ShiftType> shiftTypes = new TreeSet<ShiftType>();
    	for (final Shift shift : getExecutionCourse().getAssociatedShiftsSet()) {
    		shiftTypes.add(shift.getTipo());
    	}
    	return shiftTypes;
    }

}
