/*
 * Created on Jan 11, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.sql.PreparedStatement;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentSuportOracle {
	public abstract void startTransaction() throws ExcepcaoPersistencia;

	public abstract void commitTransaction() throws ExcepcaoPersistencia;

	public abstract void cancelTransaction() throws ExcepcaoPersistencia;

	public abstract PreparedStatement prepareStatement(String statement) throws ExcepcaoPersistencia;

}