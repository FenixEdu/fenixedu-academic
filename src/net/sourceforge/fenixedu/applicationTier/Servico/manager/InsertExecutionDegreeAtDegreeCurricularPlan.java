package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertExecutionDegreeAtDegreeCurricularPlan extends Service {

	public void run(InfoExecutionDegreeEditor infoExecutionDegree) throws FenixServiceException, ExcepcaoPersistencia {
		final Campus campus = rootDomainObject.readCampusByOID(infoExecutionDegree.getInfoCampus().getIdInternal());
		if (campus == null) {
			throw new NonExistingServiceException("message.nonExistingCampus", null);
		}

		final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
		if (degreeCurricularPlan == null) {
			throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
		}

        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(infoExecutionDegree.getInfoExecutionYear().getIdInternal());
		if (executionYear == null) {
			throw new NonExistingServiceException("message.non.existing.execution.year", null);
		}

		ExecutionDegree executionDegree = degreeCurricularPlan.createExecutionDegree(executionYear, campus, infoExecutionDegree.getTemporaryExamMap());
		setPeriods(executionDegree, infoExecutionDegree);
	}

	private void setPeriods(ExecutionDegree executionDegree, InfoExecutionDegreeEditor infoExecutionDegree) {
		InfoPeriod infoPeriodExamsFirstSemester = infoExecutionDegree.getInfoPeriodExamsFirstSemester();
		setCompositePeriod(executionDegree, infoPeriodExamsFirstSemester, 11);

		InfoPeriod infoPeriodExamsSecondSemester = infoExecutionDegree.getInfoPeriodExamsSecondSemester();
		setCompositePeriod(executionDegree, infoPeriodExamsSecondSemester, 12);

		InfoPeriod infoPeriodLessonsFirstSemester = infoExecutionDegree.getInfoPeriodLessonsFirstSemester();
		setCompositePeriod(executionDegree, infoPeriodLessonsFirstSemester, 21);

		InfoPeriod infoPeriodLessonsSecondSemester = infoExecutionDegree.getInfoPeriodLessonsSecondSemester();
		setCompositePeriod(executionDegree, infoPeriodLessonsSecondSemester, 22);
	}

	private void setCompositePeriod(ExecutionDegree executionDegree, InfoPeriod infoPeriod, int periodToAssociateExecutionDegree) {
		List<InfoPeriod> infoPeriodList = new ArrayList<InfoPeriod>();

		infoPeriodList.add(infoPeriod);

		while (infoPeriod.getNextPeriod() != null) {
			infoPeriodList.add(infoPeriod.getNextPeriod());
			infoPeriod = infoPeriod.getNextPeriod();
		}

		// inicializacao
		int infoPeriodListSize = infoPeriodList.size();
		InfoPeriod infoPeriodNew = infoPeriodList.get(infoPeriodListSize - 1);

		OccupationPeriod period = OccupationPeriod.readByDates(
                infoPeriodNew.getStartDate().getTime(), 
                infoPeriodNew.getEndDate().getTime());
		if (period == null) {
			Calendar startDate = infoPeriodNew.getStartDate();
			Calendar endDate = infoPeriodNew.getEndDate();
			period = new OccupationPeriod(startDate.getTime(), endDate.getTime());
		}

		// iteracoes
		for (int i = infoPeriodListSize - 2; i >= 0; i--) {
            infoPeriodNew = infoPeriodList.get(i);
            
            OccupationPeriod nextPeriod = period;
			period = OccupationPeriod.readByDates(
                    infoPeriodNew.getStartDate().getTime(), 
                    infoPeriodNew.getEndDate().getTime());
			if (period == null) {
				Calendar startDate = infoPeriodNew.getStartDate();
				Calendar endDate = infoPeriodNew.getEndDate();
				period = new OccupationPeriod(startDate.getTime(), endDate.getTime());
				period.setNextPeriod(nextPeriod);
			}
		}

		if (periodToAssociateExecutionDegree == 11) {
			executionDegree.setPeriodExamsFirstSemester(period);
		} else if (periodToAssociateExecutionDegree == 12) {
			executionDegree.setPeriodExamsSecondSemester(period);
		} else if (periodToAssociateExecutionDegree == 21) {
			executionDegree.setPeriodLessonsFirstSemester(period);
		} else if (periodToAssociateExecutionDegree == 22) {
			executionDegree.setPeriodLessonsSecondSemester(period);
		}
	}

}
