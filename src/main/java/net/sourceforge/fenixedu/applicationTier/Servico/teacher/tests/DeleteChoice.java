package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteChoice {
    @Service
    public static void run(NewChoice choice) throws FenixServiceException {
        choice.delete();
    }
}