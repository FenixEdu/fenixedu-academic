package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.ICreditsInScientificArea;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInSpecificScientificArea;

import org.apache.ojb.broker.query.Criteria;

public class CreditsInSpecificScientificAreaOJB extends PersistentObjectOJB implements
        IPersistentCreditsInSpecificScientificArea {
    public CreditsInSpecificScientificAreaOJB() {
    }

    public ICreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
            Integer studentCurricularPlanKey, Integer enrolmentKey,
            Integer scientificAreaKey) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanKey);
        criteria.addEqualTo("enrolment.idInternal", enrolmentKey);
        criteria.addEqualTo("scientificArea.idInternal", scientificAreaKey);
        return (ICreditsInScientificArea) queryObject(CreditsInScientificArea.class, criteria);
    }
}