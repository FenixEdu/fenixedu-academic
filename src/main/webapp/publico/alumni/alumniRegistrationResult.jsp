<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniRegistrationResult.jsp -->

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
			<bean:message key="label.registration.login" bundle="ALUMNI_RESOURCES" />
		</p>
		<ul>
			<li class="greytxt">
				<a href="<bean:message key="label.fenix.login.hostname" bundle="ALUMNI_RESOURCES" />" >
					<bean:message key="label.fenix.login.url" bundle="ALUMNI_RESOURCES" />
				</a>
			</li>
		</ul>
		<p style="margin-top: 2em;">
			<bean:message key="label.fenix.login" bundle="ALUMNI_RESOURCES" /> <strong><bean:write name="loginAlias" /></strong> 
		</p>
		<p>
			Caso nunca tenha acedido ao sistema deverá <a href="https://id.ist.utl.pt/password/recover.php" target="_blank">obter uma password</a>.
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

		
	<div class="h_box_alt" style="margin-top: 3em;">
		<ul class="material">
			<li style="padding-left: 35px;">
				Participe activamente na melhoria contínua do IST. O seu contributo é fundamental.
			</li>
		</ul>
	</div>

	
	<!-- END CONTENTS -->
</div>
