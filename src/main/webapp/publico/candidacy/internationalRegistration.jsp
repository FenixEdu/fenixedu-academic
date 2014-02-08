<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="java.util.Locale"%>

<html:xhtml/>

<h2><bean:message key ="title.register.user" bundle="APPLICATION_RESOURCES"/></h2>


<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:present name="person">
	
	<div class="infoop2">
		<p><bean:message key="message.register.user" arg0="<%=Instalation.getInstance().getInstalationName()%>" bundle="APPLICATION_RESOURCES"/></p>
		<strong><bean:message key="message.requirements"/>:</strong>
		<ul>
			<li><bean:message key="message.pass.size"/></li>
			<li><bean:message key="message.pass.classes"/></li>
			<li><bean:message key="message.pass.weak"/></li>			
		</ul>
	</div>
	
	<html:form action="/internationalRegistration.do?method=updateUserPassword">
		<table class="tstyle5 thlight thright">
			<html:hidden property="hashCode" value="<%= request.getParameter("hash") %>"/>
			<!-- password -->
			<tr>
				<th><bean:message bundle="HTMLALT_RESOURCES" key="password.password"/>:</th>
				<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.password" property="password"/></td>
			</tr>
			<!-- retype password -->
			<tr>
				<th><bean:message bundle="HTMLALT_RESOURCES" key="password.retypePassword"/>:</th>
				<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.retypePassword" property="retypedPassword"/></td>
			</tr>
		</table>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="ok" />
		</p>
	</html:form>
	
	
</logic:present>



