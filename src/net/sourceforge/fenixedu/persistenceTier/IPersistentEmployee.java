/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;

/**
 * @author jpvl
 */
public interface IPersistentEmployee extends IPersistentObject {
    public Employee readByNumber(Integer number) throws ExcepcaoPersistencia;

    public Employee readByIdInternal(int idInternal) throws ExcepcaoPersistencia;

    public Employee readByPerson(int keyPerson) throws ExcepcaoPersistencia;

    public Employee readByPerson(Person person) throws ExcepcaoPersistencia;

}