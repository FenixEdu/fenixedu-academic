/*
 * Created on 17/Jul/2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;

/**
 * @author dcs-rjao 17/Jul/2003
 */
public interface IPersistentEquivalentEnrolmentForEnrolmentEquivalence extends IPersistentObject
{

    public void delete(IEquivalentEnrolmentForEnrolmentEquivalence enrolmentEquivalenceRestriction)
            throws ExcepcaoPersistencia;

    public IEquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
            IEnrolmentEquivalence enrolmentEquivalence, IEnrolment equivalentEnrolment)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readByEquivalentEnrolment(IEnrolment equivalentEnrolment) throws ExcepcaoPersistencia;
    public List readByEnrolmentEquivalence(IEnrolmentEquivalence enrolmentEquivalence) throws ExcepcaoPersistencia;
}