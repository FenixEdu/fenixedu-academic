/*
 * Created on 29/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadStudentGroupsByShift implements IServico{
	
	private static ReadStudentGroupsByShift servico = new ReadStudentGroupsByShift();
	
	/**
	* The singleton access method of this class.
	**/
			
	public static ReadStudentGroupsByShift getService() {
		return servico;
	}

	/**
	* The actor of this class.
	**/
	private ReadStudentGroupsByShift() {
	}

	/**
	* Returns service name 
	* */
	public final String getNome() {
		return "ReadStudentGroupsByShift";
	}

	public List run(Integer shiftCode, Integer groupPropertiesCode) {
	
		List studentGroupsList = null;
		try 
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
	
			IGroupProperties groupProperties =(IGroupProperties)sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);
			ITurno shift = (ITurno) sp.getITurnoPersistente().readByOId(new Turno(shiftCode),false);

				
			studentGroupsList = sp.getIPersistentStudentGroup().readAllStudentGroupByGroupPropertiesAndShift(groupProperties,shift);
				 
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		  }
				
		List studentGroupNumbersList = new ArrayList(studentGroupsList.size());
		Iterator iter = studentGroupsList.iterator();
		while(iter.hasNext())
			studentGroupNumbersList.add(((IStudentGroup) iter.next()).getGroupNumber());				
			
		return studentGroupNumbersList;
			
	}

}
