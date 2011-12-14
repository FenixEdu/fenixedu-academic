package net.sourceforge.fenixedu.presentationTier.renderers.providers;


import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

abstract public class AbstractDomainObjectProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
