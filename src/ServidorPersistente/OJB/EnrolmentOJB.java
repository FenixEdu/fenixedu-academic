/*
 * EnrolmentOJB.java
 *
 * Created on 20 de Outubro de 2002, 17:47
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import Dominio.Enrolment;
import Dominio.IEnrolment;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;

public class EnrolmentOJB extends ObjectFenixOJB implements IPersistentEnrolment {
    

    public void delete(IEnrolment inscricao) throws ExcepcaoPersistencia {
        super.delete(inscricao);
    }
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Enrolment.class.getName();
        super.deleteAll(oqlQuery);
    }
    
}
