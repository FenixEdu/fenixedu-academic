<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><html:errors/></span>	
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exam" name="component" property="infoExam"/>
<h2><bean:message key="label.students.enrolled.exam"/></h2>
<br/>
<table>
<tr>
	<td class="listClasses-header" ><bean:message key="label.season"/></td>
	<td class="listClasses-header" ><bean:message key="label.day"/></td>
	<td class="listClasses-header" ><bean:message key="label.beginning"/></td>	
	<td class="listClasses-header"><bean:message key="label.number.students.enrolled"/></td>
</tr>
<tr>
	<td class="listClasses"><bean:write name="exam" property="season"/></td>
	<td class="listClasses"><bean:write name="exam" property="date"/></td>
	<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
	<td class="listClasses"><bean:write name="component" property="size"/></td>
</tr>
</table>
<table>
<tr>
<td class="listClasses-header"><bean:message key="label.number"/></td>
<td class="listClasses-header"><bean:message key="label.name"/></td>
</tr>
<logic:iterate id="student" name="component" property="infoStudents">
<bean:define id="person" name="student" property="person"/>
<tr>
<td class="listClasses"><bean:write name="student" property="number"/></td>
<td class="listClasses"><bean:write name="person" property="nome"/></td>
</tr>
</logic:iterate>
</table>
