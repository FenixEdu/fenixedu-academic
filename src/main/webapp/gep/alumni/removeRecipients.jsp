<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.alumni" bundle="GEP_RESOURCES"/></em>
<h2><bean:message key="title.alumni.recipients.remove" bundle="GEP_RESOURCES"/></h2>

<logic:present name="errorMessage">
	<p>
		<span class="error0"><bean:write name="errorMessage"/></span>
	</p>
</logic:present>

<fr:form action="/alumni.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="alumniForm" value="removeRecipients"/>
	<fr:edit id="emailBean" name="emailBean" schema="alumni.gep.remove.recipients">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop05 ulnomargin thnowrap"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:property name="displayHeaders" value="false"/>
		</fr:layout>	
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.alumni.remove" bundle="GEP_RESOURCES"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='manageRecipients'">
		<bean:message key="button.cancel" bundle="APPLICATION_RESOURCES"/>
	</html:cancel>
</fr:form>