<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="hoursPattern">HH:mm</bean:define>

<bean:define id="infoTeacher" name="teacherInstitutionWorkingTime" property="infoTeacher" scope="request" />
<bean:define id="infoExecutionPeriod" name="teacherInstitutionWorkingTime" property="infoExecutionPeriod" scope="request" />
<bean:define id="infoTeacherInstitutionWorkingTimeList" name="teacherInstitutionWorkingTime" property="infoTeacherInstitutionWorkTimeList" scope="request" />
<bean:define id="executionPeriodId" name="infoExecutionPeriod" property="idInternal" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b><bean:message key="label.execution-period" /></b> <bean:write name="infoExecutionPeriod" property="description"/><br />
	(<i><html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>

<h3>
	<bean:message key="label.teacher-institution-working-time.management"/>
</h3>
<bean:define id="link" type="java.lang.String">
/manageTeacherInstitutionWorkingTime.do?method=prepareEdit&amp;page=0&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>
&amp;executionPeriodId=<bean:write name="infoExecutionPeriod" property="idInternal"/>
</bean:define>
<html:link page="<%= link %>">
	<bean:message key="link.teacher-institution-working-time.create"/>
</html:link>
<br />
<br />

<bean:size id="listSize" name="infoTeacherInstitutionWorkingTimeList"/>

<logic:equal name="listSize" value="0">
	<br />
	<span class="error"><bean:message key="message.teacher-institution-working-time-list.empty"/></span>
</logic:equal>

<logic:greaterThan name="listSize" value="0">
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.weekday"/></td>			
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.start-time"/></td>						
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.end-time"/></td>									
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.edit"/></td>												
			<td class="listClasses-header"><bean:message key="label.teacher-institution-working-time.delete"/></td>																		
		</tr>

		<bean:define id="linkDelete" type="java.lang.String">
			/manageTeacherInstitutionWorkingTime.do?method=delete&amp;page=0&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;executionPeriodId=<bean:write name="infoExecutionPeriod" property="idInternal"/>
		</bean:define>
		<logic:iterate id="infoTeacherInstitutionWorkingTime" name="infoTeacherInstitutionWorkingTimeList">
			<tr>
				<td class="listClasses">
					<bean:write name="infoTeacherInstitutionWorkingTime" property="weekDay"/>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="infoTeacherInstitutionWorkingTime" property="startTime.time"/>
					</dt:format>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="infoTeacherInstitutionWorkingTime" property="endTime.time"/>
					</dt:format>
				</td>			
				<td  class="listClasses">
					<html:link page="<%= link %>" paramId="idInternal" paramName="infoTeacherInstitutionWorkingTime" paramProperty="idInternal" >
						<bean:message key="link.edit"/>
					</html:link>
				</td>
				<td  class="listClasses">
					<html:link page="<%= linkDelete %>" paramId="idInternal" paramName="infoTeacherInstitutionWorkingTime" paramProperty="idInternal" >
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</logic:greaterThan>
