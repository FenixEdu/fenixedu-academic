<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoExecutionDegreesNamesList" name="<%= SessionConstants.DEGREE_LIST %>"/>

<center>
<h2><bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="title.student.enrolment.without.rules"/></h2>
<html:form action="/prepareEnrolmentContext.do">
	<table border="0">
		<tr>
			<td align="left"><bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.choose.degree"/>:&nbsp;</td>
			<td align="left">
				<html:select property="infoExecutionDegreeName" size="1">
					<html:options collection="infoExecutionDegreesNamesList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.choose.semester"/>:&nbsp;</td>
			<td align="left">
				<html:select property="semester" size="1">
					<html:option bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.first.semester" value="1"/>
					<html:option bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.second.semester" value="2"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.choose.year"/>:&nbsp;</td>
			<td align="left">
				<html:select property="year" size="1">
					<html:option bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.first.year" value="1"/>
					<html:option bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.second.year" value="2"/>
					<html:option bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.third.year" value="3"/>
					<html:option bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.fourth.year" value="4"/>
					<html:option bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="label.fiveth.year" value="5"/>
				</html:select>
			</td>
		</tr>
	</table>
	<p>&nbsp;</p>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="DEGREE_ADMIN_OFFICE_RESOURCES" key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>
</center>