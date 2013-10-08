package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.AuthScope;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class AuthScopeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return new ArrayList<AuthScope>(RootDomainObject.getInstance().getAuthScopes());
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
