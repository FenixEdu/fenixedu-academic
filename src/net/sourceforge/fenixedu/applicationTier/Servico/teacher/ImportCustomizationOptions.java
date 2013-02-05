package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;

public class ImportCustomizationOptions extends FenixService {

    public void run(Integer executionCourseID, ExecutionCourseSite siteTo, ExecutionCourseSite siteFrom) {
        if (siteTo != null && siteFrom != null) {
            siteTo.copyCustomizationOptionsFrom(siteFrom);
        }
    }

}
