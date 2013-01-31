package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Pair;

public class CreateNewProcess extends FenixService {

	@Service
	public static Process run(String processName, Object object) {
		return Process.createNewProcess(AccessControl.getUserView(), processName, object);
	}

	@Service
	public static Process run(Class<? extends Process> processClass, Object object) {
		return Process.createNewProcess(AccessControl.getUserView(), processClass, object);
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
	@Service
	public static Process run(Class<? extends Process> processClass, Object object, final List<Pair<Class<?>, Object>> activities) {
		final IUserView userView = AccessControl.getUserView();
		final Process process = Process.createNewProcess(userView, processClass, object);

		for (final Pair<Class<?>, Object> activity : activities) {
			process.executeActivity(userView, activity.getKey().getSimpleName(), activity.getValue());
		}

		return process;
	}
}