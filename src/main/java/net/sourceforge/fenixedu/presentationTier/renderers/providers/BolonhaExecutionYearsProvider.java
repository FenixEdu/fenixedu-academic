package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public class BolonhaExecutionYearsProvider extends OpenExecutionYearsProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final ExecutionYear bolonhaYear = ExecutionYear.readExecutionYearByName("2006/2007");
        final List<ExecutionYear> bolonhaYears = new ArrayList<ExecutionYear>();

        for (ExecutionYear year : (List<ExecutionYear>) super.provide(source, currentValue)) {
            if (year.isAfterOrEquals(bolonhaYear)) {
                bolonhaYears.add(year);
            }
        }
        return bolonhaYears;
    }

}
