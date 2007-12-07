package net.sourceforge.fenixedu.domain.contents;

import java.util.List;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.contents.pathProcessors.AbstractPathProcessor;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class MetaDomainObjectPortal extends MetaDomainObjectPortal_Base {

    public MetaDomainObjectPortal(MetaDomainObject metaDomainObject) {
	super();
	setMetaDomainObject(metaDomainObject);
    }

    @Override
    public MultiLanguageString getName() {
	return super.getName() != null ? super.getName() : new MultiLanguageString(getMetaDomainObject().getType());
    } 
   
    @Override
    public void addPathContentsForTrailingPath(final List<Content> contents, final String trailingPath) {
	super.addPathContentsForTrailingPath(contents, trailingPath);
	AbstractPathProcessor strategy = getStrategy();
	final Container container = (Container) strategy.processPath(trailingPath);
	contents.add(container);
	container.addPathContentsForTrailingPath(contents, strategy.getTrailingPath(trailingPath));
    }

    public AbstractPathProcessor getStrategy() {
	return AbstractPathProcessor.findStrategyFor(this.getMetaDomainObject().getType());
    }
    
    public boolean isContentPoolAvailable() {
	return getPoolCount() > 0;
    }
}
