/*
 * IPersistentTeacher.java
 */
package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author EP 15
 * @author Ivo Brandão
 */
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.ITeacher;

public interface IPersistentTeacher extends IPersistentObject {

    /**
     * @param user
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @throws ExcepcaoPersistencia
     */
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia;

    /**
     * @throws ExcepcaoPersistencia
     */
    public void deleteAll() throws ExcepcaoPersistencia;

    /**
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readAll() throws ExcepcaoPersistencia;

    /**
     * @param department
     * @return
     */
    public List readByDepartment(IDepartment department) throws ExcepcaoPersistencia;

    /**
     * Reads a teacher with the number passed by argument.
     * 
     * @param teacherNumber
     * @return ITeacher or null if there's no teacher with that number
     * @throws ExcepcaoPersistencia
     *             if there's something wrong in database
     * @author jpvl
     */
    public ITeacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia;
       
    /**
     * Reads a collection of teachers, wich numbers are in the passed param teacherNumbers
     * 
     * @param teacherNumbers
     * @return
     * @throws ExcepcaoPersistencia
     */
    public Collection<ITeacher> readByNumbers(Collection<Integer> teacherNumbers) throws ExcepcaoPersistencia;
}