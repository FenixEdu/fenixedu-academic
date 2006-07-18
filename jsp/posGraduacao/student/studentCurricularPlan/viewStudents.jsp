<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="studentList" name="studentList" scope="request"/>

<h2 align="left"><bean:message key="title.masterDegree.administrativeOffice.chooseStudent"/></h2>
<span class="error"><html:errors/></span>
<table border="0" cellspacing="3" cellpadding="10">
	<tr>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
		<th align="left">&nbsp;</th>
		<th align="left">&nbsp;</th>
	</tr>
	<logic:iterate id="infoStudent" name="studentList" indexId="index">
		<bean:define id="seeStudentCurricularPlansLink">
			/seeStudentCurricularPlans.do?studentId=<bean:write name="infoStudent" property="idInternal"/>
		</bean:define>
<%--
		<bean:define id="qqCoisaLink">
			/qqCoisa.do?studentId=<bean:write name="infoStudent" property="idInternal"/>
		</bean:define>
--%>
		<tr>
			<td align="left">
				<bean:write name="infoStudent" property="number"/>
			</td>
			<td align="left">
				<bean:write name="infoStudent" property="infoPerson.nome"/>
			</td>
			<td align="left">
				<html:link page="<%= pageContext.findAttribute("seeStudentCurricularPlansLink").toString() %>">
					<bean:message key="label.masterDegree.administrativeOffice.studentCurricularPlans"/>
				</html:link>
			</td>
			<td align="left">
				<bean:define id="studentDataLink">
		        		/chooseStudentToVisualizeInformations.do?method=execute&studentNumber=<bean:write name="infoStudent" property="number"/>
		        </bean:define>
				<html:link page="<%= pageContext.findAttribute("studentDataLink").toString() %>">
					<bean:message key="label.masterDegree.administrativeOffice.studentData"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
