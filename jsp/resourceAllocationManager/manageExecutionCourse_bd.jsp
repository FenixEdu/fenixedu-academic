<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>

<em><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></h2>

<jsp:include page="contextExecutionCourse.jsp"/>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<p><span class="warning0"><bean:message key="label.manage.execution.course.note" bundle="SOP_RESOURCES"/></span></p>

<html:form action="/manageExecutionCourse" focus="theoreticalHours">

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>

	<table class="tstyle4 tdcenter">
			<tr>
				<th>
					<bean:message key="label.hours.load.theoretical"/>
				</th>
				<th>
					<bean:message key="label.hours.load.theoretical_practical"/>
				</th>
				<th>
					<bean:message key="label.hours.load.practical"/>
				</th>
				<th>
					<bean:message key="label.hours.load.laboratorial"/>
				</th>
				<th>
					<bean:message key="label.hours.load.seminary"/>
				</th>
				<th>
					<bean:message key="label.hours.load.problems"/>
				</th>
				<th>
					<bean:message key="label.hours.load.fieldWork"/>
				</th>
				<th>
					<bean:message key="label.hours.load.trainingPeriod"/>
				</th>
				<th>
					<bean:message key="label.hours.load.tutorialOrientation"/>
				</th>
			</tr>
			<tr>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHours" name="executionCourse" property="theoreticalHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHours" name="executionCourse" property="theoPratHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHours" name="executionCourse" property="praticalHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.labHours" name="executionCourse" property="labHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.seminaryHours" name="executionCourse" property="seminaryHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.problemsHours" name="executionCourse" property="problemsHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.fieldWorkHours" name="executionCourse" property="fieldWorkHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.trainingPeriodHours" name="executionCourse" property="trainingPeriodHours" size="4"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.tutorialOrientationHours" name="executionCourse" property="tutorialOrientationHours" size="4"/>
				</td>
				
			</tr>
	</table>
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="label.change"/>
	</html:submit>
</html:form>

<p class="mtop15 mbottom05"><bean:message key="label.execution.course.classes"/>:</p>
<logic:present name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
	<table class="tstyle2 tdcenter mtop05">
		<tr>
			<th>
				<bean:message key="label.degree.name"/>
			</th>
			<th>
				<bean:message key="label.name"/>
			</th>
		</tr>
		<logic:iterate id="infoClass" name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
			<bean:define id="classOID" name="infoClass" property="idInternal"/>
			<bean:define id="curricularYearOID" name="infoClass" property="anoCurricular"/>
			<bean:define id="executionDegreeOID" name="infoClass" property="infoExecutionDegree.idInternal"/>
			<tr>
				<td>
					<bean:write name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
				</td>
				<td>
					<html:link page="<%= "/manageClass.do?method=prepare&amp;"
							+ SessionConstants.CLASS_VIEW_OID
							+ "="
							+ pageContext.findAttribute("classOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID
							+ "="
							+ pageContext.findAttribute("executionPeriodOID")
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute("curricularYearOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>">
						<bean:write name="infoClass" property="nome"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="message.executionCourse.classes.none"/></span>
</logic:notPresent>