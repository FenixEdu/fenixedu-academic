package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.AuthScope;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AuthScopeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        List<AuthScope> scopes = new ArrayList<AuthScope>();
        scopes.addAll(RootDomainObject.getInstance().getAuthScopes());
        return scopes;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
