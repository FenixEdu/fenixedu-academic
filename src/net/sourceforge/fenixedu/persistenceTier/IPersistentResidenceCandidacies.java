/*
 * Created on Aug 4, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentResidenceCandidacies extends IPersistentObject {

    public List readResidenceCandidaciesByStudent(IStudent student) throws ExcepcaoPersistencia;

}