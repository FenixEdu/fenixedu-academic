<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="hoursPattern">HH:mm</bean:define>

<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.nome"/><br />
	<bean:define id="teacherNumber" name="teacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b><bean:message key="label.execution-period" /></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br />
	(<i><html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacher" paramProperty="idInternal">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>

<h3>
	<bean:message key="label.teacher-institution-working-time.management"/>
</h3>
<bean:define id="editLink" type="java.lang.String">
	/institutionWorkingTimeManagement.do?method=prepareEdit&amp;page=0&amp;teacherId=<bean:write name="teacher" property="idInternal"/>
	&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>
</bean:define>

<html:link page="<%= editLink %>">
	<bean:message key="link.teacher-institution-working-time.create"/>
</html:link>
<br />
<br />

<logic:notPresent name="institutionWorkTimeList">
	<br />
	<span class="error"><bean:message key="message.teacher-institution-working-time-list.empty"/></span>
</logic:notPresent>

		
<logic:present name="institutionWorkTimeList">

	<bean:define id="linkDelete" type="java.lang.String">
		/institutionWorkingTimeManagement.do?method=deleteInstitutionWorkingTime&amp;page=0&amp;teacherNumber=<bean:write name="teacherNumber"/>&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>
	</bean:define>
	
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.weekday"/></td>			
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.start-time"/></td>						
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.end-time"/></td>									
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.edit"/></td>												
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.delete"/></td>																		
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
