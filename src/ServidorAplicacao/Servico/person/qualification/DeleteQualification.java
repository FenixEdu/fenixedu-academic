/*
 * Created on 11/Nov/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import DataBeans.person.InfoQualification;
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

public class DeleteQualification implements IServico
{

	private static DeleteQualification service = new DeleteQualification();

	/**
	 * The singleton access method of this class.
	 */
	public static DeleteQualification getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private DeleteQualification()
	{
	}

	/**
	 * The name of the service
	 */
	public final String getNome()
	{
		return "DeleteQualification";
	}

	/**
	 * Executes the service
	 * 
	 * @param managerPersonKey
	 *                    the identification of the person that is running the service
	 * @param infoQualification
	 *                    the deleted qualification to be
	 */
	public void run(Integer managerPersonKey, InfoQualification infoQualification)
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
			//If the qualification to be deleted is not defined the service returns an error
			if (infoQualification.getIdInternal() == null)
			{
				throw new FenixServiceException("DeleteQualification service: Qualification id is null!");
			}

			//Deleting the qualification
			persistentQualification = persistentSupport.getIPersistentQualification();
			persistentSupport.iniciarTransaccao();
			persistentQualification.deleteByOID(Qualification.class, infoQualification.getIdInternal());

		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		} catch (Exception e)
		{
			throw new FenixServiceException(e.getMessage());
		}
	}
}
