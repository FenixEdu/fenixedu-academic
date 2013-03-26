package net.sourceforge.fenixedu.presentationTier.servlets.gwt;

import java.lang.reflect.Method;

import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Describes an incoming RPC request in terms of a resolved {@link Method} and
 * an array of arguments.
 */
public final class FenixRPCRequest {

    /**
     * The name for the service invoked, so that FenixGWTServlet can dispatch it properly.
     */
    private final String remoteServiceInterfaceName;

    /**
     * The flags associated with the RPC request.
     */
    private final int flags;

    /**
     * The method for this request.
     */
    private final Method method;

    /**
     * The parameters for this request.
     */
    private final Object[] parameters;

    /**
     * {@link SerializationPolicy} used for decoding this request and for encoding
     * the responses.
     */
    private final SerializationPolicy serializationPolicy;

    /**
     * Construct an RPCRequest.
     */
    public FenixRPCRequest(String remoteServiceInterfaceName, Method method, Object[] parameters,
            SerializationPolicy serializationPolicy, int flags) {
        this.remoteServiceInterfaceName = remoteServiceInterfaceName;
        this.method = method;
        this.parameters = parameters;
        this.serializationPolicy = serializationPolicy;
        this.flags = flags;
    }

    public String getRemoteServiceInterfaceName() {
        return remoteServiceInterfaceName;
    }

    public int getFlags() {
        return flags;
    }

    /**
     * Get the request's method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Get the request's parameters.
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * Returns the {@link SerializationPolicy} used to decode this request. This
     * is also the <code>SerializationPolicy</code> that should be used to encode
     * responses.
     * 
     * @return {@link SerializationPolicy} used to decode this request
     */
    public SerializationPolicy getSerializationPolicy() {
        return serializationPolicy;
    }

    /**
     * For debugging use only.
     */
    @Override
    public String toString() {
        StringBuilder callSignature = new StringBuilder();

        // Add the class and method names
        callSignature.append(method.getDeclaringClass().getName());
        callSignature.append('.');
        callSignature.append(method.getName());

        // Add the list of parameters
        callSignature.append('(');
        for (Object param : parameters) {
            if (param instanceof String) {
                // Put it within quotes and escape quotes, for readability
                callSignature.append('"');
                String strParam = (String) param;
                String escapedStrParam = strParam.replaceAll("\\\"", "\\\\\"");
                callSignature.append(escapedStrParam);
                callSignature.append('"');
            } else if (param == null) {
                callSignature.append("null");
            } else {
                // We assume that anyone who wants to use this method will implement
                // toString on his serializable objects.
                callSignature.append(param.toString());
            }
            callSignature.append(", ");
        }

        // Remove the last ", "
        int length = callSignature.length();
        callSignature.delete(length - 2, length);
        callSignature.append(')');

        return callSignature.toString();
    }
}
