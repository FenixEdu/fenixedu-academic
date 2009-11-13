package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdIndividualProgramProcess", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "manageProcesses", path = "/phd/coordinator/manageProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/coordinator/viewProcess.jsp"),

@Forward(name = "viewAlertMessages", path = "/phd/coordinator/viewAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/coordinator/viewProcessAlertMessages.jsp")

})
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
	SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
	searchBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	searchBean.setPhdPrograms(getManagedPhdPrograms(request));
	searchBean.setFilterPhdProcesses(false);

	return searchBean;
    }

    private Set<PhdProgram> getManagedPhdPrograms(HttpServletRequest request) {
	final Set<PhdProgram> result = new HashSet<PhdProgram>();

	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	for (final Coordinator coordinator : getLoggedPerson(request).getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegree().hasPhdProgram()
		    && coordinator.getExecutionDegree().getExecutionYear() == currentExecutionYear) {
		result.add(coordinator.getExecutionDegree().getDegree().getPhdProgram());
	    }
	}

	return result;
    }

}
