package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;

public class ReadStudentTimeTable extends Service {

    public List<InfoLesson> run(Student student) throws FenixServiceException {

        if (student == null) {
        	throw new FenixServiceException("error.service.readStudentTimeTable.noStudent");
        }
        
        final List<InfoLesson> result = new ArrayList<InfoLesson>();
        for (final Shift shift : student.getShiftsForCurrentExecutionPeriod()) {
        	for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
        		result.add(InfoLesson.newInfoFromDomain(lesson));
        	}
        }

        return result;
    }
}