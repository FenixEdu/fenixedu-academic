/*
 * CurricularCourseOJB.java
 *
 * Created on 28 of December 2002, 12:02
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;

public class CurricularCourseOJB extends ObjectFenixOJB implements IPersistentCurricularCourse {
    
    public CurricularCourseOJB() {
    }
    
    public boolean deleteAllCurricularCourse() {
        try {
            String oqlQuery = "select all from " + CurricularCourse.class.getName();
            super.deleteAll(oqlQuery);
            return true;
        } catch(ExcepcaoPersistencia ex){
            return false;
        }
    }
    
    public ICurricularCourse readCurricularCourseByNameCode(String name, String code){
        try {
            ICurricularCourse curricularCourse = null;
            String oqlQuery = "select all from " + CurricularCourse.class.getName();
            oqlQuery += " where name = $1";
            oqlQuery += " and code = $2";
            query.create(oqlQuery);
            query.bind(name);
            query.bind(code);
            
            //query.bind(new Integer(chaveDisciplina));
            List result = (List) query.execute();
            try{
                lockRead(result);
            } catch (ExcepcaoPersistencia ex) {
                return null;
            }
            if (result.size() != 0)
                curricularCourse = (ICurricularCourse) result.get(0);
            return curricularCourse;
        } catch (QueryException ex) {
            return null;
        }
    }
    
    public boolean writeCurricularCourse(ICurricularCourse curricularCourse) {
        try {
            super.lockWrite(curricularCourse);
            return true;
        } catch(ExcepcaoPersistencia ex){
            return false;
        }
    }

    public boolean deleteCurricularCourse(ICurricularCourse curricularCourse) {
        try {
            super.delete(curricularCourse);
            return true;
        } catch(ExcepcaoPersistencia ex){
            return false;
        }
    }
    
    public ArrayList readAllCurricularCourses() {
        try {
            ArrayList list = new ArrayList();
            String oqlQuery = "select all from " + CurricularCourse.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            try{
                lockRead(result);
            } catch (ExcepcaoPersistencia ex) {
                return null;
            }
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    list.add((ICurricularCourse)iterator.next());
            }
            return list;
        } catch (QueryException ex) {
            return null;
        }
    }
}
