/*
 * Created on Jan 17, 2005
 *
 */
package ServidorPersistenteOracle;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentRubric {
    public abstract List getRubricList(String rubricTableName) throws ExcepcaoPersistencia;
}