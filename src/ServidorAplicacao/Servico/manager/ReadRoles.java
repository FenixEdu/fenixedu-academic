package ServidorAplicacao.Servico.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoDegree;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.IRole;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */

public class ReadRoles implements IServico
{

	private static ReadRoles service = new ReadRoles();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadRoles getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadRoles()
	{
	}

	/**
	 * Service name
	 */
	public final String getNome()
	{
		return "ReadRoles";
	}

	/**
	 * Executes the service. Returns the current infodegree.
	 */
	public List run() throws FenixServiceException
	{
		List result = null;

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			result = (List) CollectionUtils.collect(sp.getIPersistentRole().readAll(), new Transformer()
			{
				public Object transform(Object arg0)
				{
					return Cloner.copyIRole2InfoRole((IRole) arg0);
				}
			});
		} catch (ExcepcaoPersistencia excepcaoPersistencia)
		{
			throw new FenixServiceException(excepcaoPersistencia);
		}

		return result;
	}
}