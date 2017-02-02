package org.fenixedu.academic.ui.renderers.providers.choiceType.replacement.multiple;

import java.util.stream.Collectors;

import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ProgramConclusionProvider extends AbstractDomainObjectProvider {
    @Override
    public Object provide(Object source, Object currentValue) {
    	return Bennu.getInstance().getProgramConclusionSet().stream().collect(Collectors.toList());
    }
    
    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }
}
