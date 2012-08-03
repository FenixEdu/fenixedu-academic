package pt.ist.fenixframework.plugins.remote.servlet.request;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.servlet.http.HttpServletResponse;

import pt.ist.fenixframework.plugins.remote.UriType;
import pt.ist.fenixframework.plugins.remote.servlet.RemoteServlet;
import pt.ist.fenixframework.pstm.DomainClassInfo;

public class RemoteClassDescriptionRequest extends RemoteRequest {

    private final Class clazz;

    public RemoteClassDescriptionRequest(final Class clazz) {
	this.clazz = clazz;
    }

    @Override
    protected int getStatus() {
	return HttpServletResponse.SC_OK;
    }

    @Override
    protected void writeResponseBody(Writer writer) throws IOException {
	writer.write("<" + getTag() + ">");

	writeIdentificationInfo(writer);

	writer.write("<methods>");
	for (final Method method : clazz.getMethods()) {
	    if (isMethodAvailable(method)) {
		writeMethod(writer, method);
	    }
	}
	writer.write("</methods>");

	writer.write("</" + getTag() + ">");
    }

    protected void writeIdentificationInfo(final Writer writer) throws IOException {
	final int mapClassToId = DomainClassInfo.mapClassToId(clazz);
	writeTag(writer, "domainClassId", Integer.toString(mapClassToId));
	writeTag(writer, "domainClassName", clazz.getName());
    }

    protected String getTag() {
	return "domainClass";
    }

    protected UriType getUriType() {
	return UriType.CLASS;
    }

    protected String getResource() {
	return clazz.getName();
    }

    protected boolean isMethodAvailable(final Method method) {
	final int modifiers = method.getModifiers();
	final Class<?>[] parameterTypes = method.getParameterTypes();
	return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)
		&& (parameterTypes == null || parameterTypes.length == 0);
    }

    protected void writeMethod(final Writer writer, final Method method) throws IOException {
	writer.write("<method>");

	final String name = method.getName();
	writeTag(writer, "name", name);
	writeTag(writer, "url", RemoteServlet.getMappingPatternPrefix() + getUriType().type() + "/" + getResource() + "/" + name);

	writer.write("</method>");
    }

}
