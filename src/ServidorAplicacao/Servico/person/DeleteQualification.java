/*
 * Created on 11/Nov/2003
 */

package ServidorAplicacao.Servico.person;

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

public class DeleteQualification implements IServico {

	private static DeleteQualification service = new DeleteQualification();

	/**
	 * The singleton access method of this class.
	 */
	public static DeleteQualification getService() {
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private DeleteQualification() {
	}

	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "DeleteQualification";
	}

	/**
	 * Executes the service
	 */
	public void run(
		Integer managerPersonkey,
		InfoQualification infoQualification)
		throws FenixServiceException {

		ISuportePersistente persistentSupport = null;
		IPersistentQualification persistentQualification = null;

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("Unable to dao factory!", e);
		}

		try {
			if(infoQualification.getIdInternal() == null)
			{
				throw new FenixServiceException("Qualification id is NULL!");
			}
			
			persistentQualification =
				persistentSupport.getIPersistentQualification();

			persistentSupport.iniciarTransaccao();
			
			persistentQualification.deleteByOID(Qualification.class, infoQualification.getIdInternal());

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e.getMessage());
		} catch (Exception e) {
			throw new FenixServiceException(e.getMessage());
		}
	}
}
