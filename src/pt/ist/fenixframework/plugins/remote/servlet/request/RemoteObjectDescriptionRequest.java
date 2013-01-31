package pt.ist.fenixframework.plugins.remote.servlet.request;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.plugins.remote.UriType;

public class RemoteObjectDescriptionRequest extends RemoteClassDescriptionRequest {

	private final Object target;

	public RemoteObjectDescriptionRequest(final Object target) {
		super(target.getClass());
		this.target = target;
	}

	@Override
	protected String getTag() {
		return "domainObject";
	}

	@Override
	protected UriType getUriType() {
		return UriType.OBJECT;
	}

	@Override
	protected String getResource() {
		return ((DomainObject) target).getExternalId();
	}

	@Override
	protected boolean isMethodAvailable(final Method method) {
		final int modifiers = method.getModifiers();
		final Class<?>[] parameterTypes = method.getParameterTypes();
		return Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)
				&& (parameterTypes == null || parameterTypes.length == 0);
	}

	@Override
	protected void writeIdentificationInfo(final Writer writer) throws IOException {
		final Class clazz = target.getClass();
		writeTag(writer, "domainClassName", clazz.getName());
		writeTag(writer, "domainObjectOid", getResource());
	}

}
