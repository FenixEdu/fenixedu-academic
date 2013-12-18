package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import java.util.List;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.Pair;

public class CreateNewProcess {

    @Atomic
    public static Process run(String processName, Object object) {
        return Process.createNewProcess(Authenticate.getUser(), processName, object);
    }

    @Atomic
    public static Process run(Class<? extends Process> processClass, Object object) {
        return Process.createNewProcess(Authenticate.getUser(), processClass, object);
    }

    /**
     * *
     * 
     * <pre>
     * Args:
     * - processClass to create new
     * - object used to create new process
     * - List of Pair&lt;Class, Object&gt;
     * 		- left: activity class
     * 		- right: activity arg
     * </pre>
     * 
     * @param processClass
     * @param object
     * @param activities
     * @return
     */
    @Atomic
    public static Process run(Class<? extends Process> processClass, Object object, final List<Pair<Class<?>, Object>> activities) {
        final User userView = Authenticate.getUser();
        final Process process = Process.createNewProcess(userView, processClass, object);

        for (final Pair<Class<?>, Object> activity : activities) {
            process.executeActivity(userView, activity.getKey().getSimpleName(), activity.getValue());
        }

        return process;
    }
}