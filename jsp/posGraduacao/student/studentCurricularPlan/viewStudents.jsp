<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="studentList" name="studentList" scope="request"/>

<h2 align="center"><bean:message key="title.masterDegree.administrativeOffice.chooseStudent"/></h2>
<span class="error"><html:errors/></span>
<center>
<br/>
<br/>
<table border="0" cellspacing="3" cellpadding="3">
	<tr>
		<th align="center"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
		<th align="center"><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
		<th align="center">&nbsp;</th>
		<th align="center">&nbsp;</th>
	</tr>
	<logic:iterate id="infoStudent" name="studentList" indexId="index">
		<tr>
			<td align="center">
				<bean:write name="infoStudent" property="number"/>
			</td>
			<td align="center">
				<bean:write name="infoStudent" property="infoPerson.nome"/>
			</td>
			<td align="center">
				<html:link page="/seeStudentCurricularPlans.do?method=start">
					<bean:message key="label.masterDegree.administrativeOffice.studentCurricularPlans"/>
				</html:link>
			</td>
			<td align="center">
				<html:link page="/seeStudentCurricularPlans.do?method=start">
					<bean:message key="label.masterDegree.administrativeOffice.studentData"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
</center>