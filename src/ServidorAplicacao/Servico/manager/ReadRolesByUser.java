package ServidorAplicacao.Servico.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.IRole;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */

public class ReadRolesByUser implements IServico
{

	private static ReadRolesByUser service = new ReadRolesByUser();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadRolesByUser getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadRolesByUser()
	{
	}

	/**
	 * Service name
	 */
	public final String getNome()
	{
		return "ReadRolesByUser";
	}

	/**
	 * Executes the service. Returns the current infodegree.
	 */
	public List run(String username) throws FenixServiceException
	{
		List result = null;

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
			if (person == null)
			{
				throw new FenixServiceException("error.noUsername");
			}
			
			result = (List) CollectionUtils.collect(person.getPersonRoles(), new Transformer()
			{
				public Object transform(Object arg0)
				{
					return Cloner.copyIRole2InfoRole((IRole) arg0);
				}
			});
		}
		catch (ExcepcaoPersistencia excepcaoPersistencia)
		{
			throw new FenixServiceException("error.noRoles", excepcaoPersistencia);
		}

		return result;
	}
}