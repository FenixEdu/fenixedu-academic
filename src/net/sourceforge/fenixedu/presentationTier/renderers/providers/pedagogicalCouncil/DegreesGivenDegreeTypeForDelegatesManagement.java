package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil;

import net.sourceforge.fenixedu.dataTransferObject.commons.delegates.DelegateSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.delegates.DelegateBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreesGivenDegreeTypeForDelegatesManagement implements DataProvider{
	
	public Object provide(Object source, Object currentValue) {
		
		if(source instanceof DelegateBean) {
			return Degree.readAllByDegreeType(((DelegateBean)source).getDegreeType());
		}
		else {
			return Degree.readAllByDegreeType(((DelegateSearchBean)source).getDegreeType());
		}
		
    }

	public Converter getConverter() {
        return new DomainObjectKeyConverter();  
    }
}
