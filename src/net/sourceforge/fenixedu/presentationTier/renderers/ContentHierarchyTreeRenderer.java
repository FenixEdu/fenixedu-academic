package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collections;
import java.util.Stack;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

import org.apache.commons.collections.Predicate;

public class ContentHierarchyTreeRenderer extends TreeRenderer {

    private boolean rootDragDisabled;

    private Stack<DomainObject> stack;

    private String parentParameterName;
    
    
    public String getParentParameterName() {
        return parentParameterName;
    }

    public void setParentParameterName(String parentParameterName) {
        this.parentParameterName = parentParameterName;
    }

    public ContentHierarchyTreeRenderer() {
	super();
	stack = new Stack<DomainObject>();
    }

    public boolean isRootDragDisabled() {
	return rootDragDisabled;
    }

    public void setRootDragDisabled(boolean rootDragDisabled) {
	this.rootDragDisabled = rootDragDisabled;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new ContentHierarchyTreeLayout();
    }

    protected class ContentHierarchyTreeLayout extends TreeLayout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {

	    Content content = (Content) object;
	    HtmlComponent component = super.createComponent(Collections.singletonList(content), type);

	    if (rootDragDisabled) {
		HtmlList list = (component instanceof HtmlList) ? (HtmlList) component
			: (HtmlList) ((HtmlBlockContainer) component).getChild(new Predicate() {

			    public boolean evaluate(Object htmlComponent) {
				return htmlComponent instanceof HtmlList;
			    }

			});

		list.getChildren().iterator().next().setAttribute("noDrag", "true");
	    }

	    return component;
	}
    }

    @Override
    protected HtmlComponent generateMainComponent(Object object) {
	HtmlComponent component = super.generateMainComponent(object);
	if (!stack.isEmpty() && getParentParameterName() != null) {
	    HtmlLink link = (HtmlLink) component.getChild(new Predicate() {
		public boolean evaluate(Object arg0) {
		    return arg0 instanceof HtmlLink;
		}

	    });
	    if (link != null) {
		link.addParameter(getParentParameterName(), (stack.peek()).getIdInternal());
	    }
	}
	return component;
    }

    @Override
    protected void beforeRecursion(Object object) {
	stack.push((DomainObject) object);
    }

    @Override
    protected void afterRecursion(Object object) {
	stack.pop();
    }
}
