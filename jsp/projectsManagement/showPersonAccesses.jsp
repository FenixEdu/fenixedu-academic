<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<bean:define id="code" value="" />
<h2><bean:message key="title.accessDelegation" /> <logic:present name="infoCostCenter" scope="request">
	&nbsp;-&nbsp;<bean:write name="infoCostCenter" property="description" />
	<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
	<bean:define id="code" value="<%="&amp;costCenter="+cc.toString()%>" />
</logic:present></h2>

<script language="Javascript" type="text/javascript">
<!--
var disable = false;

function invertStatus(){
	if ( disable == false ) { 
		disable = true; 
	} else { 
		disable = false;
	}
	for (var i=0; i<document.forms[0].projectCodes.length; i++){
		var e = document.forms[0].projectCodes[i];
		if (disable == true) { e.disabled=true; } else { e.disabled=false; }
	}
}

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
<br />
<logic:present name="personAccessesList" scope="request">
	<logic:present name="infoPerson" scope="request">
		<table>
			<tr>
				<td><strong><bean:message key="label.username" />:</strong></td>
				<td><bean:write name="infoPerson" property="username" /></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.name" />:</strong></td>
				<td><bean:write name="infoPerson" property="nome" /></td>
			</tr>
		</table>
	</logic:present>
	<h3><bean:message key="message.personProjectAccess" /></h3>
	<logic:notEmpty name="personAccessesList" scope="request">
		<table cellspacing="0">
			<tr>
				<td class="infoop"><span class="emphasis-box">Info</span></td>
				<td class="infoop"><bean:message key="message.projectAccess" /></td>
			</tr>
		</table>
		<br />
		<br />
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.username" /></th>
				<th class="listClasses-header"><bean:message key="label.name" /></th>
				<th class="listClasses-header"><bean:message key="label.projectNumber" /></th>
				<th class="listClasses-header"><bean:message key="label.acronym" /></th>
				<th class="listClasses-header"><bean:message key="label.beginDate" /></th>
				<th class="listClasses-header"><bean:message key="label.endDate" /></th>
			</tr>
			<logic:iterate id="projectAccess" name="personAccessesList">
				<bean:define id="person" name="projectAccess" property="infoPerson" />
				<bean:define id="username" name="person" property="username" />
				<bean:define id="personCode" name="person" property="idInternal" />
				<bean:define id="projectCode" name="projectAccess" property="keyProject" />
				<bean:define id="infoProject" name="projectAccess" property="infoProject" />
				<tr>
					<td td class="listClasses"><bean:write name="person" property="username" /></td>
					<td td class="listClasses"><bean:write name="person" property="nome" /></td>
					<td td class="listClasses"><bean:write name="infoProject" property="projectCode" /></td>
					<td td class="listClasses"><bean:write name="infoProject" property="title" /></td>
					<td td class="listClasses"><bean:write name="projectAccess" property="beginDateFormatted" /></td>
					<td td class="listClasses"><bean:write name="projectAccess" property="endDateFormatted" /></td>
					<td><html:link
						page="<%="/projectAccessEdition.do?method=prepareEditProjectAccess&amp;projectCode="+projectCode+"&amp;personCode="+personCode+code%>">Editar</html:link></td>
					<td><html:link page="<%="/projectAccess.do?method=removePersonAccess&amp;projectCode="+projectCode+"&amp;username="+username+code%>">Remover</html:link></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="personAccessesList" scope="request">
		<span class="error"><!-- Error messages go here --><bean:message key="message.person.noProjectAccesses" /></span>
	</logic:empty>
	<br />
	<br />
	<br />
	<br />
	<logic:present name="projectList">
		<h3><bean:message key="message.availableProjects" /></h3>
		<logic:notEmpty name="projectList">
			<table cellspacing="0">
				<tr>
					<td class="infoop"><span class="emphasis-box">Info</span></td>
					<td class="infoop"><bean:message key="message.delegateAccess" /></td>
				</tr>
			</table>
			<br />
			<html:form action="/projectAccess" focus="beginDay">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.username" property="username" value="<%=(pageContext.findAttribute("username")).toString()%>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="delegateAccess" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2" />
				<logic:present name="infoCostCenter" scope="request">
					<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.costCenter" property="costCenter" value="<%=cc.toString()%>" />
				</logic:present>
				<logic:present name="noProjectsSelected">
					<span class="error"><!-- Error messages go here --><bean:message key="errors.requiredProject" /></span>
				</logic:present>
				<table>
					<tr>
						<th class="listClasses-header"><bean:message key="label.projectNumber" /></th>
						<th class="listClasses-header"><bean:message key="label.acronym" /></th>
						<th class="listClasses-header"><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.projectCode" property="projectCode" onclick="invertStatus()" value="-1" /></th>
					</tr>
					<logic:iterate id="project" name="projectList">
						<bean:define id="projectCode" name="project" property="projectCode" />
						<tr>
							<td class="listClasses"><bean:write name="project" property="projectIdentification" /></td>
							<td class="listClasses"><bean:write name="project" property="title" /></td>
							<td class="listClasses"><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.projectCodes" property="projectCodes">
								<bean:write name="project" property="projectCode" />
							</html:multibox></td>
						</tr>
					</logic:iterate>
				</table>
				<br />
				<br />
				<table>
					<logic:present name="invalidTime">
						<tr>
							<td><span class="error"><!-- Error messages go here --><bean:message key="errors.invalid.time.interval" /></span></td>
						</tr>
					</logic:present>
					<logic:present name="invalidEndTime">
						<tr>
							<td><span class="error"><!-- Error messages go here --><bean:message key="errors.invalidEndTime" /></span></td>
						</tr>
					</logic:present>
					<tr>
						<td><span class="error"><!-- Error messages go here --><html:errors property="beginDay" /><html:errors property="beginMonth" /><html:errors property="beginYear" /></span></td>
					</tr>
					<tr>
						<td><span class="error"><!-- Error messages go here --><html:errors property="endDay" /><html:errors property="endMonth" /><html:errors property="endYear" /></span></td>
					</tr>
				</table>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><bean:message key="message.sinceDate" /><bean:message key="message.dateFormat" /></td>
						<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginDay" maxlength="2" size="2" property="beginDay" onkeyup="changeFocus(this)" /> / <html:text bundle="HTMLALT_RESOURCES" altKey="text.beginMonth" maxlength="2" size="2"
							property="beginMonth" onkeyup="changeFocus(this)" /> / <html:text bundle="HTMLALT_RESOURCES" altKey="text.beginYear" maxlength="4" size="4" property="beginYear" onkeyup="changeFocus(this)" /></td>
					</tr>
					<tr>
						<td><bean:message key="message.untilDate" /><bean:message key="message.dateFormat" /></td>
						<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endDay" maxlength="2" size="2" property="endDay" onkeyup="changeFocus(this)" /> / <html:text bundle="HTMLALT_RESOURCES" altKey="text.endMonth" maxlength="2" size="2" property="endMonth"
							onkeyup="changeFocus(this)" /> / <html:text bundle="HTMLALT_RESOURCES" altKey="text.endYear" maxlength="4" size="4" property="endYear" onkeyup="changeFocus(this)" /></td>
					</tr>
				</table>
				<br />
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.add" />
				</html:submit>
			</html:form>
		</logic:notEmpty>
	</logic:present>
	<logic:empty name="projectList">
		<span class="error"><!-- Error messages go here --><bean:message key="message.noOtherUserProjects" /></span>
	</logic:empty>
	<br />
	<br />
</logic:present>
