package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.onlineTests.Test;

public class EditTest extends FenixService {

    public void run(Integer executionCourseId, Integer testId, String title, String information) {
        Test test = rootDomainObject.readTestByOID(testId);
        test.setTitle(title);
        test.setInformation(information);
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }

}
