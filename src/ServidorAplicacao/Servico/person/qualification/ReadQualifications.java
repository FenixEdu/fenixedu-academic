/*
 * Created on 12/Nov/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import java.util.Collection;
import java.util.List;

import DataBeans.person.InfoQualification;
import DataBeans.util.Cloner;
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

public class ReadQualifications implements IServico
{

	private static ReadQualifications service = new ReadQualifications();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadQualifications getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadQualifications()
	{
	}

	/**
	 * The name of the service
	 */
	public final String getNome()
	{
		return "ReadQualifications";
	}

	/**
	 * Executes the service
	 */
	public Collection run(Integer managerPersonkey, InfoQualification infoQualification)
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

			//Reads the qualifications
			List qualifications = null;
			qualifications =
				persistentQualification.readQualificationsByPerson(
					Cloner.copyInfoPerson2IPerson(infoQualification.getPersonInfo()));
			
			return qualifications;

		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		} catch (Exception e)
		{
			throw new FenixServiceException(e.getMessage());
		}
	}
}
