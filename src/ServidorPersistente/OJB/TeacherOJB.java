/*
 * TeacherOJB.java
 */
package ServidorPersistente.OJB;
import java.util.ArrayList;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
/**
 *
 * @author  EP 15
 * @author Ivo Brandão
 */
public class TeacherOJB extends ObjectFenixOJB implements IPersistentTeacher {
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where username = $1";
            
            query.create(oqlQuery);
            query.bind(user);
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0){
                teacher = (ITeacher) result.get(0);
            }
            
            return teacher;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public ITeacher readTeacherByNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where teacherNumber = $1";
            
            query.create(oqlQuery);
            query.bind(teacherNumber);
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0){
                teacher = (ITeacher) result.get(0);
            }
            
            return teacher;
            
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public void lockWrite(ITeacher teacher) throws ExcepcaoPersistencia {
        super.lockWrite(teacher);
    }
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia {
        super.delete(teacher);
    }
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Teacher.class.getName();
        super.deleteAll(oqlQuery);
    }
    public List readResponsableForExecutionCourses(String whoOwns) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where username = $1";

            query.create(oqlQuery);
            query.bind(whoOwns);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
            return teacher.getResponsableForExecutionCourses();
            
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public List readResponsableForExecutionCourses(Integer teacherNumber) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where teacherNumber = $1";
            query.create(oqlQuery);
            query.bind(teacherNumber);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
			return teacher.getResponsableForExecutionCourses();
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
     public List readProfessorShipsExecutionCourses(String whoTeaches) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where username = $1";
            query.create(oqlQuery);
            query.bind(whoTeaches);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
            return teacher.getProfessorShipsExecutionCourses();
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public List readProfessorShipsExecutionCourses(Integer teacherNumber) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where teacherNumber = $1";
            query.create(oqlQuery);
            query.bind(teacherNumber);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
            return teacher.getProfessorShipsExecutionCourses();
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Teacher.class.getName();
            
			query.create(oqlQuery);
            
			List result = (List) query.execute();
			lockRead(result);
            
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
    
}
