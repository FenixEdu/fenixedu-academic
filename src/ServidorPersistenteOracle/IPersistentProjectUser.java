/*
 * Created on Jan 12, 2005
 *
 */
package ServidorPersistenteOracle;

import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProjectUser {
    public abstract Integer getUserCoordId(Integer userCode) throws ExcepcaoPersistencia;
}