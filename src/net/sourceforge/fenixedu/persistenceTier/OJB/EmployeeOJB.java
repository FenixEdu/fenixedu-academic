/*
 * Created on 12/Jul/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class EmployeeOJB extends PersistentObjectOJB implements IPersistentEmployee {

    public Employee readByNumber(Integer number) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("employeeNumber", number);
        return (Employee) queryObject(Employee.class, criteria);
    }

    public Employee readByIdInternal(int idInternal) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", new Integer(String.valueOf(idInternal)));
        return (Employee) queryObject(Employee.class, criteria);
    }

    public Employee readByPerson(int keyPerson) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", new Integer(String.valueOf(keyPerson)));
        return (Employee) queryObject(Employee.class, criteria);
    }

    public Employee readByPerson(Person person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", person.getIdInternal());
        return (Employee) queryObject(Employee.class, criteria);
    }
}