/*
 * Created on 25/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadTeachersByExecutionCourseProfessorship implements IServico {
	private static ReadTeachersByExecutionCourseProfessorship service = new ReadTeachersByExecutionCourseProfessorship();

	 /**
	  * The singleton access method of this class.
	  */
	 public static ReadTeachersByExecutionCourseProfessorship getService() {
	   return service;
	 }
	/**
	 * 
	 */
	public ReadTeachersByExecutionCourseProfessorship() {
		
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		
		return "ReadTeachersByExecutionCourseProfessorship";
	}
	
	public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
		try {
			List result= null;
			ISuportePersistente sp;
			sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao executionCourse =
						Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
			IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
			result = persistentProfessorship.readByExecutionCourse(executionCourse);
		
			List infoResult = new ArrayList();
			if (result!=null){
			
			Iterator iter = result.iterator();	
			while(iter.hasNext()) {
				IProfessorship professorship = (IProfessorship) iter.next();
				ITeacher teacher = professorship.getTeacher();
				InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
				infoResult.add(infoTeacher);				
			}
			return infoResult;
			}
			else {
				return result;
			}
					
				} catch (ExcepcaoPersistencia e) {
					throw new FenixServiceException(e);
				} 

	}
}
