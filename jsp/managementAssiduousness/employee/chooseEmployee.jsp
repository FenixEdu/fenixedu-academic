<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<bean:define id="nextAction" name="action" type="java.lang.String"/>
<h2><bean:message key="<%= "title." + nextAction %>" /></h2>
<p class="mtop2"><span class="error0"><html:errors/></span></p>
<logic:messagesPresent message="true">
	<html:messages id="message" message="true">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<html:form action="viewEmployeeAssiduousness">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="<%=nextAction%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action" value="<%=nextAction%>" />
	<bean:message key="label.employeeNumber" />
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.employeeNumber" property="employeeNumber" size="4" maxlength="4" />
	<br /><br />
	<html:submit>
		<bean:message key="button.submit" />
	</html:submit>
</html:form>
