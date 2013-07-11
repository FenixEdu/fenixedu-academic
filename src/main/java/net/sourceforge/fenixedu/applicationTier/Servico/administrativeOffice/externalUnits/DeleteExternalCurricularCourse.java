package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;


import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import pt.ist.fenixframework.Atomic;

public class DeleteExternalCurricularCourse {

    @Atomic
    public static void run(final ExternalCurricularCourse externalCurricularCourse) {
        externalCurricularCourse.delete();
    }
}