package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.FindSpacesBean;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class BuildingProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {	
	
	Set<Building> result = new HashSet<Building>();
	FindSpacesBean bean = (FindSpacesBean) source;
	Campus campus = bean.getCampus();
	
	if(campus != null) {
	    return campus.getActiveContainedSpacesByType(Building.class);	    
	}	
	
	return result;
    }

    public Converter getConverter() {	
	return new DomainObjectKeyConverter();
    }
}
