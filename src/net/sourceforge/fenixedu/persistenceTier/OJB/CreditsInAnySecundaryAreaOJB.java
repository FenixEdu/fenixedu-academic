package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInAnySecundaryArea;

import org.apache.ojb.broker.query.Criteria;

public class CreditsInAnySecundaryAreaOJB extends PersistentObjectOJB implements
        IPersistentCreditsInAnySecundaryArea {
    public CreditsInAnySecundaryAreaOJB() {
    }

    public List readAllByStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        return queryList(CreditsInAnySecundaryArea.class, criteria);
    }

    public CreditsInAnySecundaryArea readByStudentCurricularPlanAndEnrollment(
            StudentCurricularPlan studentCurricularPlan, Enrolment enrolment)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (CreditsInAnySecundaryArea) queryObject(CreditsInAnySecundaryArea.class, criteria);
    }

}