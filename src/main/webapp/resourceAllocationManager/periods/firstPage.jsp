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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<h2><bean:message key="link.periods" bundle="SOP_RESOURCES"/></h2>

<div class="simpleblock3 mtop2">
	<fr:form action="/showPeriods.do?method=firstPage">
		<fr:edit id="executionSemesterContextBean" name="contextBean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewPeriodsAction$ContextBean" bundle="APPLICATION_RESOURCES">
				<fr:slot name="executionSemester" layout="menu-select-postback" key="label.executionPeriod">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider"/>
					<fr:property name="format" value="${semester} - ${executionYear.year}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
		</fr:edit>
	</fr:form>
	<br/>
	<bean:message key="link.periods.execution.semester.state" bundle="SOP_RESOURCES"/>
	<strong>
		<fr:view name="contextBean" property="executionSemester.state.stateCode"/>
	</strong>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<bean:message key="link.periods.execution.semester.period" bundle="SOP_RESOURCES"/>
	<fr:view name="contextBean" property="executionSemester.beginDateYearMonthDay"/>
	-
	<fr:view name="contextBean" property="executionSemester.endDateYearMonthDay"/>
</div>

<div class="simpleblock3 mtop2">
	<fr:form action="/showPeriods.do?method=firstPage">
		<fr:edit id="executionDegreeContextBean" name="contextBean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewPeriodsAction$ContextBean" bundle="APPLICATION_RESOURCES">
				<fr:slot name="executionDegree" layout="menu-select-postback" key="label.executionDegree">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionDegreeForExecutionPeriodProvider"/>
					<fr:property name="format" value="${degree.presentationName}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
		</fr:edit>
	</fr:form>

	<logic:present name="contextBean" property="executionDegree">
		<h4>
			<bean:message key="link.periods.enrolment.periods" bundle="SOP_RESOURCES"/>
		</h4>
		<blockquote>
			<logic:notPresent name="contextBean" property="enrolmentPeriodInClasses">
				<bean:message key="link.periods.enrolment.periods.not.defined" bundle="SOP_RESOURCES"/>
			</logic:notPresent>
			<logic:present name="contextBean" property="enrolmentPeriodInClasses">
				<fr:view name="contextBean" property="enrolmentPeriodInClasses.startDateDateTime"/>
				-
				<fr:view name="contextBean" property="enrolmentPeriodInClasses.endDateDateTime"/>
			</logic:present>
		</blockquote>

		<h4>
			<bean:message key="link.periods.lesson.periods" bundle="SOP_RESOURCES"/>
		</h4>
		<blockquote>
			<logic:notPresent name="contextBean" property="lessonPeriod">
				<bean:message key="link.periods.lesson.periods.not.defined" bundle="SOP_RESOURCES"/>
			</logic:notPresent>
			<logic:present name="contextBean" property="lessonPeriod">
				<bean:define id="occupationPeriod" name="contextBean" property="lessonPeriod" toScope="request"/>
				<jsp:include page="viewOccupationPeriod.jsp"/>
			</logic:present>
		</blockquote>

		<h4>
			<bean:message key="link.periods.exam.periods" bundle="SOP_RESOURCES"/>
		</h4>
		<blockquote>
			<logic:notPresent name="contextBean" property="examPeriod">
				<bean:message key="link.periods.exam.periods.not.defined" bundle="SOP_RESOURCES"/>
			</logic:notPresent>
			<logic:present name="contextBean" property="examPeriod">
				<bean:define id="occupationPeriod" name="contextBean" property="examPeriod" toScope="request"/>
				<jsp:include page="viewOccupationPeriod.jsp"/>
			</logic:present>
		</blockquote>

		<h4>
			<bean:message key="link.periods.specialSeasonExamPeriod.periods" bundle="SOP_RESOURCES"/>
		</h4>
		<blockquote>
			<logic:notPresent name="contextBean" property="specialSeasonExamPeriod">
				<bean:message key="link.periods.specialSeasonExamPeriod.periods.not.defined" bundle="SOP_RESOURCES"/>
			</logic:notPresent>
			<logic:present name="contextBean" property="specialSeasonExamPeriod">
				<bean:define id="occupationPeriod" name="contextBean" property="specialSeasonExamPeriod" toScope="request"/>
				<jsp:include page="viewOccupationPeriod.jsp"/>
			</logic:present>
		</blockquote>
	</logic:present>
</div>


<!-- 
enrolmentPeriod
beginExecutionPeriodContexts
        private net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval academicInterval;
        private net.sourceforge.fenixedu.util.PeriodState state;
        private java.lang.String name;
        private org.joda.time.YearMonthDay beginDateYearMonthDay;
        private org.joda.time.YearMonthDay endDateYearMonthDay;
        private java.lang.String ojbConcreteClass;
 -->
 