<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2>
	<bean:message key="label.shifts" />
</h2>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th rowspan="2">
			<bean:message key="property.turno"/>
		</th>
		<th colspan="4">
			<bean:message key="label.lesson"/>
		</th>
		<th rowspan="2">
			<bean:message key="label.classOrClasses"/>
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
			
	<logic:iterate id="shift" type="net.sourceforge.fenixedu.domain.Shift" name="executionCourse" property="associatedShifts" indexId="shiftIndex">
		<logic:iterate id="lesson" type="net.sourceforge.fenixedu.domain.Lesson" name="shift" property="associatedLessons" length="1" indexId="lessonIndex">
			<tr>
				<td rowspan="<%= shift.getAssociatedLessons().size() %>">
					<bean:write name="shift" property="nome"/>
				</td>
				<td >
					<bean:write name="lesson" property="diaSemana"/>
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="lesson" property="inicio.time.time"/>
					</dt:format>
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="lesson" property="fim.time.time"/>
					</dt:format>
				</td>
				<td>
				    <logic:notEmpty name="lesson" property="roomOccupation">
						<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="lesson" property="roomOccupation.room.name"/>&amp;objectCode=<bean:write name="executionCourse" property="idInternal"/>&amp;executionPeriodOID=<bean:write name="executionCourse" property="executionPeriod.idInternal"/>'>
							<bean:write name="lesson" property="roomOccupation.room.name"/>
						</a>
					</logic:notEmpty>
				</td>
				<td rowspan=<%= shift.getAssociatedLessons().size() %>>
					<logic:iterate id="schoolClass" name="shift" property="associatedClasses">
						<a href="viewClassTimeTableWithClassNameAndDegreeInitialsAction.do?classId=<bean:write name="schoolClass" property="idInternal"/>&amp;className=<bean:write name="schoolClass" property="nome"/>">
							<bean:write name="schoolClass" property="nome" /><br/>
						</a>
					</logic:iterate>
				</td>
			</tr>
		</logic:iterate>		
		<logic:iterate id="lesson" name="shift" property="associatedLessons" offset="1">
			<tr>
				<td>
					<bean:write name="lesson" property="diaSemana"/> &nbsp;
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="lesson" property="inicio.time.time"/>
					</dt:format>
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="lesson" property="fim.time.time"/>
					</dt:format>
				</td>
				<td>
				    <logic:notEmpty name="lesson" property="roomOccupation.room">
						<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="lesson" property="roomOccupation.room.name"/>&amp;objectCode=<bean:write name="executionCourse" property="idInternal"/>&amp;executionPeriodOID=<bean:write name="executionCourse" property="executionPeriod.idInternal"/>'>
							<bean:write name="lesson" property="roomOccupation.room.name"/>
						</a>
					</logic:notEmpty>
				</td>
			</tr>
		</logic:iterate>
	</logic:iterate>
</table>	
