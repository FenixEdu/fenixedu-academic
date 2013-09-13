<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<html:xhtml/>

<h2>
	<bean:message key="label.shifts" />
</h2>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th rowspan="2">
			<bean:message key="property.turno"/>
		</th>
		<th colspan="5">
			<bean:message key="label.lesson"/>
		</th>
		<th rowspan="2">
			<bean:message key="label.classOrClasses"/>
		</th>
	</tr>
	<tr>
		<th>
			<bean:message key="label.weeks"/>
		</th>
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
					<bean:write name="lesson" property="occurrenceWeeksAsString" />
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
				    <logic:notEmpty name="lesson" property="sala">
						<bean:define id="url"><%= request.getContextPath() %>/publico/siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="lesson" property="sala.name"/>&amp;objectCode=<bean:write name="executionCourse" property="externalId"/>&amp;executionPeriodOID=<bean:write name="executionCourse" property="executionPeriod.externalId"/></bean:define>
							<a href="<%= url %>"><bean:write name="lesson" property="sala.name"/></a>
					</logic:notEmpty>
				</td>
				<td rowspan=<%= shift.getAssociatedLessons().size() %>>
					<logic:iterate id="schoolClass" name="shift" property="associatedClasses">
						<bean:define id="url"><%= request.getContextPath() %>/publico/viewClassTimeTableWithClassNameAndDegreeInitialsAction.do?classId=<bean:write name="schoolClass" property="externalId"/>&amp;className=<bean:write name="schoolClass" property="nome"/></bean:define>						
							<a href="<%= url %>"><bean:write name="schoolClass" property="nome" /><br/></a>
					</logic:iterate>
				</td>
			</tr>
		</logic:iterate>		
		<logic:iterate id="lesson" name="shift" property="associatedLessons" offset="1">
			<tr>
				<td>
					<bean:write name="lesson" property="occurrenceWeeksAsString" />
				</td>
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
				    <logic:notEmpty name="lesson" property="roomOccupation">
				    	<bean:define id="url"><%= request.getContextPath() %>/publico/siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="lesson" property="roomOccupation.room.name"/>&amp;objectCode=<bean:write name="executionCourse" property="externalId"/>&amp;executionPeriodOID=<bean:write name="executionCourse" property="executionPeriod.externalId"/></bean:define>
						<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><a href="<%= url %>"><bean:write name="lesson" property="roomOccupation.room.name"/></a>
					</logic:notEmpty>
				</td>
			</tr>
		</logic:iterate>
	</logic:iterate>
</table>	
