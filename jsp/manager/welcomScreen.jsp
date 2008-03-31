<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>


<h2><bean:message key="message.welcome.to.manager.portal"/></h2>
<br/>

<html:link href="<%= request.getContextPath() + "/domainbrowser/"%>"><bean:message bundle="MANAGER_RESOURCES" key="link.domain.browser"/></html:link>