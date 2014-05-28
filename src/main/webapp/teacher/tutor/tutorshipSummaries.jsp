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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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


