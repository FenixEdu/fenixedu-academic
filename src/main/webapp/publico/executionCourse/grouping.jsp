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
<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2>
	<bean:message key="label.grouping"/>:
	<bean:write name="grouping" property="name" />
</h2>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th rowspan="2">
			<bean:message key="property.shift"/>
		</th>
		<th colspan="4">
			<bean:message key="label.lesson"/>
		</th>
		<th rowspan="2">
			<bean:message key="property.groupOrGroups"/>
		</th>
	</tr>
			
	<tr>
		<th>
			<bean:message key="label.day"/>
		</th>
		<th>
			<bean:message key="property.lesson.beginning"/>
		</th>
		<th>
			<bean:message key="property.lesson.end"/>
		</th>
		<th>
			<bean:message key="property.lesson.room"/>
		</th>
	</tr>		

	<logic:iterate id="shiftStudentGroupEntry" name="grouping" property="studentGroupsIndexedByShift">
		<tr>
			<logic:empty name="shiftStudentGroupEntry" property="key">
				<td  class="listClasses">
					<bean:define id="url">/executionCourse.do?method=studentGroupsByShift&groupingID=<bean:write name="grouping" property="externalId"/></bean:define>
					<html:link page="<%= url %>"
							paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
						<bean:message key="message.without.shift"/>
					</html:link>	
				</td>
				<td class="listClasses">
					---
				</td>
				<td class="listClasses">
					---	
				</td>
				<td class="listClasses">
					---	
				</td>
               	<td class="listClasses">
					---	
				 </td>
			</logic:empty>
			<logic:notEmpty name="shiftStudentGroupEntry" property="key">
				<bean:define id="shift" type="net.sourceforge.fenixedu.domain.Shift" name="shiftStudentGroupEntry" property="key"/>
				<bean:define id="studentGroups" name="shiftStudentGroupEntry" property="value"/>
				<td  class="listClasses" rowspan="<%= shift.getAssociatedLessons().size() %>">
					<bean:define id="url">/executionCourse.do?method=studentGroupsByShift&groupingID=<bean:write name="grouping" property="externalId"/>&amp;shiftID=<bean:write name="shift" property="externalId"/></bean:define>
					<html:link page="<%= url %>"
							paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
						<bean:write name="shift" property="nome"/>
					</html:link>
				</td>
				<logic:iterate id="lesson" name="shift" property="associatedLessons" length="1">
					<td class="listClasses">
						<bean:write name="lesson" property="diaSemana"/> &nbsp;
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="inicio.time.time"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="fim.time.time"/>
						</dt:format>
					</td>
               		<td class="listClasses">               			
            	   		<logic:notEmpty name="lesson" property="roomOccupation">
							<bean:write name="lesson" property="roomOccupation.room.name"/>
						</logic:notEmpty>	
			 		</td>
				</logic:iterate>
			</logic:notEmpty>
	 		<td class="listClasses">
				<logic:iterate id="studentGroup" name="studentGroups">
					<bean:define id="urlGroup">/executionCourse.do?method=studentGroup&studentGroupID=<bean:write name="studentGroup" property="externalId"/></bean:define>
					<html:link page="<%= urlGroup %>"
							paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
						<bean:write name="studentGroup" property="groupNumber"/>
					</html:link>
				</logic:iterate>
			</td>
	 	</tr>
		<logic:notEmpty name="shiftStudentGroupEntry" property="key">
			<bean:define id="shift" type="net.sourceforge.fenixedu.domain.Shift" name="shiftStudentGroupEntry" property="key"/>
			<bean:define id="studentGroups" name="shiftStudentGroupEntry" property="value"/>
			<logic:iterate id="lesson" name="shift" property="associatedLessons" offset="1">
				<tr>
					<td class="listClasses">
						<bean:write name="lesson" property="diaSemana"/> &nbsp;
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="inicio.time.time"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="fim.time.time"/>
						</dt:format>
					</td>
               		<td class="listClasses">
            	   		<logic:notEmpty name="lesson" property="roomOccupation">
							<bean:write name="lesson" property="roomOccupation.room.name"/>
						</logic:notEmpty>	
			 		</td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>
	</logic:iterate>
</table>

<br/>

<h3>
	<bean:message key="label.students.in.grouping"/>
</h3>
<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th>
			<bean:message key="label.student.group.number"/>
		</th>
		<th>
			<bean:message key="label.student.number"/>
		</th>
		<th>
			<bean:message key="label.name"/>
		</th>
	</tr>
	<logic:iterate id="studentGroup" name="grouping" property="studentGroupsOrderedByGroupNumber">
		<logic:iterate id="attend" name="studentGroup" property="attends">
			<bean:define id="student" name="attend" property="aluno"/>
			<tr>
				<td>
					<bean:write name="studentGroup" property="groupNumber"/>
				</td>
				<td>
					<bean:write name="student" property="number"/>
				</td>
				<td>
					<bean:write name="student" property="person.name"/>
				</td>
			</tr>
		</logic:iterate>
	</logic:iterate>
</table>