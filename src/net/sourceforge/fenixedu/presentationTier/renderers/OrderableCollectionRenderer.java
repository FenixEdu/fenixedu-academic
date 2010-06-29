package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;

import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.utils.RendererPropertyUtils;

public class OrderableCollectionRenderer extends pt.ist.fenixWebFramework.renderers.OrderableCollectionRenderer {

    public OrderableCollectionRenderer() {
	super();
    }

    public class OrderableTableLink extends pt.ist.fenixWebFramework.renderers.OrderableCollectionRenderer.TableLink {

	public OrderableTableLink(String name) {
	    super(name);
	}

	@Override
	public HtmlComponent generateLink(Object object) {
	    if (getVisibleIf() != null) {
		try {
		    Boolean visible = (Boolean) RendererPropertyUtils.getProperty(object, this.getVisibleIf(), false);

		    if (visible != null && !visible) {
			return null;
		    }
		} catch (ClassCastException e) {
		    e.printStackTrace();
		}
	    }

	    if (getVisibleIfNot() != null) {
		try {
		    Boolean notVisible = (Boolean) RendererPropertyUtils.getProperty(object, getVisibleIfNot(), false);

		    if (notVisible != null && notVisible) {
			return null;
		    }
		} catch (ClassCastException e) {
		    e.printStackTrace();
		}
	    }

	    if (getCustom() != null) {
		return new HtmlText(RenderUtils.getFormattedProperties(getCustom(), object), false);
	    } else {
		HtmlLink link = new HtmlLink();

		if (isContextRelativeSet()) {
		    link.setContextRelative(isContextRelative());
		}

		if (getIcon() != null && !getIcon().equals("none")) {
		    HtmlLink forImage = new HtmlLink();
		    forImage.setModuleRelative(false);
		    forImage.setContextRelative(true);
		    forImage.setUrl("/images/" + getIcon() + ".gif");

		    HtmlImage image = new HtmlImage();
		    image.setSource(forImage.calculateUrl());
		    image.setDescription(getLinkText(this, object));
		    link.setBody(image);
		} else {
		    link.setText(getLinkText(this, object));
		}

		link.setModule(getModule());

		if (getLinkFormat() != null) {
		    link.setUrl(RenderUtils.getFormattedProperties(getLinkFormat(), object));
		} else {
		    link.setUrl(getLink());
		    setLinkParameters(object, link, this);
		}

		if (getConfirmationKey() != null) {
		    if (link.getId() == null) {
			link.setId(getName() + "-" + object.hashCode());
		    }
		    final String confirmationMessage = getConfirmationBundle() != null ? RenderUtils.getResourceString(
			    getConfirmationBundle(), getConfirmationKey()) : RenderUtils.getResourceString(getConfirmationKey());

		    link.setOnClick("return confirm('" + confirmationMessage + "');");
		}

		return link;
	    }
	}

    }

    protected TableLink getTableLink(String name) {
	TableLink tableLink = this.links.get(name);

	if (tableLink == null) {
	    tableLink = new OrderableTableLink(name);

	    this.links.put(name, tableLink);
	    this.sortedLinks.add(tableLink);
	}

	return tableLink;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	Collection sortedCollection = (isSortIgnored()) ? (Collection) object : RenderUtils.sortCollectionWithCriteria(
		(Collection) object, getSortBy());

	return new OrderedCollectionTabularLayout(sortedCollection);
    }

    protected class OrderedCollectionTabularLayout extends
	    pt.ist.fenixWebFramework.renderers.OrderableCollectionRenderer.OrderedCollectionTabularLayout {

	public OrderedCollectionTabularLayout(Collection object) {
	    super(object);
	}

	@Override
	protected void addScripts(HtmlContainer container) {
	}

    }
}
