<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<bean:define id="infoExecutionCourse" name="infoExecutionCourseOccupancy" property="infoExecutionCourse" />
<bean:define id="shiftsInGroups" name="infoExecutionCourseOccupancy" property="shiftsInGroups" />
<table>
	<tr>
		<td>
			<bean:message key="label.name" />
		</td>
		<td>
			<bean:write name="infoExecutionCourse" property="nome" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.students.inExecutionCourse" />
		</td>
		<td>
			<bean:write name="infoExecutionCourse" property="numberOfAttendingStudents" />
		</td>
	</tr>
</table>

<br />
<br />



	<logic:iterate id="infoShiftGroupStatistics" name="shiftsInGroups">
		<bean:define id="capacityCount" value="0"/>
		<table>
				<tr>
					<td class="listClasses-header">
						<bean:message key="label.name" />
					</td>	
		
					<td class="listClasses-header">
						<bean:message key="label.curricularCourseType" />
					</td>	
		
					<td class="listClasses-header">
						<bean:message key="listAlunos.OfTurno" />
					</td>	
		
					<td class="listClasses-header">
						<bean:message key="property.turno.capacity" />
					</td>	
		
					<td class="listClasses-header">
						<bean:message key="label.occupancy" />
					</td>	
				</tr>
		
		
			
			<logic:iterate id="shift" name="infoShiftGroupStatistics" property="shiftsInGroup" >
				<tr>
					<td class="listClasses">
						<bean:write name="shift" property="nome" />
					</td>
		
					<td class="listClasses">
						<bean:write name="shift" property="tipo" />
					</td>
		
					<td class="listClasses">
						<bean:write name="shift" property="ocupation" />
					</td>
		
					<td class="listClasses">
						<bean:write name="shift" property="lotacao" />
					</td>
		
					<td class="listClasses">
						<bean:write name="shift" property="percentage" />
					</td>
				</tr>
		
			</logic:iterate> 
			
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.total" />
				</td>
				
				<td class="listClasses-header">
					&nbsp
				</td>
			
				<td class="listClasses-header">
					<bean:write name="infoShiftGroupStatistics" property="totalNumberOfStudents"  />

				</td>
			
				<td class="listClasses-header">
					<bean:write name="infoShiftGroupStatistics" property="totalCapacity"  />
				</td>
			
				<td class="listClasses-header">
					<bean:write name="infoShiftGroupStatistics" property="totalPercentage"  />
				</td>
			
			</tr>
			
			
			
		</table>
		<br />
		<br />
	</logic:iterate>

