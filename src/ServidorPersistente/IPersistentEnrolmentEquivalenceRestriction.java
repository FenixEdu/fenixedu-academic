/*
 * Created on 17/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEquivalenceRestriction;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 17/Jul/2003
 */
public interface IPersistentEnrolmentEquivalenceRestriction extends IPersistentObject {
	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(IEnrolmentEquivalenceRestriction enrolmentEquivalenceRestrictionToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IEnrolmentEquivalenceRestriction enrolmentEquivalenceRestriction) throws ExcepcaoPersistencia;
	public IEnrolmentEquivalenceRestriction readByEnrolmentEquivalenceAndEquivalentEnrolment(IEnrolmentEquivalence enrolmentEquivalence, IEnrolment equivalentEnrolment) throws ExcepcaoPersistencia;
	public ArrayList readAll() throws ExcepcaoPersistencia;
}