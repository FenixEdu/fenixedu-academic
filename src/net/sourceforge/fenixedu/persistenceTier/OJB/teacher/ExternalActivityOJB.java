package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;

import org.apache.ojb.broker.query.Criteria;

public class ExternalActivityOJB extends PersistentObjectOJB implements IPersistentExternalActivity {

    public ExternalActivityOJB() {
        super();
    }

    public List readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyOrientationTeacher", teacherId);
        List externalActivities = queryList(ExternalActivity.class, criteria);

        return externalActivities;
    }

}