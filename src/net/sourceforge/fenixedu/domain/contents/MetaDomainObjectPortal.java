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
    public void setNormalizedName(final MultiLanguageString normalizedName) {
	// unable to optimize because we cannot track changes to name correctly.
	// don't call super.setNormalizedName() !
    }

    @Override
    public void addPathContentsForTrailingPath(final List<Content> contents, final String trailingPath) {
	AbstractPathProcessor strategy = getStrategy();
	final Container container = (Container) strategy.processPath(trailingPath);
	if(container == null) {
	    throw new InvalidContentPathException(this,trailingPath);
	}

	contents.add(container);
	final int size = contents.size();
	String newTrailingPath = strategy.getTrailingPath(trailingPath);
	container.addPathContentsForTrailingPath(contents, newTrailingPath);
	
	if (contents.size() == size && !(newTrailingPath.length() == 0 || 
		(newTrailingPath.length() == 1 && newTrailingPath.charAt(0) == '/'))) {
	    throw new InvalidContentPathException(this,newTrailingPath);
	}
    }

    @Override
    public void addPathContents(List<Content> contents, String path) {
	super.addPathContents(contents, path);
	String subPath = getSubPathForSearch(path);
	if (matchesPath(subPath) && !(subPath.length() + 1 < path.length())) {
	    Content initialContent = getStrategy().getInitialContent();
	    if (initialContent != null) {
		contents.add(initialContent);
	    }
	}
    }

    public AbstractPathProcessor getStrategy() {
	return AbstractPathProcessor.findStrategyFor(this.getMetaDomainObject().getType());
    }

    public boolean isContentPoolAvailable() {
	return getPoolCount() > 0;
    }
}
