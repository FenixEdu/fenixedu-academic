/*
 * Created on 29/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
	
	public Boolean run(Integer studentGroupCode) throws FenixServiceException {
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

			IPersistentStudentGroup persistentStudentGroup = persistentSuport.getIPersistentStudentGroup();
			IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport.getIPersistentStudentGroupAttend();

			IStudentGroup deletedStudentGroup = (IStudentGroup) persistentStudentGroup.readByOId(new StudentGroup(studentGroupCode), false);
		
			if (deletedStudentGroup == null) {
				return new Boolean(true);
			}

			List studentGroupAttendList = persistentStudentGroupAttend.readAllByStudentGroup(deletedStudentGroup);
			if(studentGroupAttendList.size()!=0)
				return new Boolean(false);
			persistentStudentGroup.delete(deletedStudentGroup);
			persistentSuport.confirmarTransaccao();		
			return new Boolean(true);
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}

