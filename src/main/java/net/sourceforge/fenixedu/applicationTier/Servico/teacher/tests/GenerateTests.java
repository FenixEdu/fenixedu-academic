package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class GenerateTests {
    @Atomic
    public static void run(NewTestModel testModel, String name, ExecutionCourse executionCourse, Integer variations,
            DateTime finalDate) throws FenixServiceException {
        testModel.generateTests(name, executionCourse, variations, finalDate);
    }
}