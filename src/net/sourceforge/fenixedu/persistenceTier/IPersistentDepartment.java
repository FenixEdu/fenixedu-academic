/*
 * IDepartment.java
 * 
 * Created on 25 de Agosto de 2002, 0:53
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.ITeacher;

public interface IPersistentDepartment extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public IDepartment readByTeacher(Integer teacherId) throws ExcepcaoPersistencia;
    
    public IDepartment readByEmployee(Integer employeeId) throws ExcepcaoPersistencia;
}