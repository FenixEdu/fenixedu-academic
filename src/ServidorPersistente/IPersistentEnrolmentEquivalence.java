package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IPersistentEnrolmentEquivalence extends IPersistentObject {

	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(IEnrolmentEquivalence enrolmentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IEnrolmentEquivalence enrolment) throws ExcepcaoPersistencia;
	public ArrayList readAll() throws ExcepcaoPersistencia;
	public IEnrolmentEquivalence readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia;
}