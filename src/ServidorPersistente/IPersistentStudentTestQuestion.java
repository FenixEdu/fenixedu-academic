/*
 * Created on 27/Ago/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestQuestion extends IPersistentObject {
	public abstract List readByStudentAndDistributedTest(
		IStudent student,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia;
	public List readByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia;
	public List readByStudent(IStudent student) throws ExcepcaoPersistencia;
	public void deleteByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia;
	public void delete(IStudentTestQuestion studentTestQuestion)
		throws ExcepcaoPersistencia;
}