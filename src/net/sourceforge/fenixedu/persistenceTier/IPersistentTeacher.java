/*
 * IPersistentTeacher.java
 */
package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author EP 15
 * @author Ivo Brandão
 */
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Teacher;

public interface IPersistentTeacher extends IPersistentObject {

    public Teacher readTeacherByUsername(String userName) throws ExcepcaoPersistencia;
   
    public Teacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia;

    public Collection<Teacher> readByNumbers(Collection<Integer> teacherNumbers)
            throws ExcepcaoPersistencia;
}