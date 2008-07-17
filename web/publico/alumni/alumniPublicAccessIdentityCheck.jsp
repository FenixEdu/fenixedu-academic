<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/jcaptcha.tld" prefix="jcaptcha"%>

<!-- alumniPublicAccessIdentityCheck.jsp -->


<h1>Alumni</h1>

<div class="alumnilogo">
<%-- <div class="col_right_alumni"><img src="http://www.ist.utl.pt/img/alumni/alumni_deco_3.gif" alt="[Image] Alumni" /></div> --%>

<h2><bean:message key="label.alumni.student.number.recovery" bundle="ALUMNI_RESOURCES" /></h2>

<p><a href="<%= request.getContextPath() + "/conteudos-publicos/registo-alumni"%>">« Voltar</a></p>


<p class="greytxt" style="margin-bottom: 1em;"><bean:message key="label.alumni.student.number.recovery.text" bundle="ALUMNI_RESOURCES" /></p>

<logic:present name="alumniPublicAccessMessage">
	<span class="error0"><bean:write name="alumniPublicAccessMessage" scope="request" /></span><br/>
</logic:present>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>


	<fr:form id="reg_form" action="/alumni.do?method=processRequestIdentityCheck">

		<fieldset style="display: block;">
			<legend class="toplegendfix"><bean:message key="label.alumni.form" bundle="ALUMNI_RESOURCES" /></legend>
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

<!-- END CONTENTS -->





