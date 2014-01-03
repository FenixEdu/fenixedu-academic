package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import java.util.List;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.Pair;

public class ExecuteProcessActivity {

    @Atomic
    static public Process run(final Process process, final String activityId, final Object object) {
        return process.executeActivity(Authenticate.getUser(), activityId, object);
    }

    @Atomic
    static public Process run(final Process process, final Class<?> clazz, final Object object) {
        return run(Authenticate.getUser(), process, clazz, object);
    }

    @Atomic
    static public Process run(final User userView, final Process process, final Class<?> clazz, final Object object) {
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
    @Atomic
    static public Process run(final Process process, final List<Pair<String, Object>> activities) {
        final User userView = Authenticate.getUser();
        for (final Pair<String, Object> activity : activities) {
            process.executeActivity(userView, activity.getKey(), activity.getValue());
        }

        return process;
    }

}