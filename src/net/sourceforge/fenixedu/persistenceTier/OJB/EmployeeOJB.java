/*
 * Created on 12/Jul/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class EmployeeOJB extends PersistentObjectOJB implements IPersistentEmployee {

    public IEmployee readByNumber(Integer number) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("employeeNumber", number);
        return (IEmployee) queryObject(Employee.class, criteria);
    }

    public IEmployee readByIdInternal(int idInternal) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", new Integer(String.valueOf(idInternal)));
        return (IEmployee) queryObject(Employee.class, criteria);
    }

    public IEmployee readByPerson(int keyPerson) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", new Integer(String.valueOf(keyPerson)));
        return (IEmployee) queryObject(Employee.class, criteria);
    }

    public IEmployee readByPerson(IPerson person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", person.getIdInternal());
        return (IEmployee) queryObject(Employee.class, criteria);
    }
}