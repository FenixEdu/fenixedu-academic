/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProjectUser {
    public IRubric readProjectCoordinator(Integer userCode) throws ExcepcaoPersistencia;

    public abstract Integer getUserCoordId(Integer userCode) throws ExcepcaoPersistencia;
}