/*
 * Created on 12/Nov/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import DataBeans.person.InfoQualification;
import Dominio.IQualification;
import Dominio.Qualification;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualification implements IServico
{

	private static ReadQualification service = new ReadQualification();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadQualification getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadQualification()
	{
	}

	/**
	 * The name of the service
	 */
	public final String getNome()
	{
		return "ReadQualification";
	}

	/**
	 * Executes the service
	 * 
	 * @param managerPersonKey
	 *                    the identification of the person that is running the service
	 * @param infoQualification
	 *                    the create/edit qualification to be
	 */
	public IQualification run(Integer managerPersonkey, InfoQualification infoQualification)
		throws FenixServiceException
	{
		ISuportePersistente persistentSupport = null;
		IPersistentQualification persistentQualification = null;

		try
		{
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("Unable to dao factory!", e);
		}

		try
		{
			persistentQualification = persistentSupport.getIPersistentQualification();
			persistentSupport.iniciarTransaccao();
			
			//Try to read the qualification
			IQualification qualification = null;
			qualification =
				(IQualification) persistentQualification.readByOID(
					Qualification.class,
					infoQualification.getIdInternal());

			if (qualification == null) //The qualification doesn't exist
			{ 
				throw new FenixServiceException("Read an inexistent Qualification");
			}
			return qualification;

		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		} catch (Exception e)
		{
			throw new FenixServiceException(e.getMessage());
		}
	}
}
