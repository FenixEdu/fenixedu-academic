/*
 * IPersistentTeacher.java
 */
package ServidorPersistente;
/**
 *
 * @author  EP 15
 * @author Ivo Brandão
 */
import java.util.List;

import Dominio.ITeacher;
public interface IPersistentTeacher {
	
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia;
    public ITeacher readTeacherByNumber(Integer teacherNumber) throws ExcepcaoPersistencia;
    
    public List readResponsableForExecutionCourses(String owner) throws ExcepcaoPersistencia;
    public List readResponsableForExecutionCourses(Integer teacherNumber)throws ExcepcaoPersistencia;
    public List readProfessorShipsExecutionCourses(String whoTeaches) throws ExcepcaoPersistencia;
    public List readProfessorShipsExecutionCourses(Integer teacherNumber)throws ExcepcaoPersistencia;
    
    public void lockWrite(ITeacher teacher) throws ExcepcaoPersistencia;
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
    
}
