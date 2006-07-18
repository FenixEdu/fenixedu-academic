<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>


<bean:define id="infoExecutionCourse" name="infoExecutionCourseOccupancy" property="infoExecutionCourse" />
<bean:define id="shiftsInGroups" name="infoExecutionCourseOccupancy" property="shiftsInGroups" />
<table class="infoop">
	<tr>
		<td><strong><bean:message key="label.name" />:</strong></td>
		<td><bean:write name="infoExecutionCourse" property="nome" /></td>
	</tr>
	<tr>
		<td><strong><bean:message key="label.students.inExecutionCourse" />:</strong></td>
		<td><bean:write name="infoExecutionCourse" property="numberOfAttendingStudents" /></td>
	</tr>
</table>
<br />
<br />
<logic:iterate id="infoShiftGroupStatistics" name="shiftsInGroups">
	<bean:define id="capacityCount" value="0"/>
	<table>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.name" />
				</th>	
	
				<th class="listClasses-header">
					<bean:message key="label.curricularCourseType" />
				</th>	
	
				<th class="listClasses-header">
					<bean:message key="listAlunos.OfTurno" />
				</th>	
	
				<th class="listClasses-header">
					<bean:message key="property.turno.capacity" />
				</th>	
	
				<th class="listClasses-header">
					<bean:message key="label.occupancy" />
				</th>	
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
			<th class="listClasses-header">
				<bean:message key="label.total" />
			</th>
			
			<th class="listClasses-header">
				&nbsp
			</th>
		
			<th class="listClasses-header">
				<bean:write name="infoShiftGroupStatistics" property="totalNumberOfStudents"  />

			</th>
		
			<th class="listClasses-header">
				<bean:write name="infoShiftGroupStatistics" property="totalCapacity"  />
			</th>
		
			<th class="listClasses-header">
				<bean:write name="infoShiftGroupStatistics" property="totalPercentage"  />
			</th>		
		</tr>
	</table>
	<br />
	<br />
</logic:iterate>

