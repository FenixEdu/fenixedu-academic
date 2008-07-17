<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniPublicAccessRegistrationEmail.jsp -->

<h1>Alumni</h1>

<div class="alumnilogo">


<h2><bean:message key="label.confirm.email" bundle="ALUMNI_RESOURCES" /></h2>

<logic:notPresent name="alumniEmailErrorMessage">
	<p class="greytxt"><bean:message key="label.check.email" bundle="ALUMNI_RESOURCES" /></p>
</logic:notPresent>

<logic:present name="alumniEmailErrorMessage">
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>
	<bean:write name="alumniEmailErrorMessage"  scope="request" />
</logic:present>

<logic:present name="alumniEmailSuccessMessage">
	<p>O endereço abaixo será enviado por email. Nesta fase de testes, estará aqui visível:</p>
	<p><a href='<bean:write name="alumniEmailSuccessMessage"  scope="request" />'><bean:write name="alumniEmailSuccessMessage"  scope="request" /></a></p>
</logic:present>

<br/><br/>