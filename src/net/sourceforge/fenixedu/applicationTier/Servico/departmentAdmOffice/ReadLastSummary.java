package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.commons.beanutils.BeanComparator;

public class ReadLastSummary extends Service {

    public InfoSummary run(Integer executionCourseId, Integer shiftId, Integer lessonID)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoSummary summary = null;

        Shift shift = rootDomainObject.readShiftByOID(shiftId);
        if (shift == null)
            throw new FenixServiceException("no.shift");

        Lesson aula = rootDomainObject.readLessonByOID(lessonID);
        if (aula == null)
            throw new FenixServiceException("no.lesson");

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executioncourse");
        }

        List<Summary> summaries = new ArrayList<Summary>(shift.getAssociatedSummaries());
        if (summaries != null && summaries.size() > 0) {
            Comparator comparator = new BeanComparator("summaryDate.time");
            Collections.sort(summaries, comparator);
            DiaSemana diaSemana = aula.getDiaSemana();
            Calendar summaryDate = Calendar.getInstance();
            int summaryDayOfWeek;
            for (int i = summaries.size() - 1; i >= 0; i--) {
                Summary summary1 = (Summary) summaries.get(i);
                summaryDate.setTime(summary1.getSummaryDate());
                summaryDayOfWeek = summaryDate.get(Calendar.DAY_OF_WEEK);
                if (summaryDayOfWeek == diaSemana.getDiaSemana().intValue()) {
                    summary = InfoSummary.newInfoFromDomain(summary1);
                    break;
                }
            }
        }
        return summary;
    }
    
}
