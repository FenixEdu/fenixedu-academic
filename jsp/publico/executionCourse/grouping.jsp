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
					<bean:define id="url">/executionCourse.do?method=studentGroupsByShift&groupingID=<bean:write name="grouping" property="idInternal"/></bean:define>
					<html:link page="<%= url %>"
							paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
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
					<bean:define id="url">/executionCourse.do?method=studentGroupsByShift&groupingID=<bean:write name="grouping" property="idInternal"/>&amp;shiftID=<bean:write name="shift" property="idInternal"/></bean:define>
					<html:link page="<%= url %>"
							paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
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
            	   		<logic:notEmpty name="lesson" property="roomOccupation.room.nome">
							<bean:write name="lesson" property="roomOccupation.room.nome"/>
						</logic:notEmpty>	
			 		</td>
				</logic:iterate>
			</logic:notEmpty>
	 		<td class="listClasses">
				<logic:iterate id="studentGroup" name="studentGroups">
					<bean:define id="urlGroup">/executionCourse.do?method=studentGroup&studentGroupID=<bean:write name="studentGroup" property="idInternal"/></bean:define>
					<html:link page="<%= urlGroup %>"
							paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
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
            	   		<logic:notEmpty name="lesson" property="roomOccupation.room.nome">
							<bean:write name="lesson" property="roomOccupation.room.nome"/>
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