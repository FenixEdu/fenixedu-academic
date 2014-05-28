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

<h2><bean:message key="property.shift.ocupation"/></h2>

<bean:define id="infoExecutionCourse" name="infoExecutionCourseOccupancy" property="infoExecutionCourse" />
<bean:define id="shiftsInGroups" name="infoExecutionCourseOccupancy" property="shiftsInGroups" />

<p class="mvert025"><strong><bean:message key="label.name" />:</strong> <bean:write name="infoExecutionCourse" property="nome" /></p>
<p class="mvert025"><strong><bean:message key="label.students.inExecutionCourse" />:</strong> <bean:write name="infoExecutionCourse" property="numberOfAttendingStudents" /></p>

</table>

<logic:iterate id="infoShiftGroupStatistics" name="shiftsInGroups">
	<bean:define id="capacityCount" value="0"/>
	<table class="tstyle1 tdcenter width100 mtop15">
			<tr>
				<th style="width: 15%;">
					<bean:message key="label.name" />
				</th>	
	
				<th style="width: 15%;">
					<bean:message key="label.curricularCourseType" />
				</th>	
	
				<th>
					<bean:message key="listAlunos.OfTurno" />
				</th>	
	
				<th style="width: 15%;">
					<bean:message key="property.turno.capacity" />
				</th>	
	
				<th style="width: 15%;">
					<bean:message key="label.occupancy" />
				</th>	
			</tr>
		<logic:iterate id="shift" name="infoShiftGroupStatistics" property="shiftsInGroup" >
			<tr>
				<td>
					<bean:write name="shift" property="nome" />
				</td>
	
				<td>
					<bean:write name="shift" property="shiftTypesPrettyPrint" />
				</td>
	
				<td>
					<bean:write name="shift" property="ocupation" />
				</td>
	
				<td>
					<bean:write name="shift" property="lotacao" />
				</td>
	
				<td>
					<logic:equal name="shift" property="lotacao" value="0">
						<em><bean:message key="not.applicable"/></em>
					</logic:equal>
					<logic:notEqual name="shift" property="lotacao" value="0">
						<bean:write name="shift" property="percentage" />
					</logic:notEqual>
				</td>
			</tr>
		</logic:iterate> 		
		<tr>
			<th>
				<bean:message key="label.total" />
			</th>
			
			<th>
				&nbsp
			</th>
		
			<th>
				<bean:write name="infoShiftGroupStatistics" property="totalNumberOfStudents"  />

			</th>
		
			<th>
				<bean:write name="infoShiftGroupStatistics" property="totalCapacity"  />
			</th>
		
			<th>
				<logic:equal name="infoShiftGroupStatistics" property="totalCapacity" value="0">
					<em><bean:message key="not.applicable"/></em>
				</logic:equal>
				<logic:notEqual name="infoShiftGroupStatistics" property="totalCapacity" value="0">
					<bean:write name="infoShiftGroupStatistics" property="totalPercentage"  />
				</logic:notEqual>
			</th>		
		</tr>
	</table>
</logic:iterate>

