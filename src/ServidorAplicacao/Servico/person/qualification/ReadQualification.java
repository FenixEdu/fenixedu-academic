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

public class ReadQualification implements IServico {

	private static ReadQualification service = new ReadQualification();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadQualification getService() {
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadQualification() {
	}

	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "ReadQualification";
	}

	/**
	 * Executes the service
	 */
	public IQualification run(
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
			persistentQualification =
				persistentSupport.getIPersistentQualification();

			persistentSupport.iniciarTransaccao();

			IQualification qualification = null;
			qualification =
				(IQualification) persistentQualification.readByOID(
					Qualification.class,
					infoQualification.getIdInternal());

			if (qualification == null) { //Erro, a qualificação a ler não existe
				throw new FenixServiceException("Read an inexistent Qualification");
			}
			return qualification;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e.getMessage());
		} catch (Exception e) {
			throw new FenixServiceException(e.getMessage());
		}
	}
}
