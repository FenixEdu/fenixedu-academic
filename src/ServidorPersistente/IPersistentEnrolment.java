/*
 * IPersistentEnrolment.java
 *
 * Created on 20 de Outubro de 2002, 17:45
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import Dominio.IEnrolment;
;

public interface IPersistentEnrolment extends IPersistentObject {
    public void delete(IEnrolment inscricao) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
