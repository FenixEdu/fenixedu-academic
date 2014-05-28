<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

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
					<bean:write name="shift" property="shiftTypesPrettyPrint" />
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

