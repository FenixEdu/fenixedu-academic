/*
 * Created on 24/Mar/2004
 *
 */

package ServidorPersistente;

import java.util.Date;

import Dominio.IDistributedTest;
import Dominio.IDistributedTestAdvisory;

/**
 * @author Susana Fernandes
 *  
 */
public interface IPersistentDistributedTestAdvisory extends IPersistentObject
{
	public void updateDistributedTestAdvisoryDates(IDistributedTest distributedTest, Date newExpiresDate)
			throws ExcepcaoPersistencia;

	public abstract void deleteByDistributedTest(IDistributedTest distributedTest)
			throws ExcepcaoPersistencia;

	public abstract void delete(IDistributedTestAdvisory distributedTestAdvisory)
			throws ExcepcaoPersistencia;
}