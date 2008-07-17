<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniRegistrationResult.jsp -->

<h1>Alumni</h1>

<div class="alumnilogo">
	
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>
	
	<logic:equal name="registrationResult" value="true">
		<h2>
			<bean:message key="label.registration.succeeded" bundle="ALUMNI_RESOURCES" />
		</h2>
		
		<p class="greytxt">
			<bean:message key="label.registration.login" bundle="ALUMNI_RESOURCES" />
		</p>
		<p class="greytxt">
			<a href="https://fenix.ist.utl.pt/privado">
				<bean:message key="label.fenix.login.url" bundle="ALUMNI_RESOURCES" />
			</a>
		</p>
		<p>
			<bean:message key="label.fenix.login" bundle="ALUMNI_RESOURCES" />&nbsp;<bean:write name="loginAlias" /> 
		</p>
	</logic:equal>
	
	
	<logic:equal name="registrationResult" value="false">
		<h2 class="mtop15">
			<bean:message key="label.registration.failure" bundle="ALUMNI_RESOURCES" />
		</h2>
		<p class="greytxt">
			<bean:message key="label.registration.failure.description" bundle="ALUMNI_RESOURCES" />
		</p>
	</logic:equal>
	
	<br/>
	
	<!-- END CONTENTS -->
</div>