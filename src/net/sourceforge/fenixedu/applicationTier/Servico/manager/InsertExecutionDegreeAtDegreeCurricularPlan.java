/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICampus;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class InsertExecutionDegreeAtDegreeCurricularPlan implements IService {

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException {

        IExecutionYear executionYear = null;

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentCampus campusDAO = persistentSuport.getIPersistentCampus();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                    .getIPersistentDegreeCurricularPlan();

            ICampus campus = (ICampus) campusDAO.readByOID(Campus.class, infoExecutionDegree
                    .getInfoCampus().getIdInternal());
            if (campus == null) {
                throw new NonExistingServiceException("message.nonExistingCampus", null);
            }

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, infoExecutionDegree
                            .getInfoDegreeCurricularPlan().getIdInternal());

            if (degreeCurricularPlan == null) {
                throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
            }
            IPersistentExecutionYear persistentExecutionYear = persistentSuport
                    .getIPersistentExecutionYear();

            executionYear = (IExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
                    infoExecutionDegree.getInfoExecutionYear().getIdInternal());

            if (executionYear == null) {

                throw new NonExistingServiceException("message.non.existing.execution.year", null);
            }

            IPersistentExecutionDegree persistentExecutionDegree = persistentSuport
                    .getIPersistentExecutionDegree();

            IExecutionDegree executionDegree = new ExecutionDegree();
            persistentExecutionDegree.simpleLockWrite(executionDegree);
            executionDegree.setCurricularPlan(degreeCurricularPlan);
            executionDegree.setExecutionYear(executionYear);
            executionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());
            executionDegree.setCampus(campus);

            setPeriods(executionDegree, infoExecutionDegree);

        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException(
                    "O curso em execução referente ao ano lectivo em execução "
                            + executionYear.getYear(), existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }

    private void setPeriods(IExecutionDegree executionDegree, InfoExecutionDegree infoExecutionDegree)
            throws FenixServiceException {
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

    private void setCompositePeriod(IExecutionDegree executionDegree, InfoPeriod infoPeriod,
            int periodToAssociateExecutionDegree) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentPeriod periodDAO = persistentSuport.getIPersistentPeriod();
            List infoPeriodList = new ArrayList();

            infoPeriodList.add(infoPeriod);

            while (infoPeriod.getNextPeriod() != null) {
                infoPeriodList.add(infoPeriod.getNextPeriod());
                infoPeriod = infoPeriod.getNextPeriod();
            }

            //inicializacao
            int infoPeriodListSize = infoPeriodList.size();

            InfoPeriod infoPeriodNew = (InfoPeriod) infoPeriodList.get(infoPeriodListSize - 1);

            IPeriod period = (IPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew
                    .getStartDate(), infoPeriodNew.getEndDate(), null);

            if (period == null) {
                Calendar startDate = infoPeriodNew.getStartDate();
                Calendar endDate = infoPeriodNew.getEndDate();
                period = new Period(startDate, endDate);
                periodDAO.simpleLockWrite(period);
            }

            //iteracoes
            for (int i = infoPeriodListSize - 2; i >= 0; i--) {
                Integer keyNextPeriod = period.getIdInternal();

                IPeriod nextPeriod = period;

                infoPeriodNew = (InfoPeriod) infoPeriodList.get(i);

                period = (IPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew.getStartDate(),
                        infoPeriodNew.getEndDate(), keyNextPeriod);

                if (period == null) {
                    Calendar startDate = infoPeriodNew.getStartDate();
                    Calendar endDate = infoPeriodNew.getEndDate();
                    period = new Period(startDate, endDate);
                    periodDAO.simpleLockWrite(period);
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

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }

}