/*
 * Created on 21/Jul/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSummary;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISala;
import Dominio.ISite;
import Dominio.ISummary;
import Dominio.ITurno;
import Dominio.Summary;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * @author Susana Fernandes modified by Tânia Pousão at 28/Abril/2004
 *         21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 */
public class ReadSummary implements IServico {

    private static ReadSummary service = new ReadSummary();

    public static ReadSummary getService() {

        return service;
    }

    /**
     *  
     */
    public ReadSummary() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadSummary";
    }

    public SiteView run(Integer executionCourseId, Integer summaryId)
            throws FenixServiceException {
        SiteView siteView;
        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new FenixServiceException("no.executioncourse");
            }

            IPersistentSite persistentSite = sp.getIPersistentSite();
            ISite site = persistentSite.readByExecutionCourse(executionCourse);
            if (site == null) {
                throw new FenixServiceException("no.site");
            }

            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            List shifts = persistentShift
                    .readByExecutionCourse(executionCourse);
            List infoShifts = new ArrayList();
            if (shifts != null && shifts.size() > 0) {
                infoShifts = (List) CollectionUtils.collect(shifts,
                        new Transformer() {

                            public Object transform(Object arg0) {
                                ITurno turno = (ITurno) arg0;
                                return Cloner.copyShift2InfoShift(turno);
                            }
                        });
            }

            IPersistentProfessorship persistentProfessorship = sp
                    .getIPersistentProfessorship();
            List professorships = persistentProfessorship
                    .readByExecutionCourse(executionCourse);
            List infoProfessorships = new ArrayList();
            if (professorships != null && professorships.size() > 0) {
                infoProfessorships = (List) CollectionUtils.collect(
                        professorships, new Transformer() {

                            public Object transform(Object arg0) {
                                IProfessorship professorship = (IProfessorship) arg0;
                                return Cloner
                                        .copyIProfessorship2InfoProfessorship(professorship);
                            }
                        });
            }

            ISalaPersistente persistentRoom = sp.getISalaPersistente();
            List rooms = persistentRoom.readAll();
            List infoRooms = new ArrayList();
            if (rooms != null && rooms.size() > 0) {
                infoRooms = (List) CollectionUtils.collect(rooms,
                        new Transformer() {

                            public Object transform(Object arg0) {
                                ISala room = (ISala) arg0;
                                return Cloner.copyRoom2InfoRoom(room);
                            }
                        });
            }
            Collections.sort(infoRooms, new BeanComparator("nome"));

            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            ISummary summary = (ISummary) persistentSummary.readByOID(
                    Summary.class, summaryId);
            if (summary == null) {
                throw new FenixServiceException("no.summary");
            }
            if (summary.getShift() == null) {
                findBestShiftForSummary(summary, shifts);
                if (summary.getShift() == null) {
                    throw new FenixServiceException();
                }
            }

            InfoSiteSummary bodyComponent = new InfoSiteSummary();
            bodyComponent.setInfoSummary(Cloner
                    .copyISummary2InfoSummary(summary));
            bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner
                    .get(executionCourse));
            bodyComponent.setInfoShifts(infoShifts);
            bodyComponent.setInfoProfessorships(infoProfessorships);
            bodyComponent.setInfoRooms(infoRooms);

            TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                    .getInstance();
            ISiteComponent commonComponent = componentBuilder.getComponent(
                    new InfoSiteCommon(), site, null, null, null);

            siteView = new ExecutionCourseSiteView(commonComponent,
                    bodyComponent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return siteView;
    }

    /**
     * @param summary
     */
    private void findBestShiftForSummary(ISummary summary, List shifts) {
        //choose the shift with:
        // 1. the same summary type / lesson type
        // 2. the lesson with date and hour of the summary

        if (summary.getSummaryDate() != null
                && summary.getSummaryHour() != null && shifts != null
                && shifts.size() >= 0) {
            Calendar dateAndHourSummary = Calendar.getInstance();
            dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summary
                    .getSummaryDate().get(Calendar.DAY_OF_MONTH));
            dateAndHourSummary.set(Calendar.MONTH, summary.getSummaryDate()
                    .get(Calendar.MONTH));
            dateAndHourSummary.set(Calendar.YEAR, summary.getSummaryDate().get(
                    Calendar.YEAR));
            dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summary
                    .getSummaryHour().get(Calendar.HOUR_OF_DAY));
            dateAndHourSummary.set(Calendar.MINUTE, summary.getSummaryHour()
                    .get(Calendar.MINUTE));
            dateAndHourSummary.set(Calendar.SECOND, 00);

            Calendar beginLesson = Calendar.getInstance();
            beginLesson.set(Calendar.DAY_OF_MONTH, summary.getSummaryDate()
                    .get(Calendar.DAY_OF_MONTH));
            beginLesson.set(Calendar.MONTH, summary.getSummaryDate().get(
                    Calendar.MONTH));
            beginLesson.set(Calendar.YEAR, summary.getSummaryDate().get(
                    Calendar.YEAR));

            Calendar endLesson = Calendar.getInstance();
            endLesson.set(Calendar.DAY_OF_MONTH, summary.getSummaryDate().get(
                    Calendar.DAY_OF_MONTH));
            endLesson.set(Calendar.MONTH, summary.getSummaryDate().get(
                    Calendar.MONTH));
            endLesson.set(Calendar.YEAR, summary.getSummaryDate().get(
                    Calendar.YEAR));

            ListIterator iteratorShift = shifts.listIterator();
            while (iteratorShift.hasNext()) {
                ITurno shift = (ITurno) iteratorShift.next();
                if (summary.getSummaryType() != null && shift.getTipo() != null
                        && !summary.getSummaryType().equals(shift.getTipo())) {
                    continue;
                }

                if (shift.getAssociatedLessons() != null
                        && shift.getAssociatedLessons().size() > 0) {
                    ListIterator iteratorLesson = shift.getAssociatedLessons()
                            .listIterator();
                    while (iteratorLesson.hasNext()) {
                        IAula lesson = (IAula) iteratorLesson.next();

                        beginLesson.set(Calendar.HOUR_OF_DAY, lesson
                                .getInicio().get(Calendar.HOUR_OF_DAY));
                        beginLesson.set(Calendar.MINUTE, lesson.getInicio()
                                .get(Calendar.MINUTE));
                        beginLesson.set(Calendar.SECOND, 00);

                        endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim()
                                .get(Calendar.HOUR_OF_DAY));
                        endLesson.set(Calendar.MINUTE, lesson.getFim().get(
                                Calendar.MINUTE));
                        endLesson.set(Calendar.SECOND, 00);

                        if (dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson
                                .getDiaSemana().getDiaSemana().intValue()
                                && !beginLesson.after(dateAndHourSummary)
                                && endLesson.after(dateAndHourSummary)) {
                            summary.setShift(shift);
                            summary.setIsExtraLesson(Boolean.FALSE);
                            summary.setRoom(lesson.getSala());
                            return;
                        }
                    }
                }
            }

            //If the execution to arrive until here,
            //then was impossible attribute a shift to the summary
            summary.setIsExtraLesson(Boolean.TRUE);
            summary.setShift((ITurno) shifts.get(0));
        }
    }
}