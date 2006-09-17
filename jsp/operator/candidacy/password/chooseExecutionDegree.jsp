<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<br />
<html:form action="/generatePasswordsForCandidacies.do?method=chooseExecutionDegree">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

	<h3><bean:message key="label.operator.candidacy.passwords.chooseExecutionDegree" /></h3>

	<html:select property="executionDegreeId">
		<html:option value="0">&nbsp;</html:option>
		<html:options collection="executionDegrees" property="idInternal" labelProperty="degree.presentationName"/>
	</html:select>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Ok" styleClass="inputbutton" property="OK"/>
</html:form> 