/*
 * Created on 12/Mai/2003
 *
 */
package ServidorPersistente;
import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;

/**
 * @author asnr and scpo
 *
 */
public interface IPersistentGroupProperties extends IPersistentObject{
	public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readAllGroupPropertiesByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	public void lockWrite(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
	public IGroupProperties readGroupPropertiesByExecutionCourseAndName(IDisciplinaExecucao executionCourse,String name) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
}
