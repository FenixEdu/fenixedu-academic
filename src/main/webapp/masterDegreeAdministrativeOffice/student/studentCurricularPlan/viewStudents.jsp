<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<bean:define id="studentList" name="studentList" scope="request"/>

<h2 align="left"><bean:message key="title.masterDegree.administrativeOffice.chooseStudent"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<table border="0" cellspacing="3" cellpadding="10">
	<tr>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
		<th align="left">&nbsp;</th>
		<th align="left">&nbsp;</th>
	</tr>
	<logic:iterate id="infoStudent" name="studentList" indexId="index">
		<bean:define id="seeStudentCurricularPlansLink">
			/seeStudentCurricularPlans.do?studentId=<bean:write name="infoStudent" property="externalId"/>
		</bean:define>
<%--
		<bean:define id="qqCoisaLink">
			/qqCoisa.do?studentId=<bean:write name="infoStudent" property="externalId"/>
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
