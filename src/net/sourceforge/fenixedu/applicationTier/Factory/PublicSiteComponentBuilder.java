/*
 * Created on 5/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteClasses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
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

    public ISiteComponent getComponent(ISiteComponent component, SchoolClass domainClass)
            throws FenixServiceException, ExcepcaoPersistencia {

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
     * @throws ExcepcaoPersistencia 
     */
    private ISiteComponent getInfoSiteClasses(InfoSiteClasses component, SchoolClass domainClass)
            throws FenixServiceException, ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod = domainClass.getExecutionPeriod();
        ExecutionDegree executionDegree = domainClass.getExecutionDegree();

        Set<SchoolClass> classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionPeriod, domainClass.getAnoCurricular());
        List infoClasses = new ArrayList();
        for (final SchoolClass schoolClass : classes) {
        	infoClasses.add(copyClass2InfoClass(schoolClass));
        }

        component.setClasses(infoClasses);
        return component;
    }

    /**
     * @param timetable
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component, SchoolClass domainClass)
            throws FenixServiceException {
        List infoLessonList = null;

        List<Shift> shiftList = domainClass.getAssociatedShifts();
        infoLessonList = new ArrayList();

        ExecutionPeriod executionPeriod = domainClass.getExecutionPeriod();
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                .newInfoFromDomain(executionPeriod);

        for (final Iterator classIterator = shiftList.iterator(); classIterator.hasNext();) {
            Shift shift = (Shift) classIterator.next();

            InfoShift infoShift = copyIShift2InfoShift(shift);
            InfoExecutionCourse infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(shift
                    .getDisciplinaExecucao());
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

            List lessonList = shift.getAssociatedLessons();
            Iterator lessonIterator = lessonList.iterator();
            while (lessonIterator.hasNext()) {
                Lesson elem = (Lesson) lessonIterator.next();
                infoLessonList.add(InfoLesson.newInfoFromDomain(elem));
            }
        }
        component.setInfoExecutionPeriod(infoExecutionPeriod);

        component.setLessons(infoLessonList);

        return component;
    }

    private InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(ExecutionCourse executionCourse) {
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
    private InfoShift copyIShift2InfoShift(Shift shift) {
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
    private InfoClass copyClass2InfoClass(SchoolClass taux) {
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
    private InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(ExecutionPeriod period) {
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