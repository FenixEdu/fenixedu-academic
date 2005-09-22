/*
 * IPersistentTeacher.java
 */
package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author EP 15
 * @author Ivo Brandão
 */
import java.util.Collection;

import net.sourceforge.fenixedu.domain.ITeacher;

public interface IPersistentTeacher extends IPersistentObject {

    public ITeacher readTeacherByUsername(String userName) throws ExcepcaoPersistencia;
   
    public ITeacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia;

    public Collection<ITeacher> readByNumbers(Collection<Integer> teacherNumbers)
            throws ExcepcaoPersistencia;
}