/*
 * Created on Jan 12, 2005
 *
 */
package ServidorPersistenteOracle;

import Dominio.projectsManagement.IRubric;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProjectUser {
    public IRubric readProjectCoordinator(Integer userCode) throws ExcepcaoPersistencia;

    public abstract Integer getUserCoordId(Integer userCode) throws ExcepcaoPersistencia;
}