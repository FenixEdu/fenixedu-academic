<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniPasswordRequired.jsp -->

<h1>Inscrição Alumni</h1>

<div class="alumnilogo">
	
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>
	
	<logic:equal name="registrationResult" value="true">
		<h2>
			<bean:message key="label.registration.succeeded" bundle="ALUMNI_RESOURCES" />
		</h2>
		
		<p class="greytxt">
			<bean:message key="label.registration.passwordRequired" bundle="ALUMNI_RESOURCES" />
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
	
	<br/><br/>
	
	<!-- END CONTENTS -->
</div>