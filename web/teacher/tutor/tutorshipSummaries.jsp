<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.teacher.tutor.operations" /></em>
<h2><bean:message key="label.tutorshipSummary" bundle="APPLICATION_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>

<logic:present name="tutor">
	<logic:notPresent name="tutorshipHistory">
	<em><bean:message key="error.tutor.noStudent" bundle="APPLICATION_RESOURCES" /></em>
	</logic:notPresent>

	<p class="mtop2 mbottom1 separator2">
		<b><bean:message key="label.tutorshipSummary.ableToCreate" bundle="APPLICATION_RESOURCES"/></b>
	</p>
	
	<!-- available summaries to create -->
	<logic:empty name="tutorateBean" property="availableSummaries">
		<bean:message key="message.tutorshipSummary.empty" bundle="APPLICATION_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="tutorateBean" property="availableSummaries">
		<ul>
		<logic:iterate id="createSummaryBean" name="tutorateBean" property="availableSummaries">

			<li>
			
			<logic:equal value="true" name="createSummaryBean" property="persisted">
				<bean:define id="summaryId" name="createSummaryBean" property="externalId" type="java.lang.String" />
							
				<html:link page="<%= "/tutorshipSummary.do?method=createSummary&summaryId=" + summaryId %>">
					<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" /> 
					<strong><bean:write name="createSummaryBean" property="executionSemester.semester"/> - <bean:write name="createSummaryBean" property="executionSemester.executionYear.year"/></strong>,
					<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
					<strong><bean:write name="createSummaryBean" property="degree.sigla" /></strong>
				</html:link>
				<!--  (created) -->
			</logic:equal>
			
			
			<logic:equal value="false" name="createSummaryBean" property="persisted">			
				<bean:define id="teacherId" name="tutor" property="externalId" type="java.lang.String" />
				<bean:define id="degreeId" name="createSummaryBean" property="degree.externalId" type="java.lang.String" />
				<bean:define id="semesterId" name="createSummaryBean" property="executionSemester.externalId" type="java.lang.String" />
				
				<html:link page="<%= "/tutorshipSummary.do?method=createSummary&teacherId=" + teacherId + "&degreeId=" + degreeId  + "&semesterId=" + semesterId%>">
					<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" /> 
					<strong><bean:write name="createSummaryBean" property="executionSemester.semester"/> - <bean:write name="createSummaryBean" property="executionSemester.executionYear.year"/></strong>,
					<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
					<strong><bean:write name="createSummaryBean" property="degree.sigla" /></strong>
				</html:link>
				<!--  (new) -->
			</logic:equal>
			
			</li>

		</logic:iterate>
		</ul>
	</logic:notEmpty>

	<p class="mtop2 mbottom1 separator2">
		<b><bean:message key="label.tutorshipSummary.past" bundle="APPLICATION_RESOURCES"/></b>
	</p>

	<logic:empty name="tutorateBean" property="pastSummaries">
		<bean:message key="message.tutorshipSummary.empty" bundle="APPLICATION_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="tutorateBean" property="pastSummaries">
		<ul>
		<logic:iterate id="summary" name="tutorateBean" property="pastSummaries">
			<li>
				<bean:define id="summaryId" name="summary" property="externalId" />
				<html:link page="<%= "/tutorshipSummary.do?method=viewSummary&summaryId=" + summaryId %>">
					<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" /> 
					<strong><bean:write name="summary" property="semester.semester"/> - <bean:write name="summary" property="semester.executionYear.year"/></strong>,
					<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
					<strong><bean:write name="summary" property="degree.sigla" /></strong>
				</html:link>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
</logic:present>


