<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="infoTeacher" name="dto" property="infoTeacher"/>
<bean:define id="infoExecutionPeriod" name="dto" property="infoExecutionPeriod"/>
<bean:define id="otherTypeCreditLines" name="dto" property="creditLines"/>
<p class="infoselected">
	<b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="infoExecutionPeriod" property="description"/>
	<bean:define id="executionPeriodId" name="infoExecutionPeriod" property="idInternal"/>
	(<i><html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="link.teacherCreditsSheet.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:link></i>)
</p>

<bean:define id="link">
	/crudOtherTypeCreditLine.do?executionPeriodId=<bean:write name="infoExecutionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;teacherNumber=<bean:write name="infoTeacher" property="teacherNumber"/>
</bean:define>

<logic:notEmpty name="otherTypeCreditLines">
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
		<logic:iterate id="creditLine" name="otherTypeCreditLines">
			<tr>
				<td class="listClasses"><bean:write name="creditLine" property="credits"/></td>
				<td class="listClasses" style="text-align:left">
					<pre><bean:write name="creditLine" property="reason"/></pre>
				</td>
				<td class="listClasses">
					<html:link page='<%= link + "&amp;page=0&amp;method=prepareEdit" %>' paramId="idInternal" paramName="creditLine" paramProperty="idInternal">
						<bean:message key="link.otherTypeCreditLine.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page='<%= link + "&amp;page=0&amp;method=delete" %>' paramId="idInternal" paramName="creditLine" paramProperty="idInternal">
						<bean:message key="link.otherTypeCreditLine.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
<logic:empty name="otherTypeCreditLines">
	<span class="error"><bean:message key="message.otherTypeCreditLine.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span>
</logic:empty>
<br />
<br />
<html:link page='<%= link + "&amp;page=0&amp;method=prepareEdit" %>' >
	<bean:message key="link.otherTypeCreditLine.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>	
</html:link>

