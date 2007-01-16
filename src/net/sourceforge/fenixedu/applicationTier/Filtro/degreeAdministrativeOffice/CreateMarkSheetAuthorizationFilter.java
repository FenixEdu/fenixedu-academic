package net.sourceforge.fenixedu.applicationTier.Filtro.degreeAdministrativeOffice;

import java.util.HashSet;
import java.util.Set;

public class CreateMarkSheetAuthorizationFilter extends MarkSheetAuthorizationFilter {

    @Override
    public Set<String> getAuthorizedEmployees() {
        Set<String> authorizedEmployees = new HashSet<String>();
        authorizedEmployees.add("1272");
        authorizedEmployees.add("2339");
        authorizedEmployees.add("1268");
        authorizedEmployees.add("2675");
        authorizedEmployees.add("2232");
        authorizedEmployees.add("4065");
        authorizedEmployees.add("4280");
        authorizedEmployees.add("3978");
        
        authorizedEmployees.add("3068");
        authorizedEmployees.add("2973");
        
        return authorizedEmployees;
    }

}
