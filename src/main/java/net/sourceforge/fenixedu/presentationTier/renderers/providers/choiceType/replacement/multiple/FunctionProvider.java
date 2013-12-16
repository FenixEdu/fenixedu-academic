package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;

import org.apache.commons.collections.Predicate;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FunctionProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return CollectionUtils.select(Bennu.getInstance().getAccountabilityTypesSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return isFunction((AccountabilityType) arg0);
            }

        });
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    private boolean isFunction(final AccountabilityType type) {
        return type instanceof net.sourceforge.fenixedu.domain.organizationalStructure.Function;
    }

}
