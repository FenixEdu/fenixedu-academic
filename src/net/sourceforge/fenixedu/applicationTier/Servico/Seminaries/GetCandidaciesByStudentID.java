/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Ago/2003, 19:44:39
 * 
 */
public class GetCandidaciesByStudentID extends Service {

	public List run(final Person person) throws BDException, ExcepcaoPersistencia {
		final List candidaciesInfo = new LinkedList();

		final Student student = person.getStudent();
		for (final Registration registration : student.getRegistrationsSet()) {
		    for (final SeminaryCandidacy seminaryCandidacy : registration.getAssociatedCandidanciesSet()) {
			final InfoCandidacy infoCandidacy = InfoCandidacy.newInfoFromDomain(seminaryCandidacy);
			final Seminary seminary = seminaryCandidacy.getSeminary();
			infoCandidacy.setSeminaryName(seminary.getName());
			candidaciesInfo.add(infoCandidacy);
		    }
		}

		return candidaciesInfo;
	}

}