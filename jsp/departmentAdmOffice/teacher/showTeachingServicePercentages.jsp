<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="hoursPattern">HH : mm</bean:define>

<bean:define id="teacher" name="professorship" property="teacher" scope="request" />
<bean:define id="executionCourse" name="professorship" property="executionCourse" scope="request" />
<bean:define id="executionPeriodId" name="executionCourse" property="executionPeriod.idInternal" />
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.nome"/><br />
	<bean:define id="teacherNumber" name="teacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" /></b> <bean:write name="executionCourse" property="nome"/> <br />
	<b><bean:message key="label.execution-period" /></b> <bean:write name="executionCourse" property="executionPeriod.name"/> - <bean:write name="executionCourse" property="executionPeriod.executionYear.year"/> <br />	
	(<i><html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacher" paramProperty="idInternal">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>

<html:errors/>

<html:form action="/degreeTeachingServiceManagement">
	<html:hidden property="teacherNumber" value="<%= teacherNumber.toString() %>"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="executionPeriodId" />
	<html:hidden property="executionCourseId"/>
	<html:hidden property="professorshipID"/>
	<html:hidden property="method" value="updateTeachingServices"/>

	<table width="100%" cellspacing="1" cellpadding="5">
		<%-- ********************************* HEADER *********************************************** --%>
		<tr>
			<td rowspan="2" class="listClasses-header" width="10%"><bean:message key="label.shift"/></td>
			<td rowspan="2" class="listClasses-header" width="5%"><bean:message key="label.shift.type"/></td>
			<td colspan="4" class="listClasses-header" width="40%"><bean:message key="label.lessons"/></td>
			<td rowspan="2" class="listClasses-header"><bean:message key="label.professorship.percentage"/></td>
			<td class="listClasses-header"><bean:message key="label.teacher.applied"/></td>			
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="label.day.of.week"/></td>
			<td class="listClasses-header"><bean:message key="label.lesson.start"/></td>
			<td class="listClasses-header"><bean:message key="label.lesson.end"/></td>
			<td class="listClasses-header"><bean:message key="label.lesson.room"/></td>			
			<td class="listClasses-header"><bean:message key="label.teacher"/> - <bean:message key="label.professorship.percentage"/></td>
		</tr> 
		<%-- ********************************* SHIFTS *********************************************** --%>	
		<logic:iterate id="teachingServicePercentage" name="teachingServicePercentages">
			<bean:define id="shift" name="teachingServicePercentage" property="shift"/>
			<bean:define id="availablePercentage" name="teachingServicePercentage" property="availablePercentage"/>
			
			<bean:size id="lessonsSize" name="shift" property="associatedLessons" />	

				<logic:equal name="lessonsSize" value="0">
					<tr>
						<td class="listClasses"><bean:write name="shift" property="nome"/></td>
						<td class="listClasses"><bean:write name="shift" property="tipo.siglaTipoAula"/></td>
						<td class="listClasses" colspan="7"> Não tem aulas </td>
					</tr>
				</logic:equal>

				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="lesson" name="shift" property="associatedLessons" indexId="indexLessons" >
			            <logic:equal name="indexLessons" value="0">

							<tr>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="shift" property="nome"/></td>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="shift" property="tipo.siglaTipoAula"/></td>
							<td class="listClasses">
								<bean:write name="lesson" property="diaSemana"/>
							</td>
							<td class="listClasses">
								<dt:format patternId="hoursPattern">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
							</td>
							<td class="listClasses">

								<dt:format patternId="hoursPattern">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
							</td>
							<td class="listClasses">
								<bean:write name="lesson" property="sala.nome"/>					
							</td>
							<td class="listClasses" rowspan="<%= lessonsSize %>">
								<logic:greaterThan name="availablePercentage" value="0">
									<bean:define id="propertyName">
										teacherPercentageMap(<bean:write name="shift" property="idInternal"/>)
									</bean:define>
									<html:text property='<%= propertyName %>' size="4" /> %
								</logic:greaterThan>
								<logic:equal name="availablePercentage" value="0">
									&nbsp;
								</logic:equal>
							</td>
							<td class="listClasses" rowspan="<%= lessonsSize %>">
								<bean:size id="teachingServiceSize" name="shift" property="degreeTeachingServices"/>
								<logic:equal name="teachingServiceSize" value="0">&nbsp;</logic:equal>
								<logic:notEqual name="teachingServiceSize" value="0">
									<logic:iterate id="teachingService"	name="shift" property="degreeTeachingServices" 
														  indexId="indexPercentage">						
							    		<bean:write name="teachingService" property="professorship.teacher.person.nome" />
				 						&nbsp;-&nbsp;<bean:write name="teachingService" property="percentage" />
				 						<br />
									</logic:iterate> 					
								</logic:notEqual>
							</td>						
							</tr>
						</logic:equal>
						
						<logic:greaterThan name="indexLessons" value="0">
							<tr>
								<td class="listClasses">
									<bean:write name="lesson" property="diaSemana"/>
								</td>
								<td class="listClasses">
									<dt:format patternId="hoursPattern">
										<bean:write name="lesson" property="inicio.time.time"/>
									</dt:format>
								</td>
								<td class="listClasses">
									<dt:format patternId="hoursPattern">
										<bean:write name="lesson" property="fim.time.time"/>
									</dt:format>
								</td>
								<td class="listClasses">
									<bean:write name="lesson" property="sala.nome"/>					
								</td>						
							</tr>
						</logic:greaterThan>
					</logic:iterate>
				</logic:notEqual>	
		</logic:iterate>
	</table>
	<p>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	</p>
</html:form>
