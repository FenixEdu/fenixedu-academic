<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.manager.generate.pass" /></h2>


<div class="infoop2 mvert15">
<bean:message key="message.generate.password" bundle="APPLICATION_RESOURCES"/>
</div>

<p><html:link href='https://ciist.ist.utl.pt/ciistadmin/admin' target="_blank"><bean:message key="link.generate.password" bundle="APPLICATION_RESOURCES"/></html:link></p>	

