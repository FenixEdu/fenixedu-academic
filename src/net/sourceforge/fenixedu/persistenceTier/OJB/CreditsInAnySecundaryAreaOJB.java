package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.ICreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInAnySecundaryArea;

import org.apache.ojb.broker.query.Criteria;

public class CreditsInAnySecundaryAreaOJB extends PersistentObjectOJB implements
        IPersistentCreditsInAnySecundaryArea {
    public CreditsInAnySecundaryAreaOJB() {
    }

    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        return queryList(CreditsInAnySecundaryArea.class, criteria);
    }

    public ICreditsInAnySecundaryArea readByStudentCurricularPlanAndEnrollment(
            IStudentCurricularPlan studentCurricularPlan, IEnrolment enrolment)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (ICreditsInAnySecundaryArea) queryObject(CreditsInAnySecundaryArea.class, criteria);
    }

}