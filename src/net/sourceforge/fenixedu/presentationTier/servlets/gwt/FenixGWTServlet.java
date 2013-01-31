package net.sourceforge.fenixedu.presentationTier.servlets.gwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FenixGWTServlet extends RemoteServiceServlet {

	public ServletConfig servletConfig = null;

	private Map<String, RemoteServiceServlet> dispatchMap;

	public FenixGWTServlet() {
		super();
		dispatchMap = new HashMap<String, RemoteServiceServlet>();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.servletConfig = config;
	}

	@Override
	public void destroy() {
		for (Entry<String, RemoteServiceServlet> entry : dispatchMap.entrySet()) {
			entry.getValue().destroy();
		}
	}

	@Override
	public String processCall(String payload) throws SerializationException {
		try {
			FenixRPCRequest rpcRequest = FenixRPC.decodeRequest(payload, this.getClass(), this);

			String remoteServiceName = getServerSideEquivalent(rpcRequest.getRemoteServiceInterfaceName());

			RemoteServiceServlet remoteService = appointServlet(remoteServiceName);

			/**
			 * Delegates method invocation to proper servlet. We do not call <code>remoteService.processCall(payload);</code>
			 * because while invoking the specific servlet we lose reference to <code>getThreadLocalRequest()</code>, essential
			 * to determine the <code>SerializationPolicy</code>. Eventual overrides of
			 * <code>onAfterRequestDeserialized(rpcRequest);</code> should be implemented in this class.
			 */
			String result =
					RPC.invokeAndEncodeResponse(remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(),
							rpcRequest.getSerializationPolicy(), rpcRequest.getFlags());

			return result;

		} catch (IncompatibleRemoteServiceException e) {
			return RPC.encodeResponseForFailure(null, e);
		}
	}

	private RemoteServiceServlet appointServlet(String className) {
		if (dispatchMap.get(className) == null) {
			Class<RemoteServiceServlet> servletClass;

			try {
				servletClass = (Class<RemoteServiceServlet>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new IncompatibleRemoteServiceException(e.getMessage(), e);
			}

			RemoteServiceServlet servlet;

			try {
				servlet = servletClass.newInstance();
			} catch (InstantiationException e) {
				throw new IncompatibleRemoteServiceException(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				throw new IncompatibleRemoteServiceException(e.getMessage(), e);
			}

			try {
				servlet.init(this.servletConfig);
			} catch (ServletException e) {
				throw new IncompatibleRemoteServiceException(e.getMessage(), e);
			}

			dispatchMap.put(className, servlet);
		}
		return dispatchMap.get(className);
	}

	private String getServerSideEquivalent(String remoteServiceInterfaceName) {
		String serverSideEquivalent = remoteServiceInterfaceName.replace(".client.", ".server.");
		serverSideEquivalent += "Impl";
		return serverSideEquivalent;
	}

	@Override
	//Overridden for debug purpose only
	protected void doUnexpectedFailure(Throwable e) {
		System.out.println("\n\n--------------Unwrapped Exception Starts Here------------");
		e.printStackTrace();
		System.out.println("--------------Unwrapped Exception Ends Here------------\n\n");
		super.doUnexpectedFailure(e);
	}

}
