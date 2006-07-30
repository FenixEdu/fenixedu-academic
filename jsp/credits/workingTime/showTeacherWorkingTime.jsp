<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="hoursPattern">HH:mm</bean:define>
<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal" />

<h2><bean:message key="label.teaching.service.alter" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<div class="infoop mtop2 mbottom1">
	<p class="mvert025"><b><bean:message key="label.teacher.name" />:</b> <bean:write name="teacher" property="person.nome"/></p>
	<p class="mvert025"><bean:define id="teacherNumber" name="teacher" property="teacherNumber"/><b><bean:message key="label.teacher.number" />:</b> <bean:write name="teacherNumber"/></p>
	<p class="mvert025"><b><bean:message key="label.execution-period" />:</b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/></p>
</div>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>


<bean:define id="editLink" type="java.lang.String">
	/institutionWorkingTimeManagement.do?method=prepareEdit&amp;page=0&amp;teacherId=<bean:write name="teacher" property="idInternal"/>
	&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>
</bean:define>

<ul>
	<li>
	<bean:define id="link2">/showFullTeacherCreditsSheet.do?method=showTeacherCredits&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="teacher" property="idInternal"/></bean:define>
	<html:link page='<%= link2 %>'><bean:message key="link.return"/></html:link>
	</li>
	<li>
	<html:link page="<%= editLink %>"><bean:message key="link.teacher-institution-working-time.create"/></html:link>
	</li>
</ul>

<p class="mbottom0"><strong><bean:message key="label.teacher-institution-working-time.management"/></strong></p>

<logic:notPresent name="institutionWorkTimeList">
	<br />
	<span class="error"><!-- Error messages go here --><bean:message key="message.teacher-institution-working-time-list.empty"/></span>
	<br />
</logic:notPresent>

		
<logic:present name="institutionWorkTimeList">

	<bean:define id="linkDelete" type="java.lang.String">
		/institutionWorkingTimeManagement.do?method=deleteInstitutionWorkingTime&amp;page=0&amp;teacherNumber=<bean:write name="teacherNumber"/>&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>
	</bean:define>
	
	<table class="tstyle4">
		<tr>
			<th><bean:message key="label.teacher-institution-working-time.weekday"/></th>			
			<th><bean:message key="label.teacher-institution-working-time.start-time"/></th>						
			<th><bean:message key="label.teacher-institution-working-time.end-time"/></th>									
			<th><bean:message key="label.teacher-institution-working-time.edit"/></th>												
			<th><bean:message key="label.teacher-institution-working-time.delete"/></th>																		
		</tr>
		
		<logic:iterate id="teacherInstitutionWorkingTime" name="institutionWorkTimeList">
			<tr>
				<td class="listClasses">
					<bean:message name="teacherInstitutionWorkingTime" property="weekDay.name" bundle="ENUMERATION_RESOURCES"/>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="teacherInstitutionWorkingTime" property="startTime.time"/>
					</dt:format>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="teacherInstitutionWorkingTime" property="endTime.time"/>
					</dt:format>
				</td>			
				<td  class="listClasses">
					<html:link page="<%= editLink %>" paramId="institutionWorkTimeID" paramName="teacherInstitutionWorkingTime" paramProperty="idInternal" >
						<bean:message key="link.edit"/>
					</html:link>
				</td>
				<td  class="listClasses">
					<html:link page="<%= linkDelete %>" paramId="institutionWorkTimeID" paramName="teacherInstitutionWorkingTime" paramProperty="idInternal" >
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</logic:present>
