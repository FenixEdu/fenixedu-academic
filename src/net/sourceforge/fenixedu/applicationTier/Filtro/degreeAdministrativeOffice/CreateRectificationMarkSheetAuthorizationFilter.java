package net.sourceforge.fenixedu.applicationTier.Filtro.degreeAdministrativeOffice;

import java.util.Set;

import net.sourceforge.fenixedu.predicates.MarkSheetPredicates;

public class CreateRectificationMarkSheetAuthorizationFilter extends MarkSheetAuthorizationFilter {

    @Override
    public Set<String> getAuthorizedEmployees() {
	return MarkSheetPredicates.rectificationEmployees;
    }

}
