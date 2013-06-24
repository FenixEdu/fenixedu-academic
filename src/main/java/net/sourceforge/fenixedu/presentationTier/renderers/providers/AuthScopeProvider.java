package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.AuthScope;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.services.Service;

public class AuthScopeProvider implements DataProvider {

    final static String PERSONAL_SCOPE = "personal info";
    final static String CURRICULUM_SCOPE = "curriculum";
    final static String SCHEDULE_SCOPE = "schedule info";
    final static String REGISTRATIONS_SCOPE = "registrations";

    @Override
    public Object provide(Object source, Object currentValue) {

        if (!RootDomainObject.getInstance().hasAnyAuthScopes()) {
            createAuthScopes();
        }

        List<AuthScope> scopes = new ArrayList<AuthScope>();
        scopes.addAll(RootDomainObject.getInstance().getAuthScopes());
        return scopes;
    }

    @Service
    private void createAuthScopes() {
        AuthScope a = new AuthScope();
        a.setName(PERSONAL_SCOPE);
        a.setEndpoints("xpto,ypto");

        AuthScope b = new AuthScope();
        b.setName(CURRICULUM_SCOPE);
        b.setEndpoints("xpto,ypto");

        AuthScope c = new AuthScope();
        c.setName(SCHEDULE_SCOPE);
        c.setEndpoints("xpto,ypto");

        AuthScope d = new AuthScope();
        d.setName(REGISTRATIONS_SCOPE);
        d.setEndpoints("xpto,ypto");
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
