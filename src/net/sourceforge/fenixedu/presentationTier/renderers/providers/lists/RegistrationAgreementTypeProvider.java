package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class RegistrationAgreementTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

    	return Arrays.asList(RegistrationAgreement.values());
    }

    public Converter getConverter() {
    	return new Converter() {
    	    @Override
    	    public Object convert(Class type, Object value) {
    		final List<RegistrationAgreement> registrationAgreements = new ArrayList<RegistrationAgreement>();
    		for (final String o : (String[]) value) {
    			registrationAgreements.add(RegistrationAgreement.valueOf(o));
    		}
    		return registrationAgreements;
    	    }
    	};
    }

}