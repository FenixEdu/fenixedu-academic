/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 * 
 * Created on 2003/08/09
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear implements IService {

    public Object run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) throws FenixServiceException {

        //		Calendar serviceStartTime;
        //		Calendar serviceEndTime;
        //		Calendar queryStartTime;
        //		Calendar queryEndTime;
        //		Calendar transformerStartTime;
        //		Calendar transformerEndTime;

        //		serviceStartTime = Calendar.getInstance();

        List infoShifts = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionPeriod executionPeriod = (IExecutionPeriod) sp.getIPersistentExecutionPeriod()
                    .readByOID(ExecutionPeriod.class, infoExecutionPeriod.getIdInternal());

            IExecutionDegree executionDegree = (IExecutionDegree) sp.getIPersistentExecutionDegree()
                    .readByOID(ExecutionDegree.class, infoExecutionDegree.getIdInternal());

            ICurricularYear curricularYear = (ICurricularYear) sp.getIPersistentCurricularYear()
                    .readByOID(CurricularYear.class, infoCurricularYear.getIdInternal());

            //			queryStartTime = Calendar.getInstance();
            List shifts = sp.getITurnoPersistente()
                    .readByExecutionPeriodAndExecutionDegreeAndCurricularYear(executionPeriod,
                            executionDegree, curricularYear);
            //			queryEndTime = Calendar.getInstance();

            //			transformerStartTime = Calendar.getInstance();
            // This is the nice way to get the job done, but the eficiency is
            // very poor...
            // It takes about 15seconds for an average list of shifts.
            //			infoShifts =
            //				(List) CollectionUtils.collect(shifts, new Transformer() {
            //				public Object transform(Object arg0) {
            //					IShift shift = (IShift) arg0;
            //					InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
            //					infoShift
            //						.setInfoLessons(
            //							(List) CollectionUtils
            //							.collect(
            //								shift.getAssociatedLessons(),
            //								new Transformer() {
            //						public Object transform(Object arg0) {
            //							return Cloner.copyILesson2InfoLesson((ILesson) arg0);
            //						}
            //					}));
            //					return infoShift;
            //				}
            //			});
            // So this is the fast way:
            // It takes about 1seconds for an average list of shifts.
            // Could still make it faster... but this is probably fast enough.
            infoShifts = new ArrayList();
            for (int i = 0; i < shifts.size(); i++) {
                IShift shift = (IShift) shifts.get(i);

                InfoShift infoShift = new InfoShift();
                infoShift.setAvailabilityFinal(shift.getAvailabilityFinal());
                infoShift.setIdInternal(shift.getIdInternal());
                infoShift.setLotacao(shift.getLotacao());
                infoShift.setNome(shift.getNome());
                infoShift.setOcupation(shift.getOcupation());
                infoShift.setPercentage(shift.getPercentage());
                infoShift.setTipo(shift.getTipo());

                infoShift.setInfoLessons(new ArrayList());
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
                        .get(((IShift) shifts.get(i)).getDisciplinaExecucao());
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
                for (int j = 0; j < ((IShift) shifts.get(i)).getAssociatedLessons().size(); j++) {
                    ILesson lesson = (ILesson) ((IShift) shifts.get(i)).getAssociatedLessons().get(j);
                    InfoLesson infoLesson = new InfoLesson();
                    InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());
                    InfoRoomOccupation infoRoomOccupation = Cloner
                            .copyIRoomOccupation2InfoRoomOccupation(lesson.getRoomOccupation());

                    infoLesson.setDiaSemana(lesson.getDiaSemana());
                    infoLesson.setFim(lesson.getFim());
                    infoLesson.setIdInternal(lesson.getIdInternal());
                    infoLesson.setInicio(lesson.getInicio());
                    infoLesson.setTipo(lesson.getTipo());
                    infoLesson.setInfoSala(infoRoom);
                    infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                    infoLesson.setInfoShift(infoShift);
                    //infoLesson.setInfoDisciplinaExecucao(infoExecutionCourse);

                    infoShift.getInfoLessons().add(infoLesson);

                }
                infoShifts.add(infoShift);
            }

            //			transformerEndTime = Calendar.getInstance();
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return infoShifts;

    }

}