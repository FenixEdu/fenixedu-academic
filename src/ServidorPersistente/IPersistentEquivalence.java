package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IEnrolment;
import Dominio.IEquivalence;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IPersistentEquivalence extends IPersistentObject {

		public void deleteAll() throws ExcepcaoPersistencia;
		public void lockWrite(IEquivalence enrolmentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
		public void delete(IEquivalence enrolment) throws ExcepcaoPersistencia;
		public IEquivalence readEquivalenceByEnrolmentAndEquivalentEnrolment(IEnrolment enrolment, IEnrolment equivalentEnrolment) throws ExcepcaoPersistencia;
		public ArrayList readAll() throws ExcepcaoPersistencia;
}