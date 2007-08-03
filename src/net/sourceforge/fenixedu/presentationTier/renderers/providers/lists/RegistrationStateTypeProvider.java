package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class RegistrationStateTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
//        List<RegistrationAgreement> types = new ArrayList<RegistrationAgreement>();
//
//        for (RegistrationAgreement typeToDisplay : RegistrationAgreement.values()) {
//            types.add(typeToDisplay);
//        }
//
//        return types;
    	return Arrays.asList(RegistrationStateType.values());
    }

    public Converter getConverter() {
    	return new Converter() {
    	    @Override
    	    public Object convert(Class type, Object value) {
    		final List<RegistrationStateType> registrationStateTypes = new ArrayList<RegistrationStateType>();
    		for (final String o : (String[]) value) {
    			registrationStateTypes.add(RegistrationStateType.valueOf(o));
    		}
    		return registrationStateTypes;
    	    }
    	};
    }

}