package net.sourceforge.fenixedu.domain.contents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The <code>Element</code> is the leaf of the content's tree. Elements
 * normally are atomic, as in not composed, pieces of information that usually
 * can be identified by it's name.
 * 
 * @author cfgi
 * @author lpec
 * @author pcma
 */
public abstract class Element extends Element_Base {

    public Element() {
	super();
    }

    @Override
    public boolean isParentAccepted(Container parent) {
	return true;
    }

    @Override
    public List<Content> getPathTo(Content target) {
	if (this.equals(target)) {
	    List<Content> contents = new ArrayList<Content>();
	    contents.add((Content) target);
	    return contents;
	} else {
	    return Collections.emptyList();
	}
    }

    @Override
    public final Collection<MenuEntry> getMenu() {
	return Collections.emptyList();
    }

    @Override
    public boolean isContainer() {
	return false;
    }

    @Override
    public boolean isElement() {
	return true;
    }
}
