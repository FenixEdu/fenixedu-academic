/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author lmac1
 */
public class InsertExecutionDegreeAtDegreeCurricularPlan implements IService {

	public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException,
			ExcepcaoPersistencia {

		ExecutionYear executionYear = null;

		try {
			ISuportePersistente persistentSuport = PersistenceSupportFactory
					.getDefaultPersistenceSupport();

			IPersistentCampus campusDAO = persistentSuport.getIPersistentCampus();
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
					.getIPersistentDegreeCurricularPlan();

			Campus campus = (Campus) campusDAO.readByOID(Campus.class, infoExecutionDegree
					.getInfoCampus().getIdInternal());
			if (campus == null) {
				throw new NonExistingServiceException("message.nonExistingCampus", null);
			}

			DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
					.readByOID(DegreeCurricularPlan.class, infoExecutionDegree
							.getInfoDegreeCurricularPlan().getIdInternal());

			if (degreeCurricularPlan == null) {
				throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
			}
			IPersistentExecutionYear persistentExecutionYear = persistentSuport
					.getIPersistentExecutionYear();

			executionYear = (ExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
					infoExecutionDegree.getInfoExecutionYear().getIdInternal());

			if (executionYear == null) {

				throw new NonExistingServiceException("message.non.existing.execution.year", null);
			}

			ExecutionDegree executionDegree = DomainFactory.makeExecutionDegree();
			executionDegree.setDegreeCurricularPlan(degreeCurricularPlan);
			executionDegree.setExecutionYear(executionYear);
			executionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());
			executionDegree.setCampus(campus);

			setPeriods(executionDegree, infoExecutionDegree);

		} catch (ExistingPersistentException existingException) {
			throw new ExistingServiceException(
					"O curso em execução referente ao ano lectivo em execução "
							+ executionYear.getYear(), existingException);
		}
	}

	private void setPeriods(ExecutionDegree executionDegree, InfoExecutionDegree infoExecutionDegree)
			throws FenixServiceException, ExcepcaoPersistencia {
		InfoPeriod infoPeriodExamsFirstSemester = infoExecutionDegree.getInfoPeriodExamsFirstSemester();
		setCompositePeriod(executionDegree, infoPeriodExamsFirstSemester, 11);

		InfoPeriod infoPeriodExamsSecondSemester = infoExecutionDegree
				.getInfoPeriodExamsSecondSemester();
		setCompositePeriod(executionDegree, infoPeriodExamsSecondSemester, 12);

		InfoPeriod infoPeriodLessonsFirstSemester = infoExecutionDegree
				.getInfoPeriodLessonsFirstSemester();
		setCompositePeriod(executionDegree, infoPeriodLessonsFirstSemester, 21);

		InfoPeriod infoPeriodLessonsSecondSemester = infoExecutionDegree
				.getInfoPeriodLessonsSecondSemester();
		setCompositePeriod(executionDegree, infoPeriodLessonsSecondSemester, 22);
	}

	private void setCompositePeriod(ExecutionDegree executionDegree, InfoPeriod infoPeriod,
			int periodToAssociateExecutionDegree) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentPeriod periodDAO = persistentSuport.getIPersistentPeriod();
		List<InfoPeriod> infoPeriodList = new ArrayList<InfoPeriod>();

		infoPeriodList.add(infoPeriod);

		while (infoPeriod.getNextPeriod() != null) {
			infoPeriodList.add(infoPeriod.getNextPeriod());
			infoPeriod = infoPeriod.getNextPeriod();
		}

		// inicializacao
		int infoPeriodListSize = infoPeriodList.size();

		InfoPeriod infoPeriodNew = infoPeriodList.get(infoPeriodListSize - 1);

		OccupationPeriod period = (OccupationPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew.getStartDate()
				.getTime(), infoPeriodNew.getEndDate().getTime(), null);

		if (period == null) {
			Calendar startDate = infoPeriodNew.getStartDate();
			Calendar endDate = infoPeriodNew.getEndDate();
			period = DomainFactory.makeOccupationPeriod(startDate.getTime(), endDate.getTime());
		}

		// iteracoes
		for (int i = infoPeriodListSize - 2; i >= 0; i--) {
			Integer keyNextPeriod = period.getIdInternal();

			OccupationPeriod nextPeriod = period;

			infoPeriodNew = infoPeriodList.get(i);

			period = (OccupationPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew.getStartDate()
					.getTime(), infoPeriodNew.getEndDate().getTime(), keyNextPeriod);

			if (period == null) {
				Calendar startDate = infoPeriodNew.getStartDate();
				Calendar endDate = infoPeriodNew.getEndDate();
				period = DomainFactory.makeOccupationPeriod(startDate.getTime(), endDate.getTime());
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