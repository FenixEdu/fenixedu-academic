/*
 * Created on 21/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;




import java.util.Iterator;
import java.util.List;

import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public class EditStudentGroupShift implements IServico {

	private IPersistentStudentGroup persistentStudentGroup = null;
	
	private static EditStudentGroupShift service = new EditStudentGroupShift();

	/**
		* The singleton access method of this class.
		*/
	public static EditStudentGroupShift getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditStudentGroupShift() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "EditStudentGroupShift";
	}

	
	/**
	 * Executes the service.
	 */

	public Boolean run(Integer studentGroupCode,Integer newShiftCode)
		throws FenixServiceException {

		System.out.println("ENTRA NO SERVICO EDIT STUDENT GROUP SHIFT");
		ITurnoPersistente persistentShift = null;
		IPersistentStudentGroup persistentStudentGroup = null;
		
		try 
		{
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
	
			persistentShift = persistentSupport.getITurnoPersistente();
			ITurno shift = (ITurno) persistentShift.readByOId(new Turno(newShiftCode),false);
								
			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOId(new StudentGroup(studentGroupCode),false);
			
			List studentGroupList = persistentStudentGroup.readAllStudentGroupByGroupPropertiesAndShift(studentGroup.getGroupProperties(),shift);
			
			IStudentGroup existingStudentGroup = new StudentGroup();
			Iterator iter = studentGroupList.iterator();
			while(iter.hasNext())
			{
				existingStudentGroup = (IStudentGroup)iter.next();
				if(existingStudentGroup.getGroupNumber().equals(studentGroup.getGroupNumber()))
				{
					return new Boolean(false);
				}			
			}
			
						
			studentGroup.setShift(shift);
			persistentStudentGroup.lockWrite(studentGroup);
							
				
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());			
		  }
		
		
		return new Boolean(true);
	}
}
