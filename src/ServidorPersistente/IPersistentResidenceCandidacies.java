/*
 * Created on Aug 4, 2004
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IStudent;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentResidenceCandidacies extends IPersistentObject {

    public List readResidenceCandidaciesByStudent(IStudent student) throws ExcepcaoPersistencia;

}