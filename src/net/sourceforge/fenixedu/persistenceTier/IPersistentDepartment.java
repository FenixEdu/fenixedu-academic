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

    public IDepartment lerDepartamentoPorNome(String nome) throws ExcepcaoPersistencia;

    public IDepartment lerDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia;

    public List readAllDepartments() throws ExcepcaoPersistencia;

    public IDepartment readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
    
    public IDepartment readByEmployee(IEmployee employee) throws ExcepcaoPersistencia;
}