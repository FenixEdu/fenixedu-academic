package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.PHDProgramCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class CandidacyFactory {

    public static Candidacy newCandidacy(DegreeType degreeType, Person person, ExecutionDegree executionDegree, YearMonthDay startDate) throws DomainException {
	
	switch(degreeType) {
	case BOLONHA_PHD_PROGRAM:
	    return new PHDProgramCandidacy(person, executionDegree);
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return new DFACandidacy(person, executionDegree, startDate);
	
	default:
	    throw new DomainException("error.candidacyFactory.invalid.degree.type");
	}
    }
}
