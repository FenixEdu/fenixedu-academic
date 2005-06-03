/*
 * Created on 6/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
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

	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
			final Integer studentCurricularPlanID, final Integer gratuityValuesID)
			throws ExcepcaoPersistencia {

		final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanID);

		final List<IGratuitySituation> gratuitySituationsFromCurricularPlan = studentCurricularPlan
				.getGratuitySituations();

		for (IGratuitySituation gratuitySituation : gratuitySituationsFromCurricularPlan) {
			if (gratuitySituation.getGratuityValues().getIdInternal().equals(gratuityValuesID)) {
				return gratuitySituation;
			}
		}

		return null;
	}

	public List readGratuitySituationsByDegreeCurricularPlan(final Integer degreeCurricularPlanID)
			throws ExcepcaoPersistencia {

		final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
				DegreeCurricularPlan.class, degreeCurricularPlanID);

		final List<IStudentCurricularPlan> studentCurricularPlans = degreeCurricularPlan
				.getStudentCurricularPlans();
		final List<IGratuitySituation> result = new ArrayList();

		for (final IStudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
			result.addAll(studentCurricularPlan.getGratuitySituations());
		}

		return result;
	}

	public IGratuitySituation readGratuitySituationByExecutionDegreeAndStudent(
			final Integer executionDegreeID, final Integer studentID) throws ExcepcaoPersistencia {

		final List<IStudentCurricularPlan> studentCurricularPlans = ((IStudent) readByOID(Student.class,
				studentID)).getStudentCurricularPlans();
		for (final IStudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
			List<IGratuitySituation> gratuitySituations = studentCurricularPlan.getGratuitySituations();
            
            if(gratuitySituations == null){
                continue;
            }
            
			for (final IGratuitySituation gratuitySituation : gratuitySituations) {
				if (gratuitySituation.getGratuityValues().getExecutionDegree().getIdInternal().equals(
						executionDegreeID)) {
					return gratuitySituation;
				}
			}
		}
		return null;
	}

	public IGratuitySituation readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
			final Integer studentCurricularPlanID, final Integer gratuityValuesID,
			GratuitySituationType gratuitySituationType) throws ExcepcaoPersistencia {

		final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanID);

		final List<IGratuitySituation> gratuitySituationsFromCurricularPlan = studentCurricularPlan
				.getGratuitySituations();

		if (gratuitySituationType != null) {

			for (final IGratuitySituation gratuitySituation : gratuitySituationsFromCurricularPlan) {
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
			for (final IGratuitySituation gratuitySituation : gratuitySituationsFromCurricularPlan) {
				if (gratuitySituation.getGratuityValues().getIdInternal().equals(gratuityValuesID)) {
					return gratuitySituation;
				}
			}

		}
		return null;
	}

	public List readGratuitySituatuionListByStudentCurricularPlan(Integer studentCurricularPlanID)
			throws ExcepcaoPersistencia {
		return ((IStudentCurricularPlan) readByOID(StudentCurricularPlan.class, studentCurricularPlanID))
				.getGratuitySituations();
	}
}