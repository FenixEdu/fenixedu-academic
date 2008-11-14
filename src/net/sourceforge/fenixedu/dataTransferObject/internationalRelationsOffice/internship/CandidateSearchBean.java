package net.sourceforge.fenixedu.dataTransferObject.internationalRelationsOffice.internship;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;

import org.joda.time.LocalDate;

/**
 * @author Pedro Santos (pmrsa)
 */
public class CandidateSearchBean implements Serializable {
    private static final long serialVersionUID = 1170872005958537842L;

    private DomainReference<AcademicalInstitutionUnit> university;

    private LocalDate cutStart = new LocalDate(2008, 11, 10);

    private LocalDate cutEnd = new LocalDate(System.currentTimeMillis()).minusDays(1);

    public AcademicalInstitutionUnit getUniversity() {
	return (this.university != null) ? this.university.getObject() : null;
    }

    public void setUniversity(AcademicalInstitutionUnit university) {
	this.university = (university != null) ? new DomainReference<AcademicalInstitutionUnit>(university) : null;
    }

    public LocalDate getCutStart() {
	return cutStart;
    }

    public void setCutStart(LocalDate cutStart) {
	this.cutStart = cutStart;
    }

    public LocalDate getCutEnd() {
	return cutEnd;
    }

    public void setCutEnd(LocalDate cutEnd) {
	this.cutEnd = cutEnd;
    }

    public String getName() {
	StringBuilder output = new StringBuilder();
	if (getUniversity() != null) {
	    output.append(getUniversity().getName().replace(' ', '.'));
	} else {
	    output.append("todos");
	}
	output.append(".");
	if (getCutStart() != null) {
	    output.append(getCutStart().toString("dd.MM.yy"));
	}
	output.append("-");
	if (getCutEnd() != null) {
	    output.append(getCutEnd().toString("dd.MM.yy"));
	}
	return output.toString();
    }
}
