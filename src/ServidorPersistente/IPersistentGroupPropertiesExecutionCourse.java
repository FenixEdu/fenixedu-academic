/*
 * Created on 17/Ago/2004
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;

/**
 * @author joaosa & rmalo
 */
public interface IPersistentGroupPropertiesExecutionCourse extends IPersistentObject{

    public IGroupPropertiesExecutionCourse readBy(IGroupProperties groupProperties, IExecutionCourse executionCourse)
    	throws ExcepcaoPersistencia;

    public IGroupPropertiesExecutionCourse readByIDs(Integer groupPropertiesID,Integer executionCourseID)
    	throws ExcepcaoPersistencia;
    
    public List readAllByGroupProperties(IGroupProperties groupProperties) 
    	throws ExcepcaoPersistencia;

	public List readAll() throws ExcepcaoPersistencia;

	public void delete(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) 
		throws ExcepcaoPersistencia;


	public List readAllByExecutionCourse(IExecutionCourse executionCourse) 
		throws ExcepcaoPersistencia;

	public List readByGroupPropertiesId(Integer id) throws ExcepcaoPersistencia;

	public List readByExecutionCourseId(Integer id) throws ExcepcaoPersistencia;
		
}
