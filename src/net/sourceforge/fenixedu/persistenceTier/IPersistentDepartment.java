/*
 * Department.java
 * 
 * Created on 25 de Agosto de 2002, 0:53
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Department;

public interface IPersistentDepartment extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;
    
    public Department readByName(String name) throws ExcepcaoPersistencia;
}