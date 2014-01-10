package net.sourceforge.fenixedu.presentationTier.Action.phd.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;

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

        for (PhdProgram program : Bennu.getInstance().getPhdProgramsSet()) {
            if (program.isCoordinatorFor(AccessControl.getPerson(), ExecutionYear.readCurrentExecutionYear())) {
                programs.add(program);
            }
        }

        return programs;
    }

}
