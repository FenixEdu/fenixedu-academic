package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentTimeTable {

    @Service
    public static List<InfoLesson> run(Registration registration) throws FenixServiceException {

        if (registration == null) {
            throw new FenixServiceException("error.service.readStudentTimeTable.noStudent");
        }

        final List<InfoLesson> result = new ArrayList<InfoLesson>();
        for (final Shift shift : registration.getShiftsForCurrentExecutionPeriod()) {
            for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
                result.add(InfoLesson.newInfoFromDomain(lesson));
            }
        }

        return result;
    }
}