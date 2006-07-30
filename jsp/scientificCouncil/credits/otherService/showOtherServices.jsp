<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
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
	
	<bean:define id="link">
		/otherServiceManagement.do?executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="teacher" property="idInternal"/>&amp;
	</bean:define>

<ul>	
	<li>
	<bean:define id="link2">/showFullTeacherCreditsSheet.do?method=showTeacherCredits&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="teacher" property="idInternal"/></bean:define>
	<html:link page='<%= link2 %>'><bean:message key="return"/></html:link>
	</li>
	<li>
	<html:link page='<%= link + "&amp;page=0&amp;method=prepareEditOtherService" %>'><bean:message key="link.otherTypeCreditLine.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></html:link>
	</li>
</ul>
	
	<logic:present name="otherServices">
		<table class="tstyle4">
			<tr>
				<th><bean:message key="label.otherTypeCreditLine.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.otherTypeCreditLine.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.otherTypeCreditLine.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.otherTypeCreditLine.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			</tr>
			<logic:iterate id="otherService" name="otherServices">
				<tr>
					<td><bean:write name="otherService" property="credits"/></td>
					<td style="text-align:left">
						<pre style="margin: 0pt; padding: 0pt; font-family: Verdana; font-size: 1em;"><bean:write name="otherService" property="reason"/></pre>
					</td>
					<td>
						<html:link page='<%= link + "&amp;page=0&amp;method=prepareEditOtherService" %>' paramId="otherServiceID" paramName="otherService" paramProperty="idInternal">
							<bean:message key="link.otherTypeCreditLine.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
						</html:link>
					</td>
					<td>
						<html:link page='<%= link + "&amp;page=0&amp;method=deleteOtherService" %>' paramId="otherServiceID" paramName="otherService" paramProperty="idInternal">
							<bean:message key="link.otherTypeCreditLine.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>
	<logic:notPresent name="otherServices">
		<span class="error"><!-- Error messages go here --><bean:message key="message.otherTypeCreditLine.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span>
		<br/>
	</logic:notPresent>
</logic:present>	
