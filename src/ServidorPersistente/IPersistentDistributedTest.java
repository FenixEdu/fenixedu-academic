/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDistributedTest;
import Dominio.IDomainObject;
import Dominio.IExecutionCourse;
import Dominio.IStudent;

/**
 * @author Susana Fernandes
 */
public interface IPersistentDistributedTest extends IPersistentObject
{
	public List readByTestScopeObject(IDomainObject object) throws ExcepcaoPersistencia;
	public List readByStudent(IStudent student) throws ExcepcaoPersistencia;
	public List readByStudentAndExecutionCourse(IStudent student, IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void delete(IDistributedTest distributedTest) throws ExcepcaoPersistencia;
}