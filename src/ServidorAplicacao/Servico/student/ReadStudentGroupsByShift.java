/*
 * Created on 29/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
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

	public List run(String name, Integer shiftCode, Integer executionCourseCode) {
	
		List studentGroupsList = null;
		try 
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
	
			IDisciplinaExecucao executionCourse =(IDisciplinaExecucao) sp.getIDisciplinaExecucaoPersistente().readByOId(new DisciplinaExecucao(executionCourseCode), false);
			ITurno shift = (ITurno) sp.getITurnoPersistente().readByOId(new Turno(shiftCode),false);
			IGroupProperties groupProperties = sp.getIPersistentGroupProperties().readGroupPropertiesByExecutionCourseAndName(executionCourse,name);
				
			studentGroupsList = sp.getIPersistentStudentGroup().readAllStudentGroupByGroupPropertiesAndShift(groupProperties,shift);
				 
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		  }
				
		List infoStudentGroupsList = new ArrayList(studentGroupsList.size());
		Iterator iter = studentGroupsList.iterator();
		while(iter.hasNext())
			infoStudentGroupsList.add(Cloner.copyIStudentGroup2InfoStudentGroup((IStudentGroup) iter.next()));				
			
		return infoStudentGroupsList;
			
	}

}
