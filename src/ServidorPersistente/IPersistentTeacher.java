/*
 * IPersistentTeacher.java
 */
package ServidorPersistente;
/**
 *
 * @author  EP 15
 */
import java.util.List;

import Dominio.ITeacher;
public interface IPersistentTeacher {
	
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia;
    public ITeacher readTeacherByNumber(Integer teacherNumber) throws ExcepcaoPersistencia;
    public List readOwnedSites(String owner) throws ExcepcaoPersistencia;
    public List readOwnedSites(Integer teacherNumber)throws ExcepcaoPersistencia;
    public List readProfessorShipsSites(String whoTeaches) throws ExcepcaoPersistencia;
    public List readProfessorShipsSites(Integer teacherNumber)throws ExcepcaoPersistencia;
    public void lockWrite(ITeacher teacher) throws ExcepcaoPersistencia;
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
    
}
