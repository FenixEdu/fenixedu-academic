/*
 * Created on 12/Mai/2003
 *
 */
package ServidorPersistente;
import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;

/**
 * @author asnr and scpo
 *
 */
public interface IPersistentGroupProperties extends IPersistentObject{
	public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
    public List readAllGroupPropertiesByExecutionCourseID(Integer id) throws ExcepcaoPersistencia;
	
	public List readAllGroupPropertiesByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;
	public void lockWrite(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
	public IGroupProperties readGroupPropertiesByExecutionCourseAndName(IExecutionCourse executionCourse,String name) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
}
