<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern" value="HH : mm"/>
<bean:define id="datePattern" value="dd-MM-yyyy"/>

<div id="print1" class="sheet sans">

<h1 class="caps center"><bean:message key="label.schedule" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> do <bean:write name="executionPeriod" property="semester"/><bean:message key="label.number.super.identification" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> <bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> <bean:write name="executionPeriod" property="executionYear.year"/></h1>

<table>
<logic:notEmpty name="legalRegimen">
	<tr><td><strong><bean:message key="label.name" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong> <bean:write name="legalRegimen" property="teacher.person.name"/></td><td><strong><bean:message key="label.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong> <bean:write name="legalRegimen" property="teacher.teacherNumber"/></td></tr>
	<tr><td><strong><bean:message key="label.category" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong> <bean:write name="legalRegimen" property="category.code"/></td><logic:notEmpty name="workingUnit"><td><strong><bean:message key="label.section" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong> <bean:write name="workingUnit" property="name"/></td></tr>
	<tr><td><strong><bean:message key="label.department" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong> <logic:notEmpty name="departmentRealName"> <bean:write name="departmentRealName"/> </logic:notEmpty> </td><td><strong><bean:message key="label.costCenter" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong> <bean:write name="workingUnit" property="costCenterCode"/></td></tr></logic:notEmpty>
</logic:notEmpty>
</table>

<h3><bean:message key="label.degree.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:notEmpty name="professorshipDTOs">
	<logic:iterate id="professorshipDTO" name="professorshipDTOs" indexId="indexProfessorship">
		<bean:define id="professorship" name="professorshipDTO" property="professorship"/>
		<p class="mbottom05"><strong>					
			<bean:write name="professorship" property="executionCourse.nome"/>				
			<bean:size id="degreeSiglasSizeList" name="professorshipDTO" property="degreeSiglas"/>
			(<logic:iterate id="sigla" name="professorshipDTO" property="degreeSiglas" indexId="index">
				<bean:write name="sigla" /> 
				<logic:notEqual name="degreeSiglasSizeList" value="<%= String.valueOf(index.intValue() + 1) %>">
				,
				</logic:notEqual>
			</logic:iterate>)
		</strong></p>
		
		<bean:define id="professorshipID" name="professorship" property="idInternal"/>
		<bean:define id="degreeTeachingServices" name="professorship" property="degreeTeachingServicesOrderedByShift" type="java.util.SortedSet"/>		
						
		<logic:notEmpty name="degreeTeachingServices">			
			
			<% 
			   int degreeTeachingServicesLength = 0; 
			   if(degreeTeachingServices.size() % 2 == 0){
			       degreeTeachingServicesLength = degreeTeachingServices.size() / 2;
			   } else {
			       degreeTeachingServicesLength = Math.round((degreeTeachingServices.size() / 2)) + 1;
			   }			
			%>
											
			<div style="width: 49%; float: left;">
				<table class="tb01">
					<%-- <logic:equal name="indexProfessorship" value="0"> --%>
						<tr><th>Turno</th><th style="width: 3em;"><bean:message key="label.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.schedule" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 7em;"><bean:message key="label.professorship" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> (%)</th></tr>					
					<%-- </logic:equal> --%>
					<logic:iterate id="degreeTeachingService" name="degreeTeachingServices" length="<%= String.valueOf(degreeTeachingServicesLength) %>">						
						<bean:define id="lessonList" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime"/>											
						<tr>
							<td><bean:write name="degreeTeachingService" property="shift.nome"/></td>
							<td><bean:message name="degreeTeachingService" property="shift.tipo.name" bundle="ENUMERATION_RESOURCES"/></td>
							
							<td>
								<logic:iterate id="lesson" name="lessonList" indexId="index">
									<bean:write name="lesson" property="diaSemana"/> 						
									(<dt:format patternId="hoursPattern">
										<bean:write name="lesson" property="inicio.timeInMillis"/>
								      </dt:format> 
									<bean:message key="label.time.separator" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>  <dt:format patternId="hoursPattern">
									      <bean:write name="lesson" property="fim.timeInMillis"/>
									   </dt:format> , <bean:message key="label.room" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> <bean:write name="lesson" property="sala.nome"/>)
								      ,
							    </logic:iterate>
						    </td>						    						    
						    <td>
								<bean:define id="teachingServicePercentage" name="degreeTeachingService" property="percentage"/>
								<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>%	
							</td>
						</tr>
					
					</logic:iterate>
				</table>
			</div>			
			<% if(degreeTeachingServicesLength != degreeTeachingServices.size()){ %>
				<div style="width: 49%; float: left;">
					<table class="tb01" style="margin-left: 2%;">
						<%-- <logic:equal name="indexProfessorship" value="0"> --%>
							<tr><th>Turno</th><th style="width: 3em;"><bean:message key="label.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.schedule" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 7em;"><bean:message key="label.professorship" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> (%)</th></tr>					
						<%--  </logic:equal> --%>
						<logic:iterate id="degreeTeachingService" name="degreeTeachingServices" offset="<%= String.valueOf(degreeTeachingServicesLength) %>">							
							<bean:define id="lessonList" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime"/>												
							<tr>
								<td><bean:write name="degreeTeachingService" property="shift.nome"/></td>
								<td><bean:message name="degreeTeachingService" property="shift.tipo.name" bundle="ENUMERATION_RESOURCES"/></td>
								
								<td>
									<logic:iterate id="lesson" name="lessonList" indexId="index">
										<bean:write name="lesson" property="diaSemana"/> 						
										(<dt:format patternId="hoursPattern">
											<bean:write name="lesson" property="inicio.timeInMillis"/>
									      </dt:format> 
										<bean:message key="label.time.separator" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>  <dt:format patternId="hoursPattern">
										      <bean:write name="lesson" property="fim.timeInMillis"/>
										   </dt:format> ,<bean:message key="label.room" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> <bean:write name="lesson" property="sala.nome"/>)
									      ,
								    </logic:iterate>
							    </td>							    						    
							    <td>
								    <bean:define id="teachingServicePercentage" name="degreeTeachingService" property="percentage"/>
									<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>%							    							    
							    </td>
							</tr>						
						</logic:iterate>
					</table>
				</div>
			<% } %>			
		</logic:notEmpty>
		
		<logic:empty name="degreeTeachingServices">
			<div style="width: 49%; float: left;">
				<table class="tb01">
					<tr><th style="width: 3em;"><bean:message key="label.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.schedule" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 5em;"><bean:message key="label.room" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 7em;"><bean:message key="label.professorship" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> (%)</th></tr>
					<tr class="height4"><td></td><td></td><td></td><td></td></tr>
				</table>
			</div>
	
			<div style="width: 49%; float: left;">
				<table class="tb01" style="margin-left: 2%;">
					<tr><th style="width: 3em;"><bean:message key="label.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.schedule" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 5em;"><bean:message key="label.room" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 7em;"><bean:message key="label.professorship" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> (%)</th></tr>
					<tr class="height4"><td></td><td></td><td></td><td></td></tr>
				</table>
			</div>		
		</logic:empty>

		<br style="clear: both;"/>

		<bean:define id="supportLessonList" name="professorship" property="supportLessonsOrderedByStartTimeAndWeekDay" type="java.util.TreeSet"/>
		<logic:notEmpty name="supportLessonList">		
			<br style="clear: both;"/>

			<% 
			   int supportLessonsLength = 0; 
			   if(supportLessonList.size() % 2 == 0){
			       supportLessonsLength = supportLessonList.size() / 2;
			   } else {
			       supportLessonsLength = Math.round((supportLessonList.size() / 2)) + 1;
			   }			
			%>
		
			<div style="width: 49%; float: left;">
				<table class="tb01">
					<tr><th style="width: 4em;"><bean:message key="label.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.weekDay" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.schedule" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.place" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>
					<logic:iterate id="supportLesson" name="supportLessonList" length="<%= String.valueOf(supportLessonsLength) %>">
						<tr>
							<td><bean:message key="label.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
							<td style="text-align: center;"><bean:write name="supportLesson" property="weekDay"/></td>
							<td style="text-align: center;">
							  	<dt:format patternId="hoursPattern">
									<bean:write name="supportLesson" property="startTime.time"/>
								</dt:format>
								<bean:message key="label.time.separator" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>  
								<dt:format patternId="hoursPattern">
									<bean:write name="supportLesson" property="endTime.time"/>
								</dt:format>
							</td>
							<td style="text-align: center;"><bean:write name="supportLesson" property="place"/></td>
						</tr>
					</logic:iterate>
				</table>
			</div>

			<% if(supportLessonsLength != supportLessonList.size()){ %>		
				<div style="width: 49%; float: left;">
					<table class="tb01" style="margin-left: 2%;">
						<tr><th style="width: 4em;"><bean:message key="label.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.weekDay" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.schedule" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.place" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>
						<logic:iterate id="supportLesson" name="supportLessonList" offset="<%= String.valueOf(supportLessonsLength) %>">
							<tr>
								<td><bean:message key="label.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
								<td style="text-align: center;"><bean:write name="supportLesson" property="weekDay"/></td>
								<td style="text-align: center;">
								  	<dt:format patternId="hoursPattern">
										<bean:write name="supportLesson" property="startTime.time"/>
									</dt:format>
									<bean:message key="label.time.separator" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>  
									<bean:message key="label.time.separator" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> 
									<dt:format patternId="hoursPattern">
										<bean:write name="supportLesson" property="endTime.time"/>
									</dt:format>
								</td>
								<td style="text-align: center;"><bean:write name="supportLesson" property="place"/></td>
							</tr>
						</logic:iterate>					
					</table>
				</div>		
			<% } %>							
		</logic:notEmpty>		
		<br style="clear: both;"/>
	</logic:iterate>
</logic:notEmpty>			


<h3 class="mbottom05"><bean:message key="label.master.degree.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:notEmpty name="masterDegreeServices">
	<div style="width: 49%; float: left;">
		<table class="tb01">	
			<tr><th><bean:message key="label.course" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 5em;"><bean:message key="label.hours" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 5em;"><bean:message key="label.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>
			<logic:iterate id="masterDegreeService" name="masterDegreeServices">
				<tr>
				  <td> 
				  	   <bean:write name="masterDegreeService" property="professorship.executionCourse.nome"/> 
		   	  		   <logic:iterate id="curricularCourse" name="masterDegreeService" property="professorship.executionCourse.associatedCurricularCourses">
							(<bean:write name="curricularCourse" property="degreeCurricularPlan.name"/>)
					   </logic:iterate>
				  </td>
				  <td>
				  	<logic:empty name="masterDegreeService" property="hours">
						0
					</logic:empty>
					<logic:notEmpty name="masterDegreeService" property="hours">
						<bean:write name="masterDegreeService" property="hours"/>
					</logic:notEmpty></td>
				  <td>
				  	<logic:empty name="masterDegreeService" property="credits">
						0
					</logic:empty>
					<logic:notEmpty name="masterDegreeService" property="credits">
						<bean:write name="masterDegreeService" property="credits"/>
					</logic:notEmpty>
				  </td>
				</tr>
			</logic:iterate>
		</table>
	</div>
	<br style="clear: both;"/>
</logic:notEmpty>

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="masterDegreeTeachingNotes">
		<table class="tb01" style="width: 99%;">
			<tr><th style="width: 20em;text-align: center;"><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>		
			<tr class="height2">
				<td>					    
				   	<bean:define id="masterDegreeTeachingNotesAux" name="teacherServiceNotes" property="masterDegreeTeachingNotes" />
					<%= masterDegreeTeachingNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>			
				</td>
			</tr>
		</table>	
	</logic:notEmpty>
</logic:notEmpty>
<br style="clear: both;"/>


<h3 class="mbottom05"><bean:message key="label.advises" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:notEmpty name="adviseServices">

	<bean:define id="adviseServicesBean" name="adviseServices" type="java.util.List"/>
	<% 
	   int advisesLength = 0; 
	   if(adviseServicesBean.size() % 2 == 0){
	       advisesLength = adviseServicesBean.size() / 2;
	   } else {
	       advisesLength = Math.round((adviseServicesBean.size() / 2)) + 1;
	   }			
	%>
	
	<p class="mbottom05">Alunos:</p>		
	<div style="width: 49%; float: left;">
		<table class="tb01">
			<tr><th style="width: 5em;"><bean:message key="label.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.name" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 7em;"><bean:message key="label.professorship" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> (%)</th></tr>					
			<logic:iterate id="adviseService" name="adviseServicesBean" length="<%= String.valueOf(advisesLength) %>">
			<tr> 
				<td><bean:write name="adviseService" property="advise.student.number"/></td>
				<td><bean:write name="adviseService" property="advise.student.person.name"/></td>
				<td><bean:write name="adviseService" property="percentage"/>%</td>
			</tr>			
			</logic:iterate>
		</table>
	</div>
	
	<% if(advisesLength != adviseServicesBean.size()){ %>
	<div style="width: 49%; float: left;">
		<table class="tb01" style="margin-left: 2%;">
			<tr><th style="width: 5em;"><bean:message key="label.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.name" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 7em;"><bean:message key="label.professorship" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> (%)</th></tr>					
			<logic:iterate id="adviseService" name="adviseServicesBean" offset="<%= String.valueOf(advisesLength) %>">
			<tr> 
				<td><bean:write name="adviseService" property="advise.student.number"/></td>
				<td><bean:write name="adviseService" property="advise.student.person.name"/></td>
				<td><bean:write name="adviseService" property="percentage"/>%</td>
			</tr>			
			</logic:iterate>
		</table>
	</div>
	<% } %>
</logic:notEmpty>
<br style="clear: both;"/>

<h3 class="mbottom05"><bean:message key="label.institution.working.time"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:present name="institutionWorkTimeList">
	<table class="tb01" style="width: 99%;">		
		<tr>
			<logic:iterate id="weekDay" name="weekDays">
				<th style="width: 16.5em;"><bean:message name="weekDay" property="name" bundle="ENUMERATION_RESOURCES"/></th>
			</logic:iterate>
		</tr>			
		<tr>
			<logic:iterate id="weekDay" name="weekDays">
				<bean:define id="weekDayName" name="weekDay" property="name" type="java.lang.String" />
				<td style="text-align: center;">
					<logic:iterate id="institutionWorkTime" name="institutionWorkTimeList">					  	
						<logic:equal name="institutionWorkTime" property="weekDay.name" value="<%= weekDayName %>">
			 				<dt:format patternId="hoursPattern">
			 					<bean:write name="institutionWorkTime" property="startTime.time"/>
			 				</dt:format>
			 				<bean:message key="label.time.separator" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> 
			 				<dt:format patternId="hoursPattern">
								<bean:write name="institutionWorkTime" property="endTime.time"/>
							</dt:format>
							<br/>		
						</logic:equal>					
					</logic:iterate>
				</td>					
			</logic:iterate>
		 </tr>	
	</table>
</logic:present>
<br style="clear: both;"/>

<h3 class="mbottom05"><bean:message key="label.functionsAccumulation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="functionsAccumulation">
		<table class="tb01" style="width: 99%;">
			<tr><th style="width: 20em;text-align: center;"><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>		
			<tr class="height2">
				<td>				     
					<bean:define id="functionsAccumulationAux" name="teacherServiceNotes" property="functionsAccumulation" />
					<%= functionsAccumulationAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>
<br style="clear: both;"/>


<h3 class="mbottom05"><bean:message key="label.others" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:notEmpty name="otherServices">
	<div style="width: 49%; float: left;">
		<table class="tb01">
			<tr><th><bean:message key="label.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 5em;"><bean:message key="label.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>
			<logic:iterate id="otherService" name="otherServices" >
				<tr>
					<td><bean:write name="otherService" property="reason"/></td>
					<td><bean:write name="otherService" property="credits"/></td>
				</tr>
			</logic:iterate>
		</table>
	</div>
	<br style="clear: both;"/>
	<logic:notEmpty name="teacherServiceNotes">
		<logic:notEmpty name="teacherServiceNotes" property="othersNotes">
			<br/>
		</logic:notEmpty>
	</logic:notEmpty>
</logic:notEmpty>

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="othersNotes">
		<table class="tb01" style="width: 99%;">
			<tr><th style="width: 20em;text-align: center;"><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>		
			<tr class="height2">
				<td>				     
					<bean:define id="othersNotesAux" name="teacherServiceNotes" property="othersNotes" />
					<%= othersNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>
<br style="clear: both;"/>


<h3 class="mbottom05"><bean:message key="label.managent.functions" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:notEmpty name="personFunctions">
	<table class="tb01" style="width: 99%;">
		<tr><th>Cargo</th><th><bean:message key="label.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 15em;"><bean:message key="label.period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 5em;"><bean:message key="label.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>
		<logic:iterate id="personFunction" name="personFunctions" >
			<tr class="height2">
				<td><bean:write name="personFunction" property="function.name"/></td>
				<td>
					<bean:define id="unit" name="personFunction" property="function.unit"/>
					<bean:write name="unit" property="presentationNameWithParents"/>																								
				</td>
				<td style="text-align: center;">
					De 
					<dt:format patternId="datePattern">
						<bean:write name="personFunction" property="beginDateInDateType.time"/>
					</dt:format> 
					a 
					<logic:notEmpty name="personFunction" property="endDate">
						<dt:format patternId="datePattern">
								<bean:write name="personFunction" property="endDateInDateType.time"/>
						</dt:format>
					</logic:notEmpty>
					<logic:empty name="personFunction" property="endDate">
					-
					</logic:empty>
				</td>
				<td style="text-align: center;"><bean:write name="personFunction" property="credits"/></td>
			</tr>
		</logic:iterate>
	</table>
	<br style="clear: both;"/>
</logic:notEmpty>

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="managementFunctionNotes">
		<table class="tb01" style="width: 99%;">
			<tr><th style="width: 20em;text-align: center;"><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>		
			<tr class="height2">
				<td>
					<bean:define id="managementFunctionNotesAux" name="teacherServiceNotes" property="managementFunctionNotes"/>
					<%= managementFunctionNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>				
				</td>
			</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>
<br style="clear: both;"/>



<h3 class="mbottom05"><bean:message key="label.service.exemptions" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:notEmpty name="serviceExemptions">
	<table class="tb01" style="width: 99%;">
		<tr><th style="width: 20em;"><bean:message key="label.situation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th><bean:message key="label.organization" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th><th style="width: 15em;"><bean:message key="label.period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>
		<logic:iterate id="serviceExemption" name="serviceExemptions" >
			<tr>
				<td><bean:message name="serviceExemption" property="type.name" bundle="ENUMERATION_RESOURCES"/></td>
				<td>
					<logic:notEmpty name="serviceExemption" property="institution">
						<bean:write name="serviceExemption" property="institution"/>
					</logic:notEmpty>
					<logic:empty name="serviceExemption" property="institution">
						-
					</logic:empty>
				</td>
				<td>
					De 
					<dt:format patternId="datePattern">
						<bean:write name="serviceExemption" property="start.time"/>
					</dt:format>
					a 
					<logic:notEmpty name="serviceExemption" property="end">
						<dt:format patternId="datePattern">
							<bean:write name="serviceExemption" property="end.time"/>
						</dt:format>
					</logic:notEmpty>
					<logic:empty name="serviceExemption" property="end">
					-
					</logic:empty>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br style="clear: both;"/>
</logic:notEmpty>

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="serviceExemptionNotes">
		<table class="tb01" style="width: 99%;">
			<tr><th style="width: 20em;text-align: center;"><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th></tr>		
			<tr class="height2">
				<td>
					<bean:define id="serviceExemptionNotesAux" name="teacherServiceNotes" property="serviceExemptionNotes"/>
					<%= serviceExemptionNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>
<br style="clear: both;"/>
<br/>

<p><bean:message key="label.date" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>: __ / __ / __</p>

<br/>

<table style="width: 100%; text-align: center;">
<tr><td><bean:message key="label.schedules.teacher" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td><td><bean:message key="label.schedules.section.coordinator" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td><td><bean:message key="label.schedules.department.president" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td></tr>
</table>

</div>
