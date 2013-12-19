package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.AuthScope;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AuthScopeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return new ArrayList<AuthScope>(Bennu.getInstance().getAuthScopesSet());
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
