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
import net.sourceforge.fenixedu.domain.IOccupationPeriod;
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
        IOccupationPeriod periodExamsFirstSemester = setCompositePeriod(infoPeriodExamsFirstSemester);

        InfoPeriod infoPeriodExamsSecondSemester = infoExecutionDegree
                .getInfoPeriodExamsSecondSemester();
        IOccupationPeriod periodExamsSecondSemester = setCompositePeriod(infoPeriodExamsSecondSemester);

        InfoPeriod infoPeriodLessonsFirstSemester = infoExecutionDegree
                .getInfoPeriodLessonsFirstSemester();
        IOccupationPeriod periodLessonsFirstSemester = setCompositePeriod(infoPeriodLessonsFirstSemester);

        InfoPeriod infoPeriodLessonsSecondSemester = infoExecutionDegree
                .getInfoPeriodLessonsSecondSemester();
        IOccupationPeriod periodLessonsSecondSemester = setCompositePeriod(infoPeriodLessonsSecondSemester);

        persistentExecutionDegree.simpleLockWrite(oldExecutionDegree);
        oldExecutionDegree.setPeriodLessonsFirstSemester(periodLessonsFirstSemester);
        oldExecutionDegree.setPeriodLessonsSecondSemester(periodLessonsSecondSemester);
        oldExecutionDegree.setPeriodExamsFirstSemester(periodExamsFirstSemester);
        oldExecutionDegree.setPeriodExamsSecondSemester(periodExamsSecondSemester);
    }

    // retorna o primeiro period do executiondegree
    private IOccupationPeriod setCompositePeriod(InfoPeriod infoPeriod) throws ExcepcaoPersistencia {
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
        IOccupationPeriod period = (IOccupationPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew.getStartDate().getTime(),
                infoPeriodNew.getEndDate().getTime(), null);

        if (period == null) {
            Calendar startDate = infoPeriodNew.getStartDate();
            Calendar endDate = infoPeriodNew.getEndDate();
            period = DomainFactory.makeOccupationPeriod(startDate.getTime(), endDate.getTime());
        }

        // iteracoes
        for (int i = infoPeriodListSize - 2; i >= 0; i--) {
            Integer keyNextPeriod = period.getIdInternal();

            IOccupationPeriod nextPeriod = period;

            infoPeriodNew = infoPeriodList.get(i);

            period = (IOccupationPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew.getStartDate().getTime(),
                    infoPeriodNew.getEndDate().getTime(), keyNextPeriod);

            if (period == null) {
                Calendar startDate = infoPeriodNew.getStartDate();
                Calendar endDate = infoPeriodNew.getEndDate();
                period = DomainFactory.makeOccupationPeriod(startDate.getTime(), endDate.getTime());
                period.setNextPeriod(nextPeriod);
            }
        }

        return period;
    }

}
