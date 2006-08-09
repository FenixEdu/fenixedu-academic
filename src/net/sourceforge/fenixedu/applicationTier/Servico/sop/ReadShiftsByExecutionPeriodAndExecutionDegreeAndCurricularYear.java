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

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear extends Service {

    public Object run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) throws ExcepcaoPersistencia {

    	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final CurricularYear curricularYear = rootDomainObject.readCurricularYearByOID(infoCurricularYear.getIdInternal());

        final List infoShifts = new ArrayList();
        final List<ExecutionCourse> executionCourses = executionPeriod.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(degreeCurricularPlan, curricularYear, "%");
        for (final ExecutionCourse executionCourse : executionCourses) {
        for (final Shift shift : executionCourse.getAssociatedShiftsSet()) {
        	final InfoShift infoShift = new InfoShift();
            infoShift.setAvailabilityFinal(shift.getAvailabilityFinal());
            infoShift.setIdInternal(shift.getIdInternal());
            infoShift.setLotacao(shift.getLotacao());
            infoShift.setNome(shift.getNome());
            infoShift.setTipo(shift.getTipo());

            infoShift.setInfoLessons(new ArrayList());
            final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain((shift).getDisciplinaExecucao());
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
            for (final Lesson lesson : shift.getAssociatedLessons()) {
            	final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoShift.getInfoLessons().add(infoLesson);

            }
            infoShifts.add(infoShift);
        }
        }

        return infoShifts;

    }

}