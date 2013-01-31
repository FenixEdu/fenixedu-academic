package net.sourceforge.fenixedu.presentationTier.Action.phd.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdProgramsAssociatedToCoordinator implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object arg0, Object arg1) {
		List<PhdProgram> programs = new ArrayList<PhdProgram>();

		for (PhdProgram program : RootDomainObject.getInstance().getPhdPrograms()) {
			if (program.isCoordinatorFor(AccessControl.getPerson(), ExecutionYear.readCurrentExecutionYear())) {
				programs.add(program);
			}
		}

		return programs;
	}

}
