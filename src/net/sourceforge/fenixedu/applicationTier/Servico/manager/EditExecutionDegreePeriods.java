package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditExecutionDegreePeriods extends Service {

    public void run(InfoExecutionDegreeEditor infoExecutionDegree) throws ExcepcaoPersistencia {

        final ExecutionDegree oldExecutionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        OccupationPeriod periodLessonsFirstSemester = setCompositePeriod(infoExecutionDegree.getInfoPeriodLessonsFirstSemester());
        oldExecutionDegree.setPeriodLessonsFirstSemester(periodLessonsFirstSemester);

        OccupationPeriod periodLessonsSecondSemester = setCompositePeriod(infoExecutionDegree.getInfoPeriodLessonsSecondSemester());
        oldExecutionDegree.setPeriodLessonsSecondSemester(periodLessonsSecondSemester);

        OccupationPeriod periodExamsFirstSemester = setCompositePeriod(infoExecutionDegree.getInfoPeriodExamsFirstSemester());
        oldExecutionDegree.setPeriodExamsFirstSemester(periodExamsFirstSemester);

        OccupationPeriod periodExamsSecondSemester = setCompositePeriod(infoExecutionDegree.getInfoPeriodExamsSecondSemester());
        oldExecutionDegree.setPeriodExamsSecondSemester(periodExamsSecondSemester);
    }

    // retorna o primeiro period do executiondegree
    private OccupationPeriod setCompositePeriod(InfoPeriod infoPeriod) throws ExcepcaoPersistencia {
        List<InfoPeriod> infoPeriodList = new ArrayList<InfoPeriod>();

        infoPeriodList.add(infoPeriod);
        while (infoPeriod.getNextPeriod() != null) {
            infoPeriodList.add(infoPeriod.getNextPeriod());
            infoPeriod = infoPeriod.getNextPeriod();
        }

        int infoPeriodListSize = infoPeriodList.size();
        InfoPeriod infoPeriodNew = infoPeriodList.get(infoPeriodListSize - 1);

        OccupationPeriod period = OccupationPeriod.readByDatesAndNextOccupationPeriod(
                infoPeriodNew.getStartDate().getTime(), 
                infoPeriodNew.getEndDate().getTime(),
                null);
        if (period == null) {
            Calendar startDate = infoPeriodNew.getStartDate();
            Calendar endDate = infoPeriodNew.getEndDate();
            period = new OccupationPeriod(startDate.getTime(), endDate.getTime());
        }

        for (int i = infoPeriodListSize - 2; i >= 0; i--) {
            infoPeriodNew = infoPeriodList.get(i);
            
            OccupationPeriod nextPeriod = period;
            period = OccupationPeriod.readByDatesAndNextOccupationPeriod(
                    infoPeriodNew.getStartDate().getTime(), 
                    infoPeriodNew.getEndDate().getTime(), 
                    nextPeriod);
            if (period == null) {
                Calendar startDate = infoPeriodNew.getStartDate();
                Calendar endDate = infoPeriodNew.getEndDate();
                period = new OccupationPeriod(startDate.getTime(), endDate.getTime());
                period.setNextPeriod(nextPeriod);
            }
        }

        return period;
    }

}
