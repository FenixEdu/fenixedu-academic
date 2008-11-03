package net.sourceforge.fenixedu.domain.log.profiling;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.log.profiling.ModuleBean;
import net.sourceforge.fenixedu.dataTransferObject.log.profiling.RequestBean;
import net.sourceforge.fenixedu.dataTransferObject.log.profiling.ServerBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.joda.time.LocalDate;

public class ProfileReport extends ProfileReport_Base {

    private Document cachedDocument;

    public ProfileReport(String reportData, LocalDate date) {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
	setDate(date);
	setReport(reportData);
    }

    private Document getDocument() {
	if (cachedDocument == null) {
	    SAXBuilder parser = new SAXBuilder();
	    try {
		cachedDocument = parser.build(new StringReader(getReport()));
	    } catch (Exception e) {
		e.printStackTrace();
		cachedDocument = null;
	    }
	}
	return cachedDocument;
    }

    public List<ServerBean> getSimpleServers() {
	Document document = getDocument();
	Element rootElement = document.getRootElement();
	List<Element> servers = rootElement.getChildren("server");
	List<ServerBean> beans = new ArrayList<ServerBean>();

	for (Element serverElement : servers) {
	    ServerBean bean = new ServerBean();
	    bean.setServerName(serverElement.getAttributeValue("name"));
	    bean.setMinTimeSpent(Integer.valueOf(serverElement.getAttributeValue("min")));
	    bean.setMaxTimeSpent(Integer.valueOf(serverElement.getAttributeValue("max")));
	    bean.setInvocationCount(Integer.valueOf(serverElement.getAttributeValue("invocationCount")));
	    bean.setAverageTimeSpent(Integer.valueOf(serverElement.getAttributeValue("average")));
	    bean.setTotalTimeSpent(Integer.valueOf(serverElement.getAttributeValue("total")));
	    beans.add(bean);
	}

	return beans;
    }

    public List<ServerBean> getServers() {
	Document document = getDocument();
	Element rootElement = document.getRootElement();
	List<Element> servers = rootElement.getChildren("server");
	List<ServerBean> beans = new ArrayList<ServerBean>();

	for (Element serverElement : servers) {
	    ServerBean bean = new ServerBean();
	    bean.setDate(getDate());
	    bean.setServerName(serverElement.getAttributeValue("name"));
	    bean.setMinTimeSpent(Integer.valueOf(serverElement.getAttributeValue("min")));
	    bean.setMaxTimeSpent(Integer.valueOf(serverElement.getAttributeValue("max")));
	    bean.setInvocationCount(Integer.valueOf(serverElement.getAttributeValue("invocationCount")));
	    bean.setAverageTimeSpent(Integer.valueOf(serverElement.getAttributeValue("average")));
	    bean.setTotalTimeSpent(Integer.valueOf(serverElement.getAttributeValue("total")));
	    addModules(bean, serverElement.getChild("modules"));
	    beans.add(bean);
	}

	return beans;

    }

    private void addModules(ServerBean serverBean, Element moduleRoot) {
	List<Element> modules = moduleRoot.getChildren("module");
	for (Element moduleElement : modules) {
	    ModuleBean bean = new ModuleBean();
	    bean.setDate(getDate());
	    bean.setModuleName(moduleElement.getAttributeValue("name"));
	    bean.setMinTimeSpent(Integer.valueOf(moduleElement.getAttributeValue("min")));
	    bean.setMaxTimeSpent(Integer.valueOf(moduleElement.getAttributeValue("max")));
	    bean.setInvocationCount(Integer.valueOf(moduleElement.getAttributeValue("invocationCount")));
	    bean.setTotalTimeSpent(Integer.valueOf(moduleElement.getAttributeValue("total")));
	    serverBean.addModuleBean(bean);
	    addRequests(bean, moduleElement.getChild("requests"));
	}
    }

    private void addRequests(ModuleBean moduleBean, Element requestsRoot) {
	List<Element> requests = requestsRoot.getChildren("request");
	for (Element requestElement : requests) {
	    RequestBean bean = new RequestBean();
	    bean.setDate(getDate());
	    bean.setName(requestElement.getAttributeValue("name"));
	    bean.setMinTimeSpent(Integer.valueOf(requestElement.getAttributeValue("min")));
	    bean.setMaxTimeSpent(Integer.valueOf(requestElement.getAttributeValue("max")));
	    bean.setInvocationCount(Integer.valueOf(requestElement.getAttributeValue("invocationCount")));
	    bean.setTotalTimeSpent(Integer.valueOf(requestElement.getAttributeValue("total")));
	    bean.setAverageSpent(Integer.valueOf(requestElement.getAttributeValue("average")));
	    Attribute alert = requestElement.getAttribute("alert");
	    bean.setAlert(alert == null ? false : true);
	    moduleBean.addRequestBean(bean);
	}
    }

    public ServerBean getReportForServer(String serverName) {
	List<ServerBean> beans = getServers();
	for (ServerBean bean : beans) {
	    if (bean.getServerName().equals(serverName)) {
		return bean;
	    }
	}
	return null;
    }

    public ModuleBean getReportForServerAndModule(String serverName, String moduleName) {
	ServerBean serverBean = getReportForServer(serverName);
	if (serverBean != null) {
	    for (ModuleBean bean : serverBean.getModules()) {
		if (bean.getModuleName().equals(moduleName)) {
		    return bean;
		}
	    }
	}
	return null;
    }
}
