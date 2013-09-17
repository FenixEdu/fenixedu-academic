<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="infoStudent" name="student" scope="request"/>

<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<b><bean:message key="label.masterDegree.administrativeOffice.name.of.chosen.student"/>:&nbsp;</b>
			<bean:write name="infoStudent" property="infoPerson.nome"/>
			<br/><br/>
			<b><bean:message key="label.masterDegree.administrativeOffice.number.of.chosen.student"/>:&nbsp;</b>
			<bean:write name="infoStudent" property="number"/>
		</td>
	</tr>
</table>
<br/>
