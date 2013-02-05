package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class MigrationExecutionYearsProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
        return ExecutionYear.readExecutionYears(ExecutionYear.readExecutionYearByName("1980/1981"),
                ExecutionYear.readCurrentExecutionYear());
    }

}
