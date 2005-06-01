/*
 * Created on 6/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 */
public class GratuitySituationOJB extends PersistentObjectOJB implements IPersistentGratuitySituation {

	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
			Integer studentCurricularPlanID, Integer gratuityValuesID) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanID);
		criteria.addEqualTo("gratuityValues.idInternal", gratuityValuesID);

		return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
	}

	public List readGratuitySituationsByDegreeCurricularPlan(Integer degreeCurricularPlanID)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.idInternal",
				degreeCurricularPlanID);

		return queryList(GratuitySituation.class, criteria);
	}

	public IGratuitySituation readGratuitySituationByExecutionDegreeAndStudent(
			Integer executionDegreeID, Integer studentID) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("gratuityValues.executionDegree.idInternal", executionDegreeID);
		criteria.addEqualTo("studentCurricularPlan.student.idInternal", studentID);

		return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
	}

	public IGratuitySituation readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
			Integer studentCurricularPlanID, Integer gratuityValuesID,
			GratuitySituationType gratuitySituationType) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanID);
		criteria.addEqualTo("gratuityValues.idInternal", gratuityValuesID);

		if (gratuitySituationType != null) {

			switch (gratuitySituationType) {

			case CREDITOR:
				// CREDITOR situation: remainingValue < 0
				criteria.addLessThan("remainingValue", new Double(0));
				break;
			case DEBTOR:
				// DEBTOR situation: remainingValue > 0
				criteria.addGreaterThan("remainingValue", new Double(0));
				break;
			case REGULARIZED:
				// REGULARIZED situation: remainingValue == 0
				criteria.addEqualTo("remainingValue", new Double(0));
				break;
			default:
				break;
			}
		}

		return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
	}

	public List readGratuitySituatuionListByStudentCurricularPlan(Integer studentCurricularPlanID)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanID);

		return queryList(GratuitySituation.class, criteria);
	}

}