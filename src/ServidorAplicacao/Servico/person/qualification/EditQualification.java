/*
 * Created on 07/11/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import DataBeans.person.InfoQualification;
import DataBeans.util.Cloner;
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

public class EditQualification implements IServico
{

	private static EditQualification service = new EditQualification();

	/**
	 * The singleton access method of this class.
	 */
	public static EditQualification getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private EditQualification()
	{
	}

	/**
	 * The name of the service
	 */
	public final String getNome()
	{
		return "EditQualification";
	}

	/**
	 * Method that returns a qualification that exists in the database. If the qualification does not
	 * exists returns a empty IQualification object.
	 * 
	 * @param info
	 *                    a infoQualification with the information that is going to be read
	 * @param persistenQualification
	 *                    persistent object for executing the query
	 */
	private IQualification checkIfQualificationExists(
		InfoQualification infoQualification,
		IPersistentQualification persistentQualification)
		throws FenixServiceException
	{
		IQualification qualification = null;
		try
		{
			qualification =
				(IQualification) persistentQualification.readByOID(
					Qualification.class,
					infoQualification.getIdInternal());
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}
		return qualification;
	}

	/**
	 * Executes the service
	 * 
	 * @param managerPersonKey
	 *                    the identification of the person that is running the service
	 * @param infoQualification
	 *                    the create/edit qualification to be
	 */
	public IQualification run(Integer managerPersonKey, InfoQualification infoQualification)
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

			//Check if qualification exists in the database
			IQualification qualification =
				checkIfQualificationExists(infoQualification, persistentQualification);

			//next 2lines are necessary due to a possible OJB lock problem
			persistentSupport.confirmarTransaccao();
			persistentSupport.iniciarTransaccao();

			if (qualification == null) //Lets create a qualification
			{
				qualification = new Qualification();
			}

			//Change the OJB object
			persistentQualification.simpleLockWrite(qualification);

			if (infoQualification.getMark() != null)
				qualification.setMark(infoQualification.getMark());

			if (infoQualification.getPersonInfo() != null)
				qualification.setPerson(
					Cloner.copyInfoPerson2IPerson(infoQualification.getPersonInfo()));

			if (infoQualification.getSchool() != null)
				qualification.setSchool(infoQualification.getSchool());

			if (infoQualification.getTitle() != null)
				qualification.setTitle(infoQualification.getTitle());

			if (infoQualification.getYear() != null)
				qualification.setYear(infoQualification.getYear());

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
