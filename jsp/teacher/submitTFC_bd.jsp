<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoTeacher" %>
<%@ page import="DataBeans.InfoPerson" %>

<%
InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
InfoPerson infoPerson = (InfoPerson) infoTeacher.getInfoPerson();

%>


<h2><bean:message key="title.teacher.tfcInformation"/></h2>
<html:form action="/tfcManagement">
<html:hidden property="method" value="submit"/>

<b><bean:message key="label.teacher.tfc.title"/>:</b>
<br><html:text property="title" size="100"/>
<hr><br>

<b><bean:message key="label.teacher.tfc.responsable"/>:</b>
<table width="100%">
	<tr>
		<th width="16%"><bean:message key="label.teacher.tfc.number"/>:</th>
		<td width="10%">
			<html:text property="responsableTeacherNumber" maxlength="6" size="6" readonly="true"
				value='<%= infoTeacher.getTeacherNumber().toString() %>'/>
		</td>
		<td width="1%"/>
		<th width="7%"><bean:message key="label.teacher.tfc.name"/>:</th>
		<td width="66%">
			<html:text property="responsableTeacherName" size="70" 
				value="<%= infoPerson.getNome() %>" readonly="true"/></td>
	</tr>
	<tr>
		<td width="16%"></td>
		<td width="10%"></td>
		<td width="1%"/>
		<th width="7%"><bean:message key="label.teacher.tfc.section"/>:</th>
		<td width="66%"><html:text property="responsableTeacherSection" size="70"/></td>
	</tr>
</table>
<br><br>


<b><bean:message key="label.teacher.tfc.coResponsable"/>:</b>

<table width="100%">
	<tr>
		<th width="16%"><bean:message key="label.teacher.tfc.number"/>:</th>
		<td width="10%">
			<html:text property="coResponsableTeacherNumber" maxlength="6" size="6"/>
		</td>
		<td width="1%"/>
		<th width="7%"><bean:message key="label.teacher.tfc.name"/>:</th>
		<td width="66%"><html:text property="coResponsableTeacherName" size="70"/></td>
	</tr>
	<tr>
		<td width="16%"></td>
		<td width="10%"></td>
		<td width="1%"/>
		<th width="7%"><bean:message key="label.teacher.tfc.section"/>:</th>
		<td width="66%"><html:text property="coResponsableTeacherSection" size="70"/></td>
	</tr>
</table>
<br>
<b><bean:message key="label.teacher.tfc.credits"/>:</b>
<html:text property="responsibleCreditsPercentage" size="3" maxlength="3" value="100"/>% /
<html:text property="coResponsibleCreditsPercentage" size="3" maxlength="3" value="0"/>%
<br><hr>

<b><bean:message key="label.teacher.tfc.objectives"/>:</b>
<br><html:textarea property="objectives" rows="4" cols="80"/>
<br><br>
<b><bean:message key="label.teacher.tfc.description"/>:</b>
<br><html:textarea property="description" rows="10" cols="80"/>
<br><br>
<b><bean:message key="label.teacher.tfc.requirements"/>:</b>
<br><html:textarea property="requirements" rows="10" cols="80"/>
<br><br>
<b><bean:message key="label.teacher.tfc.url"/>:</b>
<br><html:text property="url" size="100"/>
<br><hr>
<br><b><bean:message key="label.teacher.tfc.priority.info"/></b><br><br>

<bean:define id="branches" name="branchList" scope="request"/>
<table border="1">
	<tr>
		<th align="center"><bean:message key="label.teacher.tfc.branch"/></th>
		<th align="center"><bean:message key="label.teacher.tfc.priority"/></th>
	</tr>
	<logic:iterate id="branch" name="branches">
	<tr>
		<td>
			<bean:write name="branch" property="name"/>				
		</td>
		<td>
			<input type="radio" name="<bean:write name="branch" property="code"/>" value="1"/>1
			<input type="radio" name="<bean:write name="branch" property="code"/>" value="2"/>2
			<input type="radio" name="<bean:write name="branch" property="code"/>" value="3"/>3
			<input type="radio" name="<bean:write name="branch" property="code"/>" value="4"/>4
		</td>
	</tr>
</logic:iterate>
</table>

<br><hr><br>
<table cellspacing="2">
	<tr>
		<th><bean:message key="label.teacher.tfc.numberOfGroupElements"/>:</th>
		<td>
			<html:multibox value="1" property="numberOfGroupElements"/> 1 
			<html:multibox value="2" property="numberOfGroupElements"/> 2
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.tfc.degreeType"/>:</th>
		<td><html:multibox value="1" property="degreeType"/> Licenciatura</td>
		<td><html:multibox value="2" property="degreeType"/> Mestrado Integrado</td>
	</tr>
</table>
<br><hr><br>
<b><bean:message key="label.teacher.tfc.partA"/></b>
<br><html:textarea property="requirements" rows="4" cols="80"/><br><br>
<b><bean:message key="label.teacher.tfc.partB"/></b>
<br><html:textarea property="requirements" rows="4" cols="80"/><br>
<br><hr><br>
<b><bean:message key="label.teacher.tfc.observations"/>:</b>
<br><html:textarea property="requirements" rows="4" cols="80"/><br>
<br><hr><br>
<b><bean:message key="label.teacher.tfc.company"/></b><br>
<br><b><bean:message key="label.teacher.tfc.companyName"/>:</b>
<br><html:text property="companyName" size="70"/>
<br><b><bean:message key="label.teacher.tfc.companyLinkResponsable"/>:</b>
<br><html:text property="companyLinkResponsable" size="70"/>
<br><b><bean:message key="label.teacher.tfc.companyContact"/>:</b>
<br><html:text property="companyContact" size="70"/>

<br><br><html:submit styleClass="inputbutton"><bean:message key="button.submit"/></html:submit></td>
</html:form>