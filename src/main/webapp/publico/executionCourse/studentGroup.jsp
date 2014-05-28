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
	<bean:write name="studentGroup" property="grouping.name"/>
</h2>

<h3>
	<bean:message key="label.group"/>:
	<bean:write name="studentGroup" property="groupNumber"/>
</h3>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th rowspan="2">
			<bean:message key="property.shift"/>
		</th>
		<th colspan="4">
			<bean:message key="label.lesson"/>
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

	<logic:notPresent name="studentGroup" property="shift">
		<tr>
			<td class="listClasses">
				<bean:message key="message.without.shift"/>
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
		</tr>
	</logic:notPresent>
	<logic:present name="studentGroup" property="shift">
		<bean:define id="shift" name="studentGroup" property="shift" type="net.sourceforge.fenixedu.domain.Shift"/>
		<tr>
			<td class="listClasses" rowspan="<%= shift.getAssociatedLessons().size() %>">
				<bean:write name="shift" property="nome"/>
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
		</tr>
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
	</logic:present>
</table>

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
</table>