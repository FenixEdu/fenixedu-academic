/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentProfessorship extends IPersistentObject {

	public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
	public IProfessorship readByTeacherAndExecutionCourse(
		ITeacher teacher,
		IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
	public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
						throws ExcepcaoPersistencia;
	public void delete(IProfessorship professorship) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public void deleteByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	public void deleteByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
	public void lockWrite(IProfessorship professorship)
				throws ExcepcaoPersistencia, ExistingPersistentException;
	
	

}
