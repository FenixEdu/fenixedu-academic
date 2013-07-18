package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;


import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteExternalCurricularCourse {

    @Service
    public static void run(final ExternalCurricularCourse externalCurricularCourse) {
        externalCurricularCourse.delete();
    }
}