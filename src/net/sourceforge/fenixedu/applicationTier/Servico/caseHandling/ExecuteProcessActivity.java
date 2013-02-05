package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Pair;

public class ExecuteProcessActivity extends FenixService {

    @Service
    static public Process run(final Process process, final String activityId, final Object object) {
        return process.executeActivity(AccessControl.getUserView(), activityId, object);
    }

    @Service
    static public Process run(final Process process, final Class<?> clazz, final Object object) {
        return run(AccessControl.getUserView(), process, clazz, object);
    }

    @Service
    static public Process run(final IUserView userView, final Process process, final Class<?> clazz, final Object object) {
        return process.executeActivity(userView, clazz.getSimpleName(), object);
    }

    /**
     * <pre>
     * Pair&lt;String, Object&gt;
     * - left: activity id
     * - right: activity arg
     * </pre>
     * 
     * @param process
     * @param activities
     * @return
     */
    @Service
    static public Process run(final Process process, final List<Pair<String, Object>> activities) {
        final IUserView userView = AccessControl.getUserView();
        for (final Pair<String, Object> activity : activities) {
            process.executeActivity(userView, activity.getKey(), activity.getValue());
        }

        return process;
    }

}