/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 * 
 * Created on 2003/08/09
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.CurricularYear;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.IAula;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear implements IServico
{

    private static ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear _servico =
        new ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear getService()
    {
        return _servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear()
    {
    }

    /**
	 * Devolve o nome do servico
	 */
    public final String getNome()
    {
        return "ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear";
    }

    public Object run(
        InfoExecutionPeriod infoExecutionPeriod,
        InfoExecutionDegree infoExecutionDegree,
        InfoCurricularYear infoCurricularYear)
        throws FenixServiceException
    {

        //		Calendar serviceStartTime;
        //		Calendar serviceEndTime;
        //		Calendar queryStartTime;
        //		Calendar queryEndTime;
        //		Calendar transformerStartTime;
        //		Calendar transformerEndTime;

        //		serviceStartTime = Calendar.getInstance();

        List infoShifts = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionPeriod executionPeriod =
                (IExecutionPeriod) sp.getIPersistentExecutionPeriod().readByOID(
                    ExecutionPeriod.class,
                    infoExecutionPeriod.getIdInternal());

            ICursoExecucao executionDegree =
                (ICursoExecucao) sp.getICursoExecucaoPersistente().readByOID(
                    CursoExecucao.class,
                    infoExecutionDegree.getIdInternal());

            ICurricularYear curricularYear =
                (ICurricularYear) sp.getIPersistentCurricularYear().readByOID(
                    CurricularYear.class,
                    infoCurricularYear.getIdInternal());

            //			queryStartTime = Calendar.getInstance();
            List shifts =
                sp.getITurnoPersistente().readByExecutionPeriodAndExecutionDegreeAndCurricularYear(
                    executionPeriod,
                    executionDegree,
                    curricularYear);
            //			queryEndTime = Calendar.getInstance();

            //			transformerStartTime = Calendar.getInstance();
            // This is the nice way to get the job done, but the eficiency is
			// very poor...
            // It takes about 15seconds for an average list of shifts.
            //			infoShifts =
            //				(List) CollectionUtils.collect(shifts, new Transformer() {
            //				public Object transform(Object arg0) {
            //					ITurno shift = (ITurno) arg0;
            //					InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
            //					infoShift
            //						.setInfoLessons(
            //							(List) CollectionUtils
            //							.collect(
            //								shift.getAssociatedLessons(),
            //								new Transformer() {
            //						public Object transform(Object arg0) {
            //							return Cloner.copyILesson2InfoLesson((IAula) arg0);
            //						}
            //					}));
            //					return infoShift;
            //				}
            //			});
            // So this is the fast way:
            // It takes about 1seconds for an average list of shifts.
            // Could still make it faster... but this is probably fast enough.
            infoShifts = new ArrayList();
            for (int i = 0; i < shifts.size(); i++)
            {
                ITurno shift = (ITurno) shifts.get(i);
                //Cloner.copyShift2InfoShift((ITurno) shifts.get(i));
                InfoShift infoShift = new InfoShift();
                infoShift.setAvailabilityFinal(shift.getAvailabilityFinal());
                infoShift.setIdInternal(shift.getIdInternal());
                infoShift.setLotacao(shift.getLotacao());
                infoShift.setNome(shift.getNome());
                infoShift.setOcupation(shift.getOcupation());
                infoShift.setPercentage(shift.getPercentage());
                infoShift.setTipo(shift.getTipo());

                infoShift.setInfoLessons(new ArrayList());
                InfoExecutionCourse infoExecutionCourse =
                    (InfoExecutionCourse) Cloner.get(
                    ((ITurno) shifts.get(i)).getDisciplinaExecucao());
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
                for (int j = 0; j < ((ITurno) shifts.get(i)).getAssociatedLessons().size(); j++)
                {
                    IAula lesson = (IAula) ((ITurno) shifts.get(i)).getAssociatedLessons().get(j);
                    InfoLesson infoLesson = new InfoLesson();
                    InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());

                    infoLesson.setDiaSemana(lesson.getDiaSemana());
                    infoLesson.setFim(lesson.getFim());
                    infoLesson.setIdInternal(lesson.getIdInternal());
                    infoLesson.setInicio(lesson.getInicio());
                    infoLesson.setTipo(lesson.getTipo());
                    infoLesson.setInfoSala(infoRoom);
                    infoLesson.setInfoDisciplinaExecucao(infoExecutionCourse);

                    infoShift.getInfoLessons().add(infoLesson);

                }
                infoShifts.add(infoShift);
            }

            //			transformerEndTime = Calendar.getInstance();
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }
        //		serviceEndTime = Calendar.getInstance();

        //		long totalServiceRuntime = serviceEndTime.getTimeInMillis() -
		// serviceStartTime.getTimeInMillis();
        //		long queryRuntime = queryEndTime.getTimeInMillis() -
		// queryStartTime.getTimeInMillis();
        //		long transformerRuntime =
        //			transformerEndTime.getTimeInMillis() -
		// transformerStartTime.getTimeInMillis();
        //		long serviceRuntime = totalServiceRuntime - queryRuntime -
		// transformerRuntime;
        //
        //		System.out.println("Query runtime = [" + (queryRuntime / 1000) +
		// "]s");
        //		System.out.println("Transformer runtime = [" + (transformerRuntime /
		// 1000) + "]s");
        //		System.out.println("Service runtime = [" + (serviceRuntime / 1000) +
		// "]s");
        //		System.out.println("------------------------------------");
        //		System.out.println("Total runtime = [" + (totalServiceRuntime /
		// 1000) + "]s");

        return infoShifts;

    }

}