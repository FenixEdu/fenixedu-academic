<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jcaptcha.sourceforge.net" prefix="jcaptcha"%>

<!-- alumniPublicAccessIdentityCheck.jsp -->


<h1><bean:message key="label.alumni.registration" bundle="ALUMNI_RESOURCES" /></h1>

<div class="alumnilogo" style="background: none;">
<%-- <div class="col_right_alumni"><img src="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>img/alumni/alumni_deco_3.gif" alt="[Image] Alumni" /></div> --%>
	
	<h2><bean:message key="label.alumni.form" bundle="ALUMNI_RESOURCES" /></h2>
	
	<p class="mtop15 mbottom05">
		<a href="<%= request.getContextPath() + "/publico/alumni.do?method=initFenixPublicAccess"%>">&laquo; <bean:message key="link.alumni.back" bundle="ALUMNI_RESOURCES" /></a>
	</p>
	
	<p class="greytxt" style="margin-bottom: 1em;"><bean:message key="label.alumni.student.number.recovery.text" bundle="ALUMNI_RESOURCES" /></p>
	
	<logic:present name="alumniPublicAccessMessage">
		<span class="error0"><bean:write name="alumniPublicAccessMessage" scope="request" /></span><br/>
	</logic:present>
	
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</html:messages>	
	
	<div class="reg_form">	
		<fr:form action="/alumni.do?method=processRequestIdentityCheck">
	
			<fieldset style="display: block;">
				<h3><bean:message key="label.alumni.student.number.recovery" bundle="ALUMNI_RESOURCES" /></h3>
				<p class="ptoplegendfix"><bean:message key="label.all.required.fields" bundle="ALUMNI_RESOURCES" /></p>
			
				<fr:edit id="alumniBean" name="alumniBean" visible="false" />
	
				<fr:edit id="alumniFormationDegree" name="alumniBean" schema="alumni.identity.check.request" >
					<fr:layout name="tabular-break">
						<fr:property name="classes" value="thleft"/>
						<fr:property name="columnClasses" value=",tderror1"/>
					</fr:layout>
					<fr:destination name="invalid" path="/alumni.do?method=processRequestIdentityCheckError"/>
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
