<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml/>


<h2><bean:message key="message.welcome.to.manager.portal"/></h2>
<br/>

<html:link href="<%= request.getContextPath() + "/domainbrowser/"%>"><bean:message bundle="MANAGER_RESOURCES" key="link.domain.browser"/></html:link>