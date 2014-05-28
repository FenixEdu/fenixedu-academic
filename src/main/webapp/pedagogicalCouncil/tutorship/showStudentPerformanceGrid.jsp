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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- showStudentPerformanceGrid.jsp -->

<h2><bean:message key="title.tutorship.student.performance.grid" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:form action="/studentTutorship.do?method=showStudentPerformanceGrid">
	<table>
	<tr>
		<td>
			<fr:edit id="filterForm" name="tutorateBean" schema="tutorship.student.number">
				<fr:edit id="tutorateBean" name="tutorateBean" visible="false" />
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/studentTutorship.do?method=prepareStudentSearch" />
			</fr:edit>
		</td>
		<td>
		<html:submit>
			<bean:message key="label.submit" bundle="PEDAGOGICAL_COUNCIL" />
		</html:submit>
		</td>
	</tr>
	</table>
</fr:form>


<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
	<br/><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>


<logic:present name="student">

	<br/>

	<logic:notPresent name="performanceGridTable">
		
		<em><bean:message key="label.teacher.tutor.emptyStudentsList" bundle="APPLICATION_RESOURCES" /></em>
		
		<bean:define id="studentId" name="student" property="externalId" />
		<fr:form action="<%= "/viewTutorship.do?method=prepareCreateNewTutorship&studentId=" + studentId %>">
		
			<p><html:submit >
				<bean:message key="label.submit.create" bundle="PEDAGOGICAL_COUNCIL" />
			</html:submit></p>
		</fr:form>

	</logic:notPresent>
	
	<logic:present name="performanceGridTable">
		<table>
			<tr>
				<td><fr:view name="student" schema="tutorship.tutorate.student">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thright thlight mtop0" />
						<fr:property name="rowClasses" value="bold,,," />
					</fr:layout>
				</fr:view></td>
				<td><logic:notEmpty name="tutors">
					<table class="tstyle4 tdcenter mtop0">
						<tr>
							<th colspan="2"><bean:message key="label.tutors"
								bundle="PEDAGOGICAL_COUNCIL" /></th>
						</tr>
						<tr>
							<th><bean:message key="label.tutor.number"
								bundle="PEDAGOGICAL_COUNCIL" /></th>
							<th><bean:message key="label.tutor.name"
								bundle="PEDAGOGICAL_COUNCIL" /></th>
						</tr>
						<logic:iterate id="tutor" name="tutors">
							<tr>
								<td><bean:write name="tutor" property="teacherId" /></td>
								<td><bean:write name="tutor" property="person.name" /></td>
							</tr>
						</logic:iterate>
					</table>
				</logic:notEmpty></td>
			</tr>
		</table>
		<bean:define id="studentId" name="student" property="externalId" />
		<fr:form
			action="<%= "/viewTutorship.do?method=prepareTutorship&studentId=" +  studentId%>">

			<p><html:submit>
				<bean:message key="label.submit.edit" bundle="PEDAGOGICAL_COUNCIL" />
			</html:submit></p>
		</fr:form>
		<logic:present name="performanceGridFiltersBean">
			<fr:form>
				<fr:edit id="performanceGridFiltersBean"
					name="performanceGridFiltersBean" layout="tabular-editable">
					<fr:schema bundle="APPLICATION_RESOURCES"
						type="net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean">
						<fr:slot name="studentsEntryYear" key="label.entryYear"
							layout="menu-select-postback"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:property name="providerClass"
								value="net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider$TutorshipEntryExecutionYearProviderForSingleStudent" />
							<fr:property name="format" value="${year}" />
							<fr:property name="sortBy" value="year" />
							<fr:property name="destination" value="post-back" />
							<fr:property name="nullOptionHidden" value="true" />
						</fr:slot>
						<fr:slot name="currentMonitoringYear" key="label.monitoringYear"
							layout="menu-select-postback"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:property name="providerClass"
								value="net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipMonitoringExecutionYearProvider" />
							<fr:property name="format" value="${year}" />
							<fr:property name="sortBy" value="year" />
							<fr:property name="destination" value="post-back" />
							<fr:property name="nullOptionHidden" value="true" />
						</fr:slot>
					</fr:schema>
					<fr:layout>
						<fr:property name="classes"
							value="tstyle5 thlight thleft mbottom12 mtop0" />
						<fr:property name="columnClasses"
							value="width12em,width8em,tdclear tderror1" />
					</fr:layout>
					<fr:destination name="invalid"
						path="/studentTutorship.do?method=showStudentPerformanceGrid" />
					<fr:destination name="post-back"
						path="/studentTutorship.do?method=showStudentPerformanceGrid" />
				</fr:edit>
			</fr:form>
		</logic:present>

		<logic:notEmpty name="performanceGridTable" property="performanceGridTableLines">
		
		
			<bean:define id="degreeMaxYears" value="<%= request.getAttribute("degreeCurricularPeriods").toString() %>" />
			<%
			    Integer numberOfFixedColumns = Integer.valueOf(degreeMaxYears) * 2 + 2;

						String columnClasses = "acenter width2em,aleft width16em nowrap,acenter width2em,acenter width2em,acenter width2em,acenter,acenter";
						for (int i = 1; i <= (Integer.valueOf(degreeMaxYears) * 2); i++) {
						    columnClasses += ",nowrap";
						}
			%>
			<fr:view name="performanceGridTable" property="performanceGridTableLines" layout="student-performance-table">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 pgrid"/>
					<fr:property name="columnClasses" value="<%= columnClasses %>" />
					<fr:property name="schema" value="tutorship.tutorate.student.performanceGrid" />
					<fr:property name="numberOfFixedColumns" value="<%= numberOfFixedColumns.toString() %>" />
				</fr:layout>
			</fr:view>
			<ul class="nobullet list2">
				<li class="mvert05"><span class="approvedMonitoringYear performanceGridLegend">&nbsp;</span> Aprovado em <bean:write name="monitoringYear" property="year"/> </li>
				<li class="mvert05"><span class="approvedAnotherYear performanceGridLegend">&nbsp;</span> Aprovado noutro ano lectivo</li>
				<li class="mvert05"><span class="enroled performanceGridLegend">&nbsp;</span> Inscrito e não aprovado em <bean:write name="monitoringYear" property="year"/></li>
				<li class="mvert05"><span class="notApprovedMonitoringYear performanceGridLegend">&nbsp;</span> Reprovado em <bean:write name="monitoringYear" property="year"/></li>
				<li class="mvert05"><span class="notApprovedAnotherYear performanceGridLegend">&nbsp;</span> Inscrito e não aprovado noutro ano lectivo</li>
			</ul>
		</logic:notEmpty>
	</logic:present>
	
	<p class="mtop2 mbottom1 separator2">
		<b><bean:message key="label.tutorshipSummary.past" bundle="APPLICATION_RESOURCES"/></b>
	</p>
	
	<logic:empty name="pastSummaries">
		<bean:message key="message.tutorshipSummary.empty" bundle="APPLICATION_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="pastSummaries">
		<ul>
		<logic:iterate id="summary" name="pastSummaries">
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

