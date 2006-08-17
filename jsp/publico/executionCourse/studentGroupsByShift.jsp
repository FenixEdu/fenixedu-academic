<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2>
	<bean:message key="label.grouping"/>:
	<bean:write name="grouping" property="name"/>
</h2>

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

	<logic:notPresent name="shift">
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
	<logic:present name="shift">
		<bean:define id="shift" name="shift" type="net.sourceforge.fenixedu.domain.Shift"/>
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
           	   		<logic:notEmpty name="lesson" property="roomOccupation.room.nome">
						<bean:write name="lesson" property="roomOccupation.room.nome"/>
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
           	   		<logic:notEmpty name="lesson" property="roomOccupation.room.nome">
						<bean:write name="lesson" property="roomOccupation.room.nome"/>
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
	<logic:iterate id="studentGroup" name="studentGroups">
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