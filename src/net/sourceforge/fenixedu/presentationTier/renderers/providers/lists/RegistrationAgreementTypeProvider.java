package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.Arrays;

import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RegistrationAgreementTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	return Arrays.asList(RegistrationAgreement.values());
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}