/*
 * Created on 14/Jun/2004
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoPeriod;
import Dominio.ExecutionDegree;
import Dominio.IExecutionDegree;
import Dominio.IPeriod;
import Dominio.Period;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Ana e Ricardo
 */
public class EditExecutionDegreePeriods implements IServico {

    private static EditExecutionDegreePeriods service = new EditExecutionDegreePeriods();

    public static EditExecutionDegreePeriods getService() {
        return service;
    }

    private EditExecutionDegreePeriods() {
    }

    public final String getNome() {
        return "EditExecutionDegreePeriods";
    }

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException {

        IPersistentExecutionDegree persistentExecutionDegree = null;

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentExecutionDegree = persistentSuport.getIPersistentExecutionDegree();

            IExecutionDegree oldExecutionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, infoExecutionDegree.getIdInternal(), false);

            InfoPeriod infoPeriodExamsFirstSemester = infoExecutionDegree
                    .getInfoPeriodExamsFirstSemester();
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

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }

    //retorna o primeiro period do executiondegree
    private IPeriod setCompositePeriod(InfoPeriod infoPeriod) throws FenixServiceException {
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

            return period;

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }

}