package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.LinkObject;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;

public class ListLinkRenderer extends OutputRenderer {

    private static String DEFAULT_SEPARATOR = " > ";

    private String linkFormat;
    private String separator;

    private boolean contextRelative;
    private boolean moduleRelative;

    public String getSeparator() {
	return hasSeparator() ? this.separator : DEFAULT_SEPARATOR;
    }

    /**
     * Indicates the separator between generated links. If not specified then
     * " > " default sting is used
     * 
     * @property
     */
    public void setSeparator(String separator) {
	this.separator = separator;
    }

    private boolean hasSeparator() {
	return !StringUtils.isEmpty(this.separator);
    }

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public String getLinkFormat() {
	return linkFormat;
    }

    public void setLinkFormat(String linkFormat) {
	this.linkFormat = linkFormat;
    }

    public boolean isModuleRelative() {
	return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
	this.moduleRelative = moduleRelative;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {

		final HtmlBlockContainer blockContainer = new HtmlBlockContainer();

		final List<LinkObject> objects = (List<LinkObject>) object;
		final Iterator<LinkObject> iterator = objects.iterator();

		while (iterator.hasNext()) {
		    final LinkObject each = iterator.next();

		    final ObjectLinkRenderer linkRenderer = new ObjectLinkRenderer();

		    linkRenderer.setText(each.getLabel());
		    linkRenderer.setLinkFormat(getLinkFormat());
		    linkRenderer.setModuleRelative(isModuleRelative());
		    linkRenderer.setContextRelative(isContextRelative());

		    blockContainer.addChild(RenderKit.getInstance().renderUsing(linkRenderer, getContext(), each,
			    LinkObject.class));
		    blockContainer.addChild(iterator.hasNext() ? new HtmlText(getSeparator()) : new HtmlText());
		}
		return blockContainer;
	    }
	};
    }
}
