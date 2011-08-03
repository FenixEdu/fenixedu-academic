package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class AutoCompleteInputRenderer extends pt.ist.fenixWebFramework.rendererExtensions.AutoCompleteInputRenderer {

    public static final String SERVLET_URI = "/ajax/AutoCompleteServlet";
    public static final String TYPING_VALUE = "custom";

    private String className;
    private String serviceName;
    private String serviceArgs;

    public AutoCompleteInputRenderer() {
	super();
	setMinChars(3);
    }

    public String getClassName() {
	if (this.className != null) {
	    return this.className;
	} else {
	    return getContext().getMetaObject().getType().getName();
	}
    }

    /**
     * The name of the type of objects we want to search. This should be the the
     * same type or a subtype of the type of the slot this rendering is editing.
     * 
     * @property
     */
    public void setClassName(String className) {
	this.className = className;
    }

    public String getServiceName() {
	return this.serviceName;
    }

    /**
     * Configures the service that should be used to do the search. That service
     * must implement the interface
     * {@link net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService}
     * .
     * 
     * @property
     */
    public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
    }

    public String getServiceArgs() {
	return this.serviceArgs;
    }

    /**
     * Allows you to pass extra arguments to the service in the form
     * <code>paramA=value1,paramB=value2</code>. This arguments will be
     * available in the arguments map passed to the service.
     * 
     * @property
     */
    public void setServiceArgs(String serviceArgs) {
	this.serviceArgs = serviceArgs;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new AutoCompleteLayout() {

	    @Override
	    protected String getFormatedArgs() {
		return String.format("serviceName=%s,class=%s,%s", getServiceName(), getClassName(), getFormatedServiceArgs());
	    }

	    private String getFormatedServiceArgs() {
		Object object = ((MetaSlot) getInputContext().getMetaObject()).getMetaObject().getObject();
		return RenderUtils.getFormattedProperties(getServiceArgs(), object);
	    }

	};
    }


}
