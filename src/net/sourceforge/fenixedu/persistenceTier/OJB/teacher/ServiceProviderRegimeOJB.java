package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;

import org.apache.ojb.broker.query.Criteria;

public class ServiceProviderRegimeOJB extends PersistentObjectOJB implements
        IPersistentServiceProviderRegime {

    public ServiceProviderRegimeOJB() {
        super();
    }

    public ServiceProviderRegime readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacherId);
        return (ServiceProviderRegime) queryObject(ServiceProviderRegime.class, criteria);
    }

}