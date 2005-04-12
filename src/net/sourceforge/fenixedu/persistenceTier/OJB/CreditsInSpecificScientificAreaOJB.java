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

    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        return queryList(CreditsInScientificArea.class, criteria);
    }

    public ICreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
            IStudentCurricularPlan studentCurricularPlan, IEnrolment enrolment,
            IScientificArea scientificArea) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        criteria.addEqualTo("scientificArea.idInternal", scientificArea.getIdInternal());
        return (ICreditsInScientificArea) queryObject(CreditsInScientificArea.class, criteria);
    }
}