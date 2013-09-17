package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;

public class ReadStudentTimeTable {

    @Atomic
    public static List<InfoShowOccupation> run(Registration registration, ExecutionSemester executionSemester)
            throws FenixServiceException {

        if (registration == null) {
            throw new FenixServiceException("error.service.readStudentTimeTable.noStudent");
        }
        if (executionSemester == null) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        }

        final List<InfoShowOccupation> result = new ArrayList<InfoShowOccupation>();
        for (final Shift shift : registration.getShiftsFor(executionSemester)) {
            result.addAll(InfoLessonInstanceAggregation.getAggregations(shift));

        }

        return result;
    }
}