package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddCandidateYearDelegateElections extends Service  {
	
	public void run(YearDelegateElection yearDelegateElection, Student student) throws FenixServiceException, ExcepcaoPersistencia {
		
		try {
			if(!yearDelegateElection.getCandidates().contains(student)) {
				yearDelegateElection.addCandidates(student);
			}
			else {
				throw new FenixServiceException("error.student.elections.candidacy.studentAlreadyCandidated");
			}
		} catch (DomainException ex) {
			throw new FenixServiceException(ex.getMessage(), ex.getArgs());
		}
	}

}
