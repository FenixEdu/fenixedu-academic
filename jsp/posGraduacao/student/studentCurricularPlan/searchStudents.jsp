<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="docIDTypeList" name="docIDTypeList" scope="request"/>

<h2 align="center"><bean:message key="title.masterDegree.administrativeOffice.chooseStudent"/></h2>
<span class="error"><html:errors/></span>
<center>
<br/>
<br/>
<html:form action="/seeStudentCurricularPlans.do">
	<html:hidden property="method" value="read"/>
	<html:hidden property="page" value="1"/>
	<table border="0">
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/>:&nbsp;</td>
			<td align="left">
				<input type="text" name="studentName" size="20" value=""/>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.idNumber"/>:&nbsp;</td>
			<td align="left">
				<input type="text" name="idNumber" size="20" value=""/>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.idType"/>:&nbsp;</td>
			<td align="left">
				<html:select property="idType" size="1">
					<html:options collection="docIDTypeList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/>:&nbsp;</td>
			<td align="left">
				<input type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="label.masterDegree.administrativeOffice.studentSearchSubmit"/>
	</html:submit>
</html:form>
</center>