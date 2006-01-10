/*
 * Created on 6/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Tânia Pousão
 */
public class GratuitySituationVO extends VersionedObjectsBase implements IPersistentGratuitySituation {

	public GratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
			final Integer studentCurricularPlanID, final Integer gratuityValuesID)
			throws ExcepcaoPersistencia {

		final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanID);

		final List<GratuitySituation> gratuitySituationsFromCurricularPlan = studentCurricularPlan
				.getGratuitySituations();

		for (GratuitySituation gratuitySituation : gratuitySituationsFromCurricularPlan) {
			if (gratuitySituation.getGratuityValues().getIdInternal().equals(gratuityValuesID)) {
				return gratuitySituation;
			}
		}

		return null;
	}

	public List readGratuitySituationsByDegreeCurricularPlan(final Integer degreeCurricularPlanID)
			throws ExcepcaoPersistencia {

		final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
				DegreeCurricularPlan.class, degreeCurricularPlanID);

		final List<StudentCurricularPlan> studentCurricularPlans = degreeCurricularPlan
				.getStudentCurricularPlans();
		final List<GratuitySituation> result = new ArrayList();

		for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
			result.addAll(studentCurricularPlan.getGratuitySituations());
		}

		return result;
	}

	public GratuitySituation readGratuitySituationByExecutionDegreeAndStudent(
			final Integer executionDegreeID, final Integer studentID) throws ExcepcaoPersistencia {

		final List<StudentCurricularPlan> studentCurricularPlans = ((Student) readByOID(Student.class,
				studentID)).getStudentCurricularPlans();
		for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
			List<GratuitySituation> gratuitySituations = studentCurricularPlan.getGratuitySituations();
            
            if(gratuitySituations == null){
                continue;
            }
            
			for (final GratuitySituation gratuitySituation : gratuitySituations) {
				if (gratuitySituation.getGratuityValues().getExecutionDegree().getIdInternal().equals(
						executionDegreeID)) {
					return gratuitySituation;
				}
			}
		}
		return null;
	}

	public GratuitySituation readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
			final Integer studentCurricularPlanID, final Integer gratuityValuesID,
			GratuitySituationType gratuitySituationType) throws ExcepcaoPersistencia {

		final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanID);

		final List<GratuitySituation> gratuitySituationsFromCurricularPlan = studentCurricularPlan
				.getGratuitySituations();

		if (gratuitySituationType != null) {

			for (final GratuitySituation gratuitySituation : gratuitySituationsFromCurricularPlan) {
				if (gratuitySituation.getGratuityValues().getIdInternal().equals(gratuityValuesID)) {
					switch (gratuitySituationType) {

					case CREDITOR:
						if (gratuitySituation.getRemainingValue() < 0) {
							return gratuitySituation;
						}
						break;
					case DEBTOR:
						if (gratuitySituation.getRemainingValue() > 0) {
							return gratuitySituation;
						}
						break;
					case REGULARIZED:
						if (gratuitySituation.getRemainingValue() == 0) {
							return gratuitySituation;
						}
						break;
					default:
						break;
					}
				}
			}
		} else {
			for (final GratuitySituation gratuitySituation : gratuitySituationsFromCurricularPlan) {
				if (gratuitySituation.getGratuityValues().getIdInternal().equals(gratuityValuesID)) {
					return gratuitySituation;
				}
			}

		}
		return null;
	}

	public List readGratuitySituatuionListByStudentCurricularPlan(Integer studentCurricularPlanID)
			throws ExcepcaoPersistencia {
		return ((StudentCurricularPlan) readByOID(StudentCurricularPlan.class, studentCurricularPlanID))
				.getGratuitySituations();
	}
}