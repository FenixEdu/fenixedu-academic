package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.OutOfPeriodFilterException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class SubmitMarksPeriodFilter extends Filtro {

    @Override
    public void execute(Object[] parameters) throws Exception {
        String startString = "2006-01-04";
        String endString = "2006-02-25";
        Date start = DateFormatUtil.parse("yyyy-MM-dd", startString);
        Date end = DateFormatUtil.parse("yyyy-MM-dd", endString);
        Date now = new Date();
        Integer executionPeriodId = 83;

        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID((Integer) parameters[0]);
        if (executionCourse != null) {
            if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodId)) {
                if (now.after(start) && now.before(end)) {
                    return;
                }
            }
        }
        throw new OutOfPeriodFilterException("error.submitMarks.outOfPeriod", null, null);
    }
}
