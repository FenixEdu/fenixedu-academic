<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="hoursPattern">HH : mm</bean:define>

<bean:define id="infoShiftsPercentageList" name="teacherExecutionCourseProfessorshipShifts" property="infoShiftPercentageList" scope="request" />
<bean:define id="infoTeacher" name="teacherExecutionCourseProfessorshipShifts" property="infoTeacher" scope="request" />
<bean:define id="infoExecutionCourse" name="teacherExecutionCourseProfessorshipShifts" property="infoExecutionCourse" scope="request" />
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" /></b> <bean:write name="infoExecutionCourse" property="nome"/>
</p>

<html:errors/>

<html:form action="/manageTeacherShiftProfessorships">
	<html:hidden property="teacherNumber" value="<%= teacherNumber.toString() %>"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="executionCourseId"/>
	<html:hidden property="method" value="processForm"/>

	<table width="100%" cellspacing="1" cellpadding="5">
		<%-- ********************************* HEADER *********************************************** --%>
		<tr>
			<td rowspan="2" class="listClasses-header" width="10%"><bean:message key="label.shift"/></td>
			<td rowspan="2" class="listClasses-header" width="5%"><bean:message key="label.shift.type"/></td>
			<td colspan="4" class="listClasses-header" width="40%"><bean:message key="label.lessons"/></td>
<%--			<td rowspan="2" class="listClasses-header"><bean:message key="label.professorship.question"/></td>			 --%>
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
		<logic:iterate id="infoShiftPercentage" name="infoShiftsPercentageList" indexId="index">
			<bean:define id="availablePercentage" name="infoShiftPercentage" property="availablePercentage" />
			<bean:size id="lessonsSize" name="infoShiftPercentage" property="infoLessons" />	

				<logic:equal name="lessonsSize" value="0">
					<tr>
						<td class="listClasses"><bean:write name="infoShiftPercentage" property="shift.nome"/></td>
						<td class="listClasses"><bean:write name="infoShiftPercentage" property="shift.tipo.siglaTipoAula"/></td>
						<td class="listClasses" colspan="7"> Não tem aulas </td>
					</tr>
				</logic:equal>

				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="infoLesson" name="infoShiftPercentage" property="infoLessons" indexId="indexLessons" >
			            <logic:equal name="indexLessons" value="0">

							<tr>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="infoShiftPercentage" property="shift.nome"/></td>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="infoShiftPercentage" property="shift.tipo.siglaTipoAula"/></td>
							<td class="listClasses">
								<bean:write name="infoLesson" property="diaSemana"/>
							</td>
							<td class="listClasses">
								<dt:format patternId="hoursPattern">
									<bean:write name="infoLesson" property="inicio.time.time"/>
								</dt:format>
							</td>
							<td class="listClasses">

								<dt:format patternId="hoursPattern">
									<bean:write name="infoLesson" property="fim.time.time"/>
								</dt:format>
							</td>
							<td class="listClasses">
								<bean:write name="infoLesson" property="infoSala.nome"/>					
							</td>
							<td class="listClasses" rowspan="<%= lessonsSize %>">
								<logic:greaterThan name="infoShiftPercentage" property="availablePercentage" value="0">
									<bean:define id="propertyName">
										teacherPercentageMap(<bean:write name="infoShiftPercentage" property="shift.idInternal"/>)
									</bean:define>
									<html:text property='<%= propertyName %>' size="4" /> %
								</logic:greaterThan>
								<logic:equal name="infoShiftPercentage" property="availablePercentage" value="0">
									&nbsp;
								</logic:equal>
							</td>
							<td class="listClasses" rowspan="<%= lessonsSize %>">
								<bean:size id="shiftPercentageSize" name="infoShiftPercentage" property="infoShiftProfessorshipList"/>
								<logic:equal name="shiftPercentageSize" value="0">&nbsp;</logic:equal>
								<logic:notEqual name="shiftPercentageSize" value="0">
									<logic:iterate id="teacherShiftPercentage"	name="infoShiftPercentage" property="infoShiftProfessorshipList" 
														  indexId="indexPercentage">						
							    		<bean:write name="teacherShiftPercentage" property="infoProfessorship.infoTeacher.infoPerson.nome" />
				 						&nbsp;-&nbsp;<bean:write name="teacherShiftPercentage" property="percentage" />
				 						<br />
									</logic:iterate> 					
								</logic:notEqual>
							</td>						
							</tr>
						</logic:equal>
						
						<logic:greaterThan name="indexLessons" value="0">
							<tr>
								<td class="listClasses">
									<bean:write name="infoLesson" property="diaSemana"/>
								</td>
								<td class="listClasses">
									<dt:format patternId="hoursPattern">
										<bean:write name="infoLesson" property="inicio.time.time"/>
									</dt:format>
								</td>
								<td class="listClasses">
									<dt:format patternId="hoursPattern">
										<bean:write name="infoLesson" property="fim.time.time"/>
									</dt:format>
								</td>
								<td class="listClasses">
									<bean:write name="infoLesson" property="infoSala.nome"/>					
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
