<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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