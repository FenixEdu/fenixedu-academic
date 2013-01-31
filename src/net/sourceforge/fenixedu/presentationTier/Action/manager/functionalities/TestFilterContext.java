package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.IFunctionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class TestFilterContext implements FunctionalityContext {

	private HttpServletRequest request;
	private IUserView userView;
	private Functionality functionality;

	public TestFilterContext(HttpServletRequest request, TestFilterBean bean, Functionality functionality) {
		this.request = new TestFilterRequestWrapper(request, bean.getParametersMap());
		this.userView = AccessControl.getUserView();
		this.functionality = functionality;
	}

	@Override
	public HttpServletRequest getRequest() {
		return this.request;
	}

	@Override
	public IUserView getUserView() {
		return this.userView;
	}

	@Override
	public User getLoggedUser() {
		return this.userView == null ? null : AccessControl.getPerson().getUser();
	}

	public Module getSelectedModule() {
		return ((IFunctionality) this.functionality).getModule();
	}

	public Functionality getSelectedFunctionality() {
		return this.functionality;
	}

	private static class TestFilterRequestWrapper extends HttpServletRequestWrapper {

		private Map<String, String[]> parameters;

		public TestFilterRequestWrapper(HttpServletRequest request, Map<String, String[]> parameters) {
			super(request);

			this.parameters = parameters;
		}

		@Override
		public String getQueryString() {
			if (this.parameters.isEmpty()) {
				return null;
			}

			StringBuilder builder = new StringBuilder();

			for (String key : this.parameters.keySet()) {
				String[] values = this.parameters.get(key);

				for (String value : values) {
					builder.append("&" + key + "=" + value);
				}
			}

			return builder.substring(1).toString();
		}

		@Override
		public String getParameter(String name) {
			String[] values = this.parameters.get(name);

			if (values == null) {
				return null;
			} else {
				return values[0];
			}
		}

		@Override
		public Map getParameterMap() {
			return this.parameters;
		}

		@Override
		public Enumeration getParameterNames() {
			return new Vector<String>(this.parameters.keySet()).elements();
		}

		@Override
		public String[] getParameterValues(String name) {
			return this.parameters.get(name);
		}

	}

	@Override
	public Container getSelectedContainer() {
		Portal root = Portal.getRootPortal();

		Section section = functionality.getUniqueParentContainer(Section.class);
		List<Content> path = root.getPathTo(section);

		return path.size() <= 2 ? (Container) root : (Container) path.get(2);
	}

	@Override
	public Container getSelectedTopLevelContainer() {
		Portal root = Portal.getRootPortal();

		Section section = functionality.getUniqueParentContainer(Section.class);
		return (Container) root.getPathTo(section).get(2);
	}

	@Override
	public Content getLastContentInPath(Class type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Content> getSelectedContents() {
		return Collections.emptyList();
	}

	@Override
	public Content getSelectedContent() {
		// TODO Auto-generated method stub
		return null;
	}
}
