package pt.ist.fenixframework.plugins.remote.servlet.request;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public abstract class ValidRequest extends RemoteRequest {

    protected final Object target;
    protected final Class<?> clazz;
    protected final Method method;
    protected Object result;
    protected Object[] paramArgs;

    public ValidRequest(final Object target, final Class<?> clazz, final String[] pathParts) throws SecurityException,
	    NoSuchMethodException {
	this.target = target;
	this.clazz = clazz;
	if (pathParts.length > 2) {
	    // TODO : the next line needs to be generalized.
	    if (pathParts.length > 3) { // args available
		final String[] split = pathParts[3].split(",");
		final String[] args = new String[split.length];
		final Class[] paramClasses = new Class[split.length];
		int i = 0;
		for (String arg : split) {
		    args[i] = URLDecoder.decode(arg).replace("\"", "");
		    paramClasses[i++] = String.class;
		}
		setParamArgs(args);
		method = clazz.getMethod(pathParts[2], paramClasses);
	    } else {
		Class<?>[] paramClasses = null;
		setParamArgs(new String[] {});
		method = clazz.getMethod(pathParts[2], paramClasses);
	    }

	} else {
	    method = null;
	}
    }

    private void setParamArgs(String[] args) {
	this.paramArgs = args;
    }

    public ValidRequest(final Object object, final String[] pathParts) throws SecurityException, NoSuchMethodException {
	this(object, object.getClass(), pathParts);
    }

    public ValidRequest(final Class<?> clazz, final String[] pathParts) throws SecurityException, NoSuchMethodException {
	this(clazz, clazz, pathParts);
    }

    @Override
    protected void writeResponseBody(final Writer writer) throws IOException {
	if (result != null) {
	    final String responseString = serializeResult();
	    writeTag(writer, "methodResultValue", responseString);
	}
    }

    protected String serializeResult() {
	if (result instanceof AbstractDomainObject) {
	    final AbstractDomainObject domainObject = (AbstractDomainObject) result;
	    return domainObject.getExternalId();
	} else if (result instanceof Collection) {
	    final StringBuilder stringBuilder = new StringBuilder();
	    for (final Object object : (Collection) result) {
		final AbstractDomainObject domainObject = (AbstractDomainObject) object;
		if (stringBuilder.length() > 0) {
		    stringBuilder.append(',');
		}
		stringBuilder.append(domainObject.getExternalId());
	    }
	    return stringBuilder.toString();
	}
	return result.toString();
    }

    @Override
    protected int getStatus() {
	return HttpServletResponse.SC_OK;
    }

    @Override
    public void sendResponse(final HttpServletResponse response) throws IOException {
	RemoteRequest responder;
	if (target != null && clazz != null) {
	    try {
		if (method == null) {
		    if (clazz == target) { // static method invoke
			responder = new RemoteClassDescriptionRequest(clazz);
		    } else {
			responder = new RemoteObjectDescriptionRequest(target);
		    }
		} else {
		    result = method.invoke(target, getParamArgs());
		    responder = this;
		}
	    } catch (IllegalArgumentException e) {
		responder = new BadRequest(e.toString());
	    } catch (IllegalAccessException e) {
		responder = new BadRequest(e.toString());
	    } catch (InvocationTargetException e) {
		responder = new BadRequest(e.toString());
	    }
	} else {
	    responder = new BadRequest();
	}

	if (responder == this) {
	    super.sendResponse(response);
	} else {
	    responder.sendResponse(response);
	}
    }

    private Object[] getParamArgs() {
	return paramArgs;
    }

}
