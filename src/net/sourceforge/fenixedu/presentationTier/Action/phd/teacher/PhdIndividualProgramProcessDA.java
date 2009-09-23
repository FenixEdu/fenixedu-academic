package net.sourceforge.fenixedu.presentationTier.Action.phd.teacher;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.InternalGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdIndividualProgramProcess", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {

@Forward(name = "manageProcesses", path = "/phd/teacher/manageProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/teacher/viewProcess.jsp"),

@Forward(name = "viewAlertMessages", path = "/phd/teacher/viewAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/teacher/viewProcessAlertMessages.jsp")

})
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {

	final SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
	searchBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	searchBean.setFilterPhdPrograms(false);

	final List<PhdIndividualProgramProcess> processes = new ArrayList<PhdIndividualProgramProcess>();
	for (final InternalGuiding eachGuiding : getLoggedPerson(request).getInternalGuidings()) {
	    processes.add(eachGuiding.getPhdIndividualProgramProcess());
	}

	searchBean.setProcesses(processes);

	return searchBean;

    }

}
