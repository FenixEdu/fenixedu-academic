<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<p class="infoselected">
	<b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacher" property="person.nome"/><br />
	<b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacher" property="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br/>
	<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal"/>
	(<i><html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacher" paramProperty="idInternal">
		<bean:message key="link.teacherCreditsSheet.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:link></i>)
</p>

<bean:define id="link">
	/otherServiceManagement.do?executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="teacher" property="idInternal"/>&amp;teacherNumber=<bean:write name="teacher" property="teacherNumber"/>
</bean:define>

<html:link page='<%= link + "&amp;page=0&amp;method=prepareEditOtherService" %>' >
	<bean:message key="link.otherTypeCreditLine.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>	
</html:link>
<br/><br/>


<logic:present name="otherServices">
	<table width="100%">
		<tr>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.otherTypeCreditLine.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" style="text-align:left">
				<bean:message key="label.otherTypeCreditLine.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.otherTypeCreditLine.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.otherTypeCreditLine.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
		</tr>
		<logic:iterate id="otherService" name="otherServices">
			<tr>
				<td class="listClasses"><bean:write name="otherService" property="credits"/></td>
				<td class="listClasses" style="text-align:left">
					<pre><bean:write name="otherService" property="reason"/></pre>
				</td>
				<td class="listClasses">
					<html:link page='<%= link + "&amp;page=0&amp;method=prepareEditOtherService" %>' paramId="otherServiceID" paramName="otherService" paramProperty="idInternal">
						<bean:message key="link.otherTypeCreditLine.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page='<%= link + "&amp;page=0&amp;method=deleteOtherService" %>' paramId="otherServiceID" paramName="otherService" paramProperty="idInternal">
						<bean:message key="link.otherTypeCreditLine.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
<logic:notPresent name="otherServices">
	<span class="error"><bean:message key="message.otherTypeCreditLine.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span>
</logic:notPresent>

