<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<script language="Javascript" type="text/javascript">
<!--
function changeFocus(input) { 
	if( input.value.length == input.maxLength) { 
		next=getIndex(input)+1;
		if (next<document.forms[0].elements.length){
			document.forms[0].elements[next].focus();
		}
	} 
} 

function getIndex(input){
	var index = -1, i = 0; 
	while ( i < input.form.length && index == -1 ) 
	if ( input.form[i] == input ) { 
		index = i; 
	} else { 
		i++; 
	} 
	return index; 
}
// -->
</script>
<h2><bean:message key="title.changeProjectAccessDates" /></h2>
<br />
<br />
<logic:present name="infoProjectAccess" scope="request">
	<bean:define id="projectCode" name="infoProjectAccess" property="keyProject" />
	<bean:define id="person" name="infoProjectAccess" property="infoPerson" />
	<bean:define id="infoProject" name="infoProjectAccess" property="infoProject" />
	<bean:define id="username" name="person" property="username" />
	<html:form action="/projectAccessEdition" focus="beginDay">
		<html:hidden property="method" value="editProjectAccess" />
		<html:hidden property="projectCode" value="<%=projectCode.toString()%>" />
		<bean:define id="personCode" name="person" property="idInternal" />
		<html:hidden property="personCode" value="<%=personCode.toString()%>" />
		<html:hidden property="username" value="<%=username.toString()%>" />
		<html:hidden property="page" value="1" />

		<table>
			<tr>
				<td><strong><bean:message key="label.username" />:</strong></td>
				<td><bean:write name="person" property="username" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.name" />:</strong></td>
				<td><bean:write name="person" property="nome" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.projectNumber" />:</strong></td>
				<td><bean:write name="infoProject" property="projectIdentification" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.acronym" />:</strong></td>
				<td><bean:write name="infoProject" property="title" /></td>
			</tr>
		</table>
		<br />
		<br />
		<table>
			<logic:present name="invalidTime">
				<tr>
					<td><span class="error"><bean:message key="errors.invalid.time.interval" /></span></td>
				</tr>
			</logic:present>
			<logic:present name="invalidEndTime">
				<tr>
					<td><span class="error"><bean:message key="errors.invalidEndTime" /></span></td>
				</tr>
			</logic:present>
			<tr>
				<td><span class="error"><html:errors property="beginDay" /><html:errors property="beginMonth" /><html:errors property="beginYear" /></span></td>
			</tr>
			<tr>
				<td><span class="error"><html:errors property="endDay" /><html:errors property="endMonth" /><html:errors property="endYear" /></span></td>
			</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><bean:message key="message.sinceDate" /><bean:message key="message.dateFormat" /></td>
				<td><html:text maxlength="2" size="2" property="beginDay" onkeyup="changeFocus(this)" /> / <html:text maxlength="2" size="2"
					property="beginMonth" onkeyup="changeFocus(this)" /> / <html:text maxlength="4" size="4" property="beginYear" onkeyup="changeFocus(this)" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.untilDate" /><bean:message key="message.dateFormat" /></td>
				<td><html:text maxlength="2" size="2" property="endDay" onkeyup="changeFocus(this)" /> / <html:text maxlength="2" size="2" property="endMonth"
					onkeyup="changeFocus(this)" /> / <html:text maxlength="4" size="4" property="endYear" onkeyup="changeFocus(this)" /></td>
			</tr>
		</table>
		<br />
		<table>
			<tr>
				<td><html:submit styleClass="inputbutton">
					<bean:message key="label.confirm" />
				</html:submit>
	</html:form>
	</td>
	<td><html:form action="/projectAccess">
		<html:hidden property="method" value="showPersonAccesses" />
		<html:hidden property="username" value="<%=username.toString()%>" />
		<html:submit styleClass="inputbutton">
			<bean:message key="label.back" />
		</html:submit>
	</html:form></td>
	</tr>
	</table>
</logic:present>
