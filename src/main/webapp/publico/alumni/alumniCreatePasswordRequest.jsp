<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniCreatePasswordRequest.jsp -->


<h1>Inscrição Alumni</h1>

<div class="alumnilogo">

<h2>
	<logic:present name="pendingRequests">
		<bean:message key="label.choose.password" bundle="ALUMNI_RESOURCES" />
	</logic:present>
	<logic:notPresent name="pendingRequests">
		<bean:message key="label.choose.password.identity.check" bundle="ALUMNI_RESOURCES" />
	</logic:notPresent>
</h2>

<p class="greytxt"><bean:message key="label.registration.password" bundle="ALUMNI_RESOURCES" /></p>


<logic:notPresent name="pendingRequests">
	<p class="greytxt"><bean:message key="label.chooose.password.text.no.login" bundle="ALUMNI_RESOURCES" /></p>
</logic:notPresent>


<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<div class="reg_form">	
	<fr:form action="/alumni.do?method=createPasswordRequest">
	
		<fr:edit id="passwordAccessBean" name="passwordAccessBean" visible="false" />
	
		<logic:notPresent name="pendingRequests">
			<fieldset style="display: block;">
				<legend><bean:message key="label.alumni.form.personal.data" bundle="ALUMNI_RESOURCES" /></legend>
				<p>
					<bean:message key="label.all.required.fields" bundle="ALUMNI_RESOURCES" />
				</p>
			
				<fr:edit id="alumniFormationDegree" name="passwordAccessBean" schema="alumni.public.access.passwordCreation.personal.info" >
					<fr:destination name="invalid" path="/alumni.do?method=createPasswordRequestInvalid"/>
					<fr:layout name="tabular-break">
						<fr:property name="classes" value="thleft"/>
						<fr:property name="columnClasses" value=",tderror1"/>
					</fr:layout>
				</fr:edit>
		
				<p class="comment" />
			
			</fieldset>
		</logic:notPresent>
		<p>
			<html:submit>
				<bean:message key="label.conclude" bundle="ALUMNI_RESOURCES" />
			</html:submit>
		</p>
	
	</fr:form>
</div>


