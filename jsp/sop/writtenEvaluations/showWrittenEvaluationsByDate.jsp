<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<h2><bean:message key="title.written.evaluations.search.by.date"/></h2>

<html:form action="/searchWrittenEvaluationsByDate" focus="day">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodOID" property="executionPeriodOID" value="<%= request.getAttribute("executionPeriodOID").toString() %>"/>

	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<table>
		<tr>
			<td>
			   	<bean:message key="property.exam.date"/>:
			</td>
			<td>
   				<html:text bundle="HTMLALT_RESOURCES" altKey="text.day" maxlength="2" size="2" property="day"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.month" maxlength="2" size="2" property="month"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" maxlength="4" size="4" property="year"/>
			   	<bean:message key="label.example.date"/>
			</td>
		</tr>
		<tr>
			<td>
			    <bean:message key="property.exam.beginning"/>:
			</td>
			<td>
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginningHour" maxlength="2" size="2" property="beginningHour"/>
   				:
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginningMinute" maxlength="2" size="2" property="beginningMinute"/> 
		  		<bean:message key="label.optional"/>
			</td>
		</tr>
		<tr>
			<td>
    			<bean:message key="property.exam.end"/>:
			</td>
			<td>
			   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.endHour" maxlength="2" size="2" property="endHour"/>
			   	:
			   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.endMinute" maxlength="2" size="2" property="endMinute"/>
			   	<bean:message key="label.optional"/>
			</td>
		</tr>
	</table>
	<br/>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="lable.choose"/>
	</html:submit>

</html:form>

<logic:present name="writtenEvaluations">
	<br/>
	<table width="100%" border="1">
		<tr>
			<th class="listClasses-header"><bean:message key="lable.execution.course"/></th>
			<th class="listClasses-header"><bean:message key="lable.degree"/></th>
			<th class="listClasses-header"><bean:message key="lable.season"/></th>
			<th class="listClasses-header"><bean:message key="lable.hour"/></th>
			<th class="listClasses-header"><bean:message key="lable.rooms"/></th>
			<th class="listClasses-header"><bean:message key="lable.number.enroled.students"/></th>
			<th class="listClasses-header"><bean:message key="lable.number.missing.seats"/></th>	
		</tr>
		<logic:iterate id="writtenEvaluation" name="writtenEvaluations">
			<bean:define id="evaluationID" name="writtenEvaluation" property="idInternal"/>
			<bean:define id="evaluationTypeClassname" name="writtenEvaluation" property="class.name"/>
			<tr>
				<td class="listClasses">
					<logic:iterate id="executionCourse" name="writtenEvaluation" property="associatedExecutionCourses">
						<bean:write name="executionCourse" property="nome"/><br />
						<bean:define id="executionCourseID" name="executionCourse" property="idInternal"/>
						<bean:define id="executionPeriodID" name="executionCourse" property="executionPeriod.idInternal"/>
						<bean:define id="executionYearID" name="executionCourse" property="executionPeriod.executionYear.idInternal" type="java.lang.Integer"/>
					</logic:iterate>
				</td>
				<td class="listClasses">
					<logic:iterate id="executionCourse" name="writtenEvaluation" property="associatedExecutionCourses">
						<logic:iterate id="curricularCourse" name="executionCourse" property="associatedCurricularCourses">
							<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/><br />

							<logic:iterate id="executionDegree" name="curricularCourse" property="degreeCurricularPlan.executionDegrees">
								<logic:equal name="executionDegree" property="executionYear.idInternal" value="<%= pageContext.findAttribute("executionYearID").toString() %>">
									<bean:define id="executionDegreeID" name="executionDegree" property="idInternal"/>
								</logic:equal>
							</logic:iterate>

							<logic:iterate id="degreeModuleScope" name="curricularCourse" property="degreeModuleScopes">
								<bean:define id="curricularYearID" name="degreeModuleScope" property="curricularYear"/>
							</logic:iterate>
						</logic:iterate>
					</logic:iterate>
				</td>
				<td class="listClasses">
				<bean:define id="selectedBegin"><logic:present name="examSearchByDateForm" property="beginningHour"><logic:notEmpty name="examSearchByDateForm" property="beginningHour">true</logic:notEmpty><logic:empty name="examSearchByDateForm" property="beginningHour">false</logic:empty></logic:present><logic:notPresent name="examSearchByDateForm" property="beginningHour">false</logic:notPresent></bean:define>
				<bean:define id="selectedEnd"><logic:present name="examSearchByDateForm" property="endHour"><logic:notEmpty name="examSearchByDateForm" property="endHour">true</logic:notEmpty><logic:empty name="examSearchByDateForm" property="endHour">false</logic:empty></logic:present><logic:notPresent name="examSearchByDateForm" property="endHour">false</logic:notPresent></bean:define>

					<html:link page="<%= "/writtenEvaluations/editWrittenTest.faces?"
										+ "executionDegreeID"
										+ "="
										+ pageContext.findAttribute("executionDegreeID")
										+ "&amp;"
										+ "evaluationID"
										+ "="
										+ pageContext.findAttribute("evaluationID")
										+ "&amp;"
										+ "evaluationTypeClassname"
										+ "="
										+ pageContext.findAttribute("evaluationTypeClassname") 
										+ "&amp;"
										+ "executionPeriodID"
										+ "="
										+ pageContext.findAttribute("executionPeriodID")
										+ "&amp;"
										+ "executionPeriodOID"
										+ "="
										+ pageContext.findAttribute("executionPeriodID")
										+ "&amp;"
										+ "executionCourseID"
										+ "="
										+ pageContext.findAttribute("executionCourseID")
										+ "&amp;"
										+ "curricularYearIDsParameterString"
										+ "="
										+ pageContext.findAttribute("curricularYearID") 
										+ "&amp;"
										+ "originPage=showWrittenEvaluationsByDate"
										+ "&amp;"
										+ "selectedBegin"
										+ "="
										+ pageContext.findAttribute("selectedBegin") 
										+ "&amp;"
										+ "selectedEnd"
										+ "="
										+ pageContext.findAttribute("selectedEnd") 
										%>">
						<logic:equal name="writtenEvaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
							<bean:write name="writtenEvaluation" property="season"/>
						</logic:equal>
						<logic:equal name="writtenEvaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
							<bean:write name="writtenEvaluation" property="description"/>
						</logic:equal>
					</html:link>
				</td>
				<td class="listClasses">
					<dt:format pattern="HH:mm">
						<bean:write name="writtenEvaluation" property="beginningDate.time"/>
					</dt:format>
					-
					<dt:format pattern="HH:mm">
						<bean:write name="writtenEvaluation" property="endDate.time"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<logic:iterate id="roomOccupation" name="writtenEvaluation" property="associatedRoomOccupation">
						<bean:write name="roomOccupation" property="room.nome"/>;
					</logic:iterate>
				</td>
				<td class="listClasses">
					<bean:define id="countStudentsEnroledAttendingExecutionCourses" name="writtenEvaluation" property="countStudentsEnroledAttendingExecutionCourses"
							type="java.lang.Integer"/>
					<bean:write name="countStudentsEnroledAttendingExecutionCourses"/>
				</td>
				<td class="listClasses">
					<bean:define id="countNumberReservedSeats" name="writtenEvaluation" property="countNumberReservedSeats"
							type="java.lang.Integer"/>
					<%= "" + (countStudentsEnroledAttendingExecutionCourses.intValue() - countNumberReservedSeats.intValue()) %>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>