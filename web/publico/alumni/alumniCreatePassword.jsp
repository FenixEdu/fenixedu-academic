<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniCreatePassword.jsp -->


<h1>Alumni</h1>

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


<fr:form id="reg_form" action="/alumni.do?method=createPassword">

	<fr:edit id="passwordAccessBean" name="passwordAccessBean" visible="false" />

	<logic:notPresent name="pendingRequests">
		<fieldset style="display: block;">
			<legend><bean:message key="label.alumni.form.personal.data" bundle="ALUMNI_RESOURCES" /></legend>
			<p>
				<bean:message key="label.all.required.fields" bundle="ALUMNI_RESOURCES" />
			</p>
		
			<fr:edit id="alumniFormationDegree" name="passwordAccessBean" schema="alumni.public.access.passwordCreation.personal.info" >
				<fr:destination name="invalid" path="/alumni.do?method=createPasswordInvalid"/>
				<fr:layout name="tabular-break">
					<fr:property name="classes" value="thleft"/>
					<fr:property name="columnClasses" value=",tderror1"/>
				</fr:layout>
			</fr:edit>
	
			<p class="comment" />
		
		</fieldset>
	</logic:notPresent>


	<logic:notPresent name="pendingRequests">
		<h2 class="mtop15">
			Escolher Password		
		</h2>
	</logic:notPresent>



	<p class="greytxt"><bean:message key="label.chooose.password.text.previous.login" bundle="ALUMNI_RESOURCES" /></p>

	<div class="mvert15">
		<strong>Requisitos:</strong>
		<ul>
			<li>a password deve ter no mínimo 8 caracteres.</li>
			<li>a password deve usar pelo menos três variantes de caracteres. As variantes podem ser: maíusculas, minúsculas, algarismos, caracteres especiais.</li>
			<li>não reutilize uma password antiga para a nova password.</li>
			<li>a password não deve ser muito simples nem baseada numa palavra de dicionário.</li>			
		</ul>
	</div>

	<fieldset>
		<legend><bean:message key="label.password" bundle="ALUMNI_RESOURCES" /></legend>
		<label for="" class="">
			<bean:message key="label.password" bundle="ALUMNI_RESOURCES" />:
		</label>
		<fr:edit id="password-validated" name="passwordAccessBean" slot="password" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=createPasswordInvalid"/>
			<fr:layout name="password">
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="password-validated" /></span>
		

		<label for="" class="">
			<bean:message key="label.passwordConfirmation" bundle="ALUMNI_RESOURCES" />:
		</label>
		<fr:edit id="passwordConfirmation-validated" name="passwordAccessBean" slot="passwordConfirmation" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=createPasswordInvalid"/>
			<fr:layout name="password">
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="passwordConfirmation-validated" /></span>
	</fieldset>

	<p>
		<html:submit>
			<bean:message key="label.conclude" bundle="ALUMNI_RESOURCES" />
		</html:submit>
	</p>

</fr:form>


