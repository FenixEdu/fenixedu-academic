package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ImageObjectRenderer extends OutputRenderer {

    private String imageFormat;

    private boolean useParent;

    private boolean moduleRelative;

    private boolean contextRelative;

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public String getImageFormat() {
	return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
	this.imageFormat = imageFormat;
    }

    public boolean isModuleRelative() {
	return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
	this.moduleRelative = moduleRelative;
    }

    public boolean isUseParent() {
	return useParent;
    }

    public void setUseParent(boolean useParent) {
	this.useParent = useParent;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new ImageObjectLayout();
    }

    public class ImageObjectLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlImage image = new HtmlImage();
	    String link = RenderUtils.getFormattedProperties(getImageFormat(), getTargetObject(object));
	    if(isModuleRelative()) {
		link = RenderUtils.getModuleRelativePath("") + link; 
	    }
	    else if(isContextRelative()) {
		 link = RenderUtils.getContextRelativePath("") + link;
	    }
	    image.setSource(link);
	    return image;
	}

	protected Object getTargetObject(Object object) {
	    if (isUseParent()) {
		if (getContext().getParentContext() != null) {
		    return getContext().getParentContext().getMetaObject().getObject();
		} else {
		    return null;
		}
	    } else {
		return object;
	    }
	}
    }
}
