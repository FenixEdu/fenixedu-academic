<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="link.workSheet" /></h2>
<br />
<p><span class="error0"><html:errors/></span></p>
<logic:messagesPresent message="true">
	<html:messages id="message" message="true">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<html:form action="viewAssiduousness">
	<html:hidden property="page" value="1" />
	<bean:define id="nextAction" name="action" type="java.lang.String"/>
	<html:hidden property="method" value="<%=nextAction%>" />
	<html:hidden property="action" value="<%=nextAction%>" />
	<bean:message key="label.employeeNumber" />
	<html:text property="employeeNumber" size="4" maxlength="4" />
	<br /><br />
	<html:submit>
		<bean:message key="button.submit" />
	</html:submit>
</html:form>
