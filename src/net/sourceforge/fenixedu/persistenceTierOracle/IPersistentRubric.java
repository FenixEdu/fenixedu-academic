/*
 * Created on Jan 17, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentRubric {
    public abstract List getRubricList(String rubricTableName) throws ExcepcaoPersistencia;
}