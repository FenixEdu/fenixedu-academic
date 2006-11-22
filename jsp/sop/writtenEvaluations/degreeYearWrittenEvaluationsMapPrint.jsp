<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="executionCoursesByCurricularYearByExecutionDegree">

<div style="font-family: Arial, sans-serif">
	<logic:iterate id="executionCoursesByCurricularYearByExecutionDegreeEntry" name="executionCoursesByCurricularYearByExecutionDegree">
		<logic:iterate id="executionCoursesByCurricularYearEntry" name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="value">
			<h2><bean:message key="title.written.evaluations.by.degree.and.curricular.year"/></h2>
			<strong>
				<bean:write name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="key.degreeCurricularPlan.degree.nome"/>
				<br/>
				<bean:define id="year" type="java.lang.Integer" name="executionCoursesByCurricularYearEntry" property="key"/> <bean:message key="label.year" arg0="<%= year.toString() %>"/>
				-
				<bean:define id="semester" type="java.lang.Integer" name="executionPeriod" property="semester"/> <bean:message key="label.period" arg0="<%= semester.toString() %>"/>
				-
				<bean:write name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="key.executionYear.year"/>
			</strong>
			<br/>
			<table border='1' cellspacing='0' cellpadding='3' width='95%' class='break-after'>
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
				<logic:iterate id="executionCourse" name="executionCoursesByCurricularYearEntry" property="value">
					<logic:iterate id="evaluation" name="executionCourse" property="associatedEvaluations">
						<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
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
									<logic:iterate id="roomOccupation" name="evaluation" property="associatedRoomOccupation">
										<bean:write name="roomOccupation" property="room.nome"/>; 
									</logic:iterate>
								</td>
							</tr>
						</logic:equal>
						<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
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
									<logic:iterate id="roomOccupation" name="evaluation" property="associatedRoomOccupation">
										<bean:write name="roomOccupation" property="room.nome"/>; 
									</logic:iterate>
								</td>
							</tr>
						</logic:equal>
					</logic:iterate>
				</logic:iterate>
			</table>
			<br/>
			<br/>
		</logic:iterate>
	</logic:iterate>
</div>
</logic:present>