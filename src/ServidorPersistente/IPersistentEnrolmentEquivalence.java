package ServidorPersistente;

import java.util.List;

import Dominio.IEnrollment;
import Dominio.IEnrolmentEquivalence;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentEnrolmentEquivalence extends IPersistentObject
{

    public void delete(IEnrolmentEquivalence enrolment) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public IEnrolmentEquivalence readByEnrolment(IEnrollment enrolment) throws ExcepcaoPersistencia;
}