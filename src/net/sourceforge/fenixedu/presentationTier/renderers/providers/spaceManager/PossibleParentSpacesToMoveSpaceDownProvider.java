package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MoveSpaceBean;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class PossibleParentSpacesToMoveSpaceDownProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {	
	MoveSpaceBean bean = (MoveSpaceBean) source;			
	Space space = bean.getSpace();
	if(space != null) {
	    return space.getPossibleParentSpacesToMoveSpaceDown();	    
	}	
	return new ArrayList<Space>();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
