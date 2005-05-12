/*
 * Created on 6/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.util.Specialization;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 *  
 */
public class GratuitySituationOJB extends PersistentObjectOJB implements IPersistentGratuitySituation {

    public IGratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
            IStudentCurricularPlan studentCurricularPlan, IGratuityValues gratuityValues)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("gratuityValues.idInternal", gratuityValues.getIdInternal());

        return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
    }

    public List readGratuitySituationsByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.idInternal",
                degreeCurricularPlan.getIdInternal());

        return queryList(GratuitySituation.class, criteria);
    }

    public List readGratuitySituationListByExecutionDegreeAndSpecialization(
            IExecutionDegree executionDegree, Specialization specialization) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (executionDegree != null) {
            if (executionDegree.getIdInternal() != null) {
                criteria.addEqualTo("gratuityValues.executionDegree.idInternal", executionDegree
                        .getIdInternal());
            } else if (executionDegree.getExecutionYear() != null
                    && executionDegree.getExecutionYear().getYear() != null) {
                criteria.addEqualTo("gratuityValues.executionDegree.executionYear.year", executionDegree
                        .getExecutionYear().getYear());
            }
        }

        if (specialization != null && specialization.getSpecialization() != null) {
            criteria.addEqualTo("studentCurricularPlan.specialization", specialization
                    .getSpecialization());
        } else //all specialization required, but not records with
               // specialization null
        {
            criteria.addNotNull("studentCurricularPlan.specialization");
        }

        /*
         * //student curricular plan actives or with school part conclued
         * Criteria criteriaState = new Criteria(); criteriaState.addEqualTo(
         * "studentCurricularPlan.currentState",
         * StudentCurricularPlanState.ACTIVE_OBJ); Criteria
         * criteriaStateConclued = new Criteria();
         * criteriaStateConclued.addEqualTo(
         * "studentCurricularPlan.currentState",
         * StudentCurricularPlanState.SCHOOLPARTCONCLUDED_OBJ);
         * criteriaState.addOrCriteria(criteriaStateConclued);
         * 
         * criteria.addAndCriteria(criteriaState);
         */

        return queryList(GratuitySituation.class, criteria);
    }

    public List readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(
            IExecutionDegree executionDegree, Specialization specialization,
            GratuitySituationType situation) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (executionDegree != null) {
            if (executionDegree.getIdInternal() != null) {
                criteria.addEqualTo("gratuityValues.executionDegree.idInternal", executionDegree
                        .getIdInternal());
            } else if (executionDegree.getExecutionYear() != null
                    && executionDegree.getExecutionYear().getYear() != null) {
                criteria.addEqualTo("gratuityValues.executionDegree.executionYear.year", executionDegree
                        .getExecutionYear().getYear());
            }
        }

        if (specialization != null && specialization.getSpecialization() != null) {
            criteria.addEqualTo("studentCurricularPlan.specialization", specialization
                    .getSpecialization());
        } else //all specialization required, but not records with
               // specialization null
        {
            criteria.addNotNull("studentCurricularPlan.specialization");
        }

        if (situation != null) {
            switch (situation) {
            case CREDITOR:
                //CREDITOR situation: remainingValue < 0
                criteria.addLessThan("remainingValue", new Double(0));
                break;
            case DEBTOR:
                //DEBTOR situation: remainingValue > 0
                criteria.addGreaterThan("remainingValue", new Double(0));
                break;
            case REGULARIZED:
                //REGULARIZED situation: remainingValue == 0
                criteria.addEqualTo("remainingValue", new Double(0));
                break;
            default:
                break;
            }
        }

        return queryList(GratuitySituation.class, criteria);
    }

    public IGratuitySituation readGratuitySituationByExecutionDegreeAndStudent(
            IExecutionDegree executionDegree, IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria
                .addEqualTo("gratuityValues.executionDegree.idInternal", executionDegree.getIdInternal());
        criteria.addEqualTo("studentCurricularPlan.student.idInternal", student.getIdInternal());

        return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
    }

    public IGratuitySituation readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
            IStudentCurricularPlan studentCurricularPlan, IGratuityValues gratuityValues,
            GratuitySituationType gratuitySituationType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        criteria.addEqualTo("gratuityValues.idInternal", gratuityValues.getIdInternal());

        if (gratuitySituationType != null) {

            switch (gratuitySituationType) {

            case CREDITOR:
                //CREDITOR situation: remainingValue < 0
                criteria.addLessThan("remainingValue", new Double(0));
                break;
            case DEBTOR:
                //DEBTOR situation: remainingValue > 0
                criteria.addGreaterThan("remainingValue", new Double(0));
                break;
            case REGULARIZED:
                //REGULARIZED situation: remainingValue == 0
                criteria.addEqualTo("remainingValue", new Double(0));
                break;
            default:
                break;
            }
        }

        return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
    }

    public List readGratuitySituatuionListByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());

        return queryList(GratuitySituation.class, criteria);
    }
}