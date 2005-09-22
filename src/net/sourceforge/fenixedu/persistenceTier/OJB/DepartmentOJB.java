/*
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;

import org.apache.ojb.broker.query.Criteria;

public class DepartmentOJB extends PersistentObjectOJB implements IPersistentDepartment {

    public List readAll() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(Department.class, crit);
    }

    public IDepartment readByName(String name) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);  
        return (IDepartment) queryObject(Department.class, criteria);                
    }
}
