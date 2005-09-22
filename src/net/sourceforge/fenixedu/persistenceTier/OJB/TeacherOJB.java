/*
 * TeacherOJB.java
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author EP 15
 * @author Ivo Brandão
 *  
 */
public class TeacherOJB extends ObjectFenixOJB implements IPersistentTeacher {
    
    public ITeacher readTeacherByUsername(String userName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", userName);
        return (ITeacher) queryObject(Teacher.class, criteria);
    }

    public ITeacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacherNumber", teacherNumber);
        return (ITeacher) queryObject(Teacher.class, criteria);
    }
    
    public Collection<ITeacher> readByNumbers(Collection<Integer> teacherNumbers) throws ExcepcaoPersistencia {
        if(teacherNumbers.isEmpty()){
            return new ArrayList<ITeacher>();
        }
        
        Criteria criteria = new Criteria();
        criteria.addIn("teacherNumber",teacherNumbers);
        return queryList(Teacher.class, criteria);
    }
    
}