/*
 * Created on 14/Jun/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ana e Ricardo
 */
public class EditExecutionDegreePeriods implements IService {

    public void run(InfoExecutionDegree infoExecutionDegree) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IPersistentExecutionDegree persistentExecutionDegree = persistentSuport
                .getIPersistentExecutionDegree();
        IExecutionDegree oldExecutionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                ExecutionDegree.class, infoExecutionDegree.getIdInternal(), false);

        InfoPeriod infoPeriodExamsFirstSemester = infoExecutionDegree.getInfoPeriodExamsFirstSemester();
        IPeriod periodExamsFirstSemester = setCompositePeriod(infoPeriodExamsFirstSemester);

        InfoPeriod infoPeriodExamsSecondSemester = infoExecutionDegree
                .getInfoPeriodExamsSecondSemester();
        IPeriod periodExamsSecondSemester = setCompositePeriod(infoPeriodExamsSecondSemester);

        InfoPeriod infoPeriodLessonsFirstSemester = infoExecutionDegree
                .getInfoPeriodLessonsFirstSemester();
        IPeriod periodLessonsFirstSemester = setCompositePeriod(infoPeriodLessonsFirstSemester);

        InfoPeriod infoPeriodLessonsSecondSemester = infoExecutionDegree
                .getInfoPeriodLessonsSecondSemester();
        IPeriod periodLessonsSecondSemester = setCompositePeriod(infoPeriodLessonsSecondSemester);

        persistentExecutionDegree.simpleLockWrite(oldExecutionDegree);
        oldExecutionDegree.setPeriodLessonsFirstSemester(periodLessonsFirstSemester);
        oldExecutionDegree.setPeriodLessonsSecondSemester(periodLessonsSecondSemester);
        oldExecutionDegree.setPeriodExamsFirstSemester(periodExamsFirstSemester);
        oldExecutionDegree.setPeriodExamsSecondSemester(periodExamsSecondSemester);
    }

    // retorna o primeiro period do executiondegree
    private IPeriod setCompositePeriod(InfoPeriod infoPeriod) throws ExcepcaoPersistencia {
        List<InfoPeriod> infoPeriodList = new ArrayList<InfoPeriod>();

        infoPeriodList.add(infoPeriod);
        while (infoPeriod.getNextPeriod() != null) {
            infoPeriodList.add(infoPeriod.getNextPeriod());
            infoPeriod = infoPeriod.getNextPeriod();
        }

        // inicializacao
        int infoPeriodListSize = infoPeriodList.size();

        InfoPeriod infoPeriodNew = infoPeriodList.get(infoPeriodListSize - 1);

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentPeriod periodDAO = persistentSuport.getIPersistentPeriod();
        IPeriod period = (IPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew.getStartDate(),
                infoPeriodNew.getEndDate(), null);

        if (period == null) {
            Calendar startDate = infoPeriodNew.getStartDate();
            Calendar endDate = infoPeriodNew.getEndDate();
            period = DomainFactory.makePeriod(startDate, endDate);
        }

        // iteracoes
        for (int i = infoPeriodListSize - 2; i >= 0; i--) {
            Integer keyNextPeriod = period.getIdInternal();

            IPeriod nextPeriod = period;

            infoPeriodNew = infoPeriodList.get(i);

            period = (IPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew.getStartDate(),
                    infoPeriodNew.getEndDate(), keyNextPeriod);

            if (period == null) {
                Calendar startDate = infoPeriodNew.getStartDate();
                Calendar endDate = infoPeriodNew.getEndDate();
                period = DomainFactory.makePeriod(startDate, endDate);
                period.setNextPeriod(nextPeriod);
            }
        }

        return period;
    }

}
