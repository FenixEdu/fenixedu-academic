/*
 * Created on 10/Set/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDistributedTest;
import Dominio.IStudent;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestLog extends IPersistentObject {
	public abstract List readByStudentAndDistributedTest(
		IStudent student,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia;
}