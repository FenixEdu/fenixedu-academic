package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentTimeTable {

    @Service
    public static List<InfoShowOccupation> run(final Registration registration) throws FenixServiceException {

        if (registration == null) {
            throw new FenixServiceException("error.service.readStudentTimeTable.noStudent");
        }

        final List<InfoShowOccupation> result = new ArrayList<InfoShowOccupation>();
        for (final Shift shift : registration.getShiftsForCurrentExecutionPeriod()) {
            result.addAll(InfoLessonInstanceAggregation.getAggregations(shift));
        }

        return result;
    }
}