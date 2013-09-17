<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml/>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<div style="font-family: Verdana, sans-serif; font-size: 80%;">
<logic:present name="executionCoursesByCurricularYearByExecutionDegree">

	<logic:iterate id="executionCoursesByCurricularYearByExecutionDegreeEntry" name="executionCoursesByCurricularYearByExecutionDegree">
		<bean:size id="length" name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="value"/>
		<logic:iterate id="executionCoursesByCurricularYearEntry" indexId="i" name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="value">
			<h2><bean:message key="title.written.evaluations.by.degree.and.curricular.year"/></h2>
			<strong>
				<bean:write name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="key.degreeCurricularPlan.degree.nome"/>
				<br/>
				<bean:define id="year" type="java.lang.Integer" name="executionCoursesByCurricularYearEntry" property="key"/> <bean:message key="label.year" arg0="<%= year.toString() %>"/>
				-
                <bean:write name="academicInterval" property="pathName" />
               
                <bean:define id="semester" type="java.lang.Integer" name="semester" />

				<bean:define id="degreeCurricularPlan" name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="key.degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"/>
			</strong>
			<br/>
			<bean:define id="iS"><%= i + 1 %></bean:define>
			<bean:define id="lS"><%= length %></bean:define>
			<logic:equal name="iS" value="<%= lS %>">
				<table border='1' cellspacing='0' cellpadding='3' width='95%' class="td01 break-after">
			</logic:equal>
			<logic:notEqual name="iS" value="<%= lS %>">
				<table border='1' cellspacing='0' cellpadding='3' width='95%' class="td01 break-after">
			</logic:notEqual>
				<tr>
					<td>
						<bean:message key="label.Degree"/>
					</td>
					<td>
						<bean:message key="lable.season"/>
					</td>
					<td>
						<bean:message key="label.date"/>
					</td>
					<td>
						<bean:message key="link.public.home"/>
					</td>
					<td>
						<bean:message key="property.exam.end"/>
					</td>
					<td>
						<bean:message key="lable.rooms"/>
					</td>
				</tr>
				<bean:define id="formBean" name="searchWrittenEvaluationsByDegreeAndYearForm"/>
				<bean:define id="evaluationType" type="java.lang.String" name="formBean" property="evaluationType"/>
				<logic:iterate id="executionCourse" name="executionCoursesByCurricularYearEntry" property="value">
					<logic:iterate id="evaluation" name="executionCourse" property="orderedAssociatedEvaluations">
						<bean:define id="className" name="evaluation" property="class.name"/>
						<% if (evaluationType == null || evaluationType.length() == 0 || evaluationType.equals(className)) { %>
						<logic:equal name="className" value="net.sourceforge.fenixedu.domain.WrittenTest">
							<bean:define id="writtenTest" name="evaluation" type="net.sourceforge.fenixedu.domain.WrittenTest"/>
							<% if (writtenTest.hasScopeFor(year, semester, degreeCurricularPlan)) { %>
							<tr>
								<td>
									<bean:write name="executionCourse" property="nome"/>
								</td>
								<td>
									<bean:write name="evaluation" property="description"/>
								</td>
								<td>
									<dt:format pattern="dd/MM/yyyy">
										<bean:write name="evaluation" property="dayDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="beginningDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="endDate.time"/>
									</dt:format>
								</td>
								<td>
									<logic:iterate id="roomOccupation" name="evaluation" property="writtenEvaluationSpaceOccupations">
										<bean:write name="roomOccupation" property="room.nome"/>; 
									</logic:iterate>
								</td>
							</tr>
							<% } %>
						</logic:equal>
						<logic:equal name="className" value="net.sourceforge.fenixedu.domain.Exam">
							<bean:define id="writtenTest" name="evaluation" type="net.sourceforge.fenixedu.domain.Exam"/>
							<% if (writtenTest.hasScopeFor(year, semester, degreeCurricularPlan)) { %>
							<tr>
								<td>
									<bean:write name="executionCourse" property="nome"/>
								</td>
								<td>
									<bean:write name="evaluation" property="season"/>
								</td>
								<td>
									<dt:format pattern="dd/MM/yyyy">
										<bean:write name="evaluation" property="dayDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="beginningDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="endDate.time"/>
									</dt:format>
								</td>
								<td>
									<logic:iterate id="roomOccupation" name="evaluation" property="writtenEvaluationSpaceOccupations">
										<bean:write name="roomOccupation" property="room.nome"/>; 
									</logic:iterate>
								</td>
							</tr>
							<% } %>
						</logic:equal>
						<% } %>
					</logic:iterate>
				</logic:iterate>
			</table>
			<br/>
			<br/>
		</logic:iterate>
	</logic:iterate>

</logic:present>
</div>