/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentResponsibleFor extends IPersistentObject {
	public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
	public IResponsibleFor readByTeacherAndExecutionCourse(
		ITeacher teacher,
		IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
	public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
	public void delete(IResponsibleFor responsibleFor)
		throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll()throws ExcepcaoPersistencia;
	public void lockWrite(IResponsibleFor responsibleFor)
		throws ExcepcaoPersistencia, ExistingPersistentException;
}
