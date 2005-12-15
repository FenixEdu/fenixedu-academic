package relations;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;

public class AdviseTeacherAdviseService extends AdviseTeacherAdviseService_Base {

    public static void add(net.sourceforge.fenixedu.domain.teacher.IAdvise advise,
            net.sourceforge.fenixedu.domain.teacher.ITeacherAdviseService teacherAdviseServices) {
        AdviseTeacherAdviseService_Base.add(advise, teacherAdviseServices);

        IExecutionPeriod executionPeriod = teacherAdviseServices.getTeacherService()
                .getExecutionPeriod();
        if (executionPeriod.getEndDate().after(advise.getEndExecutionPeriod().getEndDate())) {
            advise.setEndExecutionPeriod(executionPeriod);
        }
        if (executionPeriod.getBeginDate().before(advise.getStartExecutionPeriod().getEndDate())) {
            advise.setStartExecutionPeriod(executionPeriod);
        }
    }

    public static void remove(net.sourceforge.fenixedu.domain.teacher.IAdvise advise,
            net.sourceforge.fenixedu.domain.teacher.ITeacherAdviseService teacherAdviseServices) {

        IExecutionPeriod executionPeriod = teacherAdviseServices.getTeacherService().getExecutionPeriod();
        AdviseTeacherAdviseService_Base.remove(advise, teacherAdviseServices);
                
        if (advise.getTeacherAdviseServices() == null || advise.getTeacherAdviseServices().isEmpty()) {
            advise.delete();
        } else {
            if ( executionPeriod == advise.getEndExecutionPeriod()) {
                advise.updateEndExecutionPeriod();
            } else if (executionPeriod == advise.getStartExecutionPeriod()) {
                advise.updateStartExecutionPeriod();
            }
        }
    }
}
