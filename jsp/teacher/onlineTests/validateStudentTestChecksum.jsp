<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/testChecksumValidation">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="validateTestChecksum"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<bean:define id="studentCode" name="registration" property="idInternal"/>
	<bean:define id="distributedTestCode" name="distributedTest" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%=studentCode.toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=distributedTestCode.toString()%>"/>

	<table>
		<tr>
			<td><b><bean:message key="label.markSheet.name"/></b></td>
			<td><bean:write name="registration" property="student.person.name"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="label.markSheet.number"/></b></td>
			<td><bean:write name="registration" property="number"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="lable.test"/></b></td>
			<td><bean:write name="distributedTest" property="title"/>
			(<bean:write name="distributedTest" property="idInternal"/>)</td>
		</tr>
		<tr>
			<td><b><bean:message key="label.date"/><b><bean:message key="message.dateTimeFormat"/></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.dayFormatted" maxlength="19" size="19" property="date" />
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="button.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit></td>
		</tr>	
		<logic:present name="checksum">
			<tr>
				<td><b><bean:message key="label.validationCode"/></b></td>
				<td><bean:write name="checksum"/></td>
			</tr>	
		</logic:present>
	</table>
</html:form>

