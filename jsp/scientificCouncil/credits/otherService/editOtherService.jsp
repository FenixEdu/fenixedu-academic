<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present role="SCIENTIFIC_COUNCIL">

<h2><bean:message key="label.teaching.service.alter" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<div class="infoop mtop2 mbottom1">
	<p class="mvert025"><b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b> <bean:write name="teacher" property="person.nome"/></p>
	<p class="mvert025"><b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b> <bean:write name="teacher" property="teacherNumber"/></p>
	<p class="mvert025"><b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/></p>
	<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal"/>
</div>
	
	<logic:messagesPresent>		
		<span class="error"><html:errors/></span>			
		<br/>
	</logic:messagesPresent>
	<html:messages id="message" message="true">
		<span class="error">
			<bean:write name="message"/>
			<br/>
		</span>
	</html:messages>	
		
	<html:form action="/otherServiceManagement">
		<html:hidden property="idInternal"/>
		<html:hidden property="otherServiceID"/>
		<html:hidden property="teacherId"/>
		<html:hidden property="executionPeriodId"/>
		<html:hidden property="page" value="1"/>	
		<html:hidden property="method" value="editOtherService"/>
		<html:hidden property="teacherNumber" name="teacher"/>
		<table>
			<tr>
				<td width="10%">
					<bean:message key="label.otherTypeCreditLine.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES" />:
				</td>
				<td>
					<html:text property="credits" size="3"/>
					<bean:message key="label.otherTypeCreditLine.credits.size" bundle="TEACHER_CREDITS_SHEET_RESOURCES" />
				</td>
			</tr>
			<tr>
				<td style="vertical-align:top">
					<bean:message key="label.otherTypeCreditLine.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:
				</td>
				<td>
					<html:textarea property="reason" cols="50" rows="3"/> <br />
				</td>
			</tr>
		</table>
		<br/>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.submit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:submit>
		<html:submit styleClass="inputbutton" onclick="this.form.method.value='cancel';this.form.page.value='0'">
			<bean:message key="button.cancel"/>
		</html:submit>
	</html:form>	
</logic:present>