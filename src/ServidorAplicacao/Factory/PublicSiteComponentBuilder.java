/*
 * Created on 5/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoomOccupation;
import DataBeans.InfoShift;
import DataBeans.InfoSiteClasses;
import DataBeans.InfoSiteTimetable;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class PublicSiteComponentBuilder {

    private static PublicSiteComponentBuilder instance = null;

    public PublicSiteComponentBuilder() {
    }

    public static PublicSiteComponentBuilder getInstance() {
        if (instance == null) {
            instance = new PublicSiteComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, ITurma domainClass)
            throws FenixServiceException {

        if (component instanceof InfoSiteTimetable) {
            return getInfoSiteTimetable((InfoSiteTimetable) component, domainClass);
        } else if (component instanceof InfoSiteClasses) {
            return getInfoSiteClasses((InfoSiteClasses) component, domainClass);
        }

        return null;

    }

    /**
     * @param classes
     * @return
     */
    private ISiteComponent getInfoSiteClasses(InfoSiteClasses component, ITurma domainClass)
            throws FenixServiceException {
        List classes = new ArrayList();
        List infoClasses = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurmaPersistente classDAO = sp.getITurmaPersistente();

            IExecutionPeriod executionPeriod = domainClass.getExecutionPeriod();
            ICursoExecucao executionDegree = domainClass.getExecutionDegree();

            classes = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(executionPeriod,
                    domainClass.getAnoCurricular(), executionDegree);

            for (int i = 0; i < classes.size(); i++) {
                ITurma taux = (ITurma) classes.get(i);
                infoClasses.add(copyClass2InfoClass(taux));
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        component.setClasses(infoClasses);
        return component;
    }

    /**
     * @param timetable
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component, ITurma domainClass)
            throws FenixServiceException {
        List infoLessonList = null;

        IExecutionPeriod infoExecutionPeriod;
        
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            //ITurnoAulaPersistente shiftLessonDAO =
            // sp.getITurnoAulaPersistente();
            List shiftList = sp.getITurmaTurnoPersistente().readByClass(domainClass);
            Iterator iterator = shiftList.iterator();
            infoLessonList = new ArrayList();

            infoExecutionPeriod = domainClass.getExecutionPeriod();
            
            while (iterator.hasNext()) {
                ITurno shift = (ITurno) iterator.next();
                InfoShift infoShift = copyIShift2InfoShift(shift);
                InfoExecutionCourse infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(shift
                        .getDisciplinaExecucao());
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                //List lessonList = shiftLessonDAO.readByShift(shift);
                List lessonList = shift.getAssociatedLessons();
                Iterator lessonIterator = lessonList.iterator();
                while (lessonIterator.hasNext()) {
                    IAula elem = (IAula) lessonIterator.next();
                    InfoLesson infoLesson = copyILesson2InfoLesson(elem);

                    if (infoLesson != null) {
                        infoLesson.setInfoShift(infoShift);

                        infoLesson.getInfoShiftList().add(infoShift);
                        infoLessonList.add(infoLesson);
                    }

                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        component.setLessons(infoLessonList);
        component.setExecutionPeriod(infoExecutionPeriod);

        return component;
    }

    private InfoLesson copyILesson2InfoLesson(IAula lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.setIdInternal(lesson.getIdInternal());
            infoLesson.setDiaSemana(lesson.getDiaSemana());
            infoLesson.setFim(lesson.getFim());
            infoLesson.setInicio(lesson.getInicio());
            infoLesson.setTipo(lesson.getTipo());
            //infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));

            InfoRoomOccupation infoRoomOccupation = Cloner.copyIRoomOccupation2InfoRoomOccupation(lesson
                    .getRoomOccupation());
            infoLesson.setInfoRoomOccupation(infoRoomOccupation);
        }
        return infoLesson;
    }

    /**
     * @param executionCourse
     * @return
     */
    private InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(IExecutionCourse executionCourse) {
        InfoExecutionCourse infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.setIdInternal(executionCourse.getIdInternal());
            infoExecutionCourse.setNome(executionCourse.getNome());
            infoExecutionCourse.setSigla(executionCourse.getSigla());
            infoExecutionCourse
                    .setInfoExecutionPeriod(copyIExecutionPeriod2InfoExecutionPeriod(executionCourse
                            .getExecutionPeriod()));
        }
        return infoExecutionCourse;
    }

    /**
     * @param shift
     * @return
     */
    private InfoShift copyIShift2InfoShift(ITurno shift) {
        InfoShift infoShift = null;
        if (shift != null) {
            infoShift = new InfoShift();
            infoShift.setIdInternal(shift.getIdInternal());
            infoShift.setNome(shift.getNome());
        }
        return infoShift;
    }

    /**
     * @param taux
     * @return
     */
    private InfoClass copyClass2InfoClass(ITurma taux) {
        InfoClass infoClass = null;
        if (taux != null) {
            infoClass = new InfoClass();
            infoClass.setIdInternal(taux.getIdInternal());
            infoClass.setNome(taux.getNome());
            infoClass.setAnoCurricular(taux.getAnoCurricular());
            infoClass.setInfoExecutionPeriod(copyIExecutionPeriod2InfoExecutionPeriod(taux
                    .getExecutionPeriod()));
        }
        return infoClass;
    }

    /**
     * @param period
     * @return
     */
    private InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(IExecutionPeriod period) {
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (period != null) {
            infoExecutionPeriod = new InfoExecutionPeriod();
            infoExecutionPeriod.setIdInternal(period.getIdInternal());
            infoExecutionPeriod.setName(period.getName());
            infoExecutionPeriod.setState(period.getState());
            infoExecutionPeriod.setBeginDate(period.getBeginDate());
            infoExecutionPeriod.setEndDate(period.getEndDate());
            infoExecutionPeriod.setSemester(period.getSemester());
        }
        return infoExecutionPeriod;
    }
}