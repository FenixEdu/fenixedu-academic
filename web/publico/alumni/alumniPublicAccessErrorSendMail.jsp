<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/jcaptcha.tld" prefix="jcaptcha"%>

<h1><bean:message key="label.alumni.registration" bundle="ALUMNI_RESOURCES" /></h1>

<div class="alumnilogo">
	<h2><bean:message key="title.report.error" bundle="ALUMNI_RESOURCES" /></h2>
	
	<p class="mtop15 mbottom05">
		<a href="<%= request.getContextPath() + "/publico/alumni.do?method=initFenixPublicAccess"%>">&laquo; <bean:message key="link.alumni.back" bundle="ALUMNI_RESOURCES" /></a>
	</p>
	<p class="greytxt" style="margin-bottom: 1em;"><bean:message key="message.public.error.form" bundle="ALUMNI_RESOURCES" /></p>
		
	<bean:define id="errorMessage" name="alumniErrorSendMail" property="errorMessage" type="java.lang.String"/>				
	<span class="error0"><!-- Error messages go here --><bean:message key="<%= errorMessage %>" bundle="ALUMNI_RESOURCES"/></span>
	
	<div class="reg_form">	
		<fr:form action="/alumni.do?method=sendEmailReportingError">
			<fieldset style="display: block;">
				<legend class="toplegendfix"><bean:message key="title.mail.form" bundle="ALUMNI_RESOURCES" /></legend>
				<p class="ptoplegendfix"><bean:message key="label.all.required.fields" bundle="ALUMNI_RESOURCES" /></p>
			
				<fr:edit id="alumniErrorSendMail" name="alumniErrorSendMail" schema="alumni.register.error" >
					<fr:layout name="tabular-break">
						<fr:property name="classes" value="thleft"/>
						<fr:property name="columnClasses" value=",tderror1"/>
					</fr:layout>					
				</fr:edit>
	
				<label for="captcha">
					<bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:
				</label>
				<div class="mbottom05"><img src="<%= request.getContextPath() + "/publico/jcaptcha.do" %>"/><br/></div>
				<span class="color777"><bean:message key="label.captcha.process" bundle="ALUMNI_RESOURCES" /></span><br/>
				<input type="text" name="j_captcha_response" size="30" style="display: inline;"/>
				<logic:present name="captcha.unknown.error">
					<span class="error0">
						<bean:message key="captcha.unknown.error" bundle="ALUMNI_RESOURCES" />
					</span>
				</logic:present>					
			</fieldset>
	
			<p>
				<html:submit>
					<bean:message key="label.submit" bundle="ALUMNI_RESOURCES" />
				</html:submit>
			</p>		
		</fr:form>		
	</div>
</div>

<!-- END CONTENTS -->