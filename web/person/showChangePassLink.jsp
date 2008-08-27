<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key ="title.person.changepass" /></h2>


<div class="infoop2 mvert15">
<bean:message key="message.change.password" bundle="APPLICATION_RESOURCES"/>
</div>

<p><html:link href='<%= "https://id.ist.utl.pt/password/index.php?url=https://fenix.ist.utl.pt/privado&istid=" + AccessControl.getUserView().getPerson().getIstUsername() %>' ><bean:message key="link.change.password" bundle="APPLICATION_RESOURCES"/></html:link></p>	

