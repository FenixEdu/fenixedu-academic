package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteGrade {
    @Service
    public static void run(NewQuestion question) throws FenixServiceException {
        question.setGrade(null);
    }
}