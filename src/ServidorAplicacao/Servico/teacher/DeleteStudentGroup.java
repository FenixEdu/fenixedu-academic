/*
 * Created on 29/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr and scpo
 *
 */
public class DeleteStudentGroup implements IServico {
	private static DeleteStudentGroup service = new DeleteStudentGroup();
	public static DeleteStudentGroup getService() {
		return service;
	}
	
	private DeleteStudentGroup() {
	}
	
	public final String getNome() {
		return "DeleteStudentGroup";
	}
	
	public void run(Integer studentGroupCode) throws FenixServiceException {
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

			IPersistentStudentGroup persistentStudentGroup = persistentSuport.getIPersistentStudentGroup();
			IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport.getIPersistentStudentGroupAttend();

			IStudentGroup deletedStudentGroup = (IStudentGroup) persistentStudentGroup.readByOId(new StudentGroup(studentGroupCode), false);
		
			if (deletedStudentGroup == null) {
				return;
			}

			List studentGroupAttendList = persistentStudentGroupAttend.readAllByStudentGroup(deletedStudentGroup);
			if(studentGroupAttendList.size()!=0)
				throw new ExistingServiceException();
			
			persistentStudentGroup.delete(deletedStudentGroup);
			persistentSuport.confirmarTransaccao();		
			
		} 
		catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}

