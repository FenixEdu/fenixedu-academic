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
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipPeriodPartialBean"%>
<%@page import="org.joda.time.Partial"%>

<h2><bean:message key="title.tutorship.edit" bundle="PEDAGOGICAL_COUNCIL" /></h2>


<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="success">
		<p><bean:message key="label.submit.success" bundle="PEDAGOGICAL_COUNCIL"/></p>
</logic:present>

<logic:present name="successDate">
		<p><bean:message key="label.submit.success.date" bundle="PEDAGOGICAL_COUNCIL"/></p>
</logic:present>

<logic:present name="successDelete">
		<p><bean:message key="label.submit.success.delete" bundle="PEDAGOGICAL_COUNCIL"/></p>
</logic:present>

<logic:present name="periodBean">

	<bean:define id="periodBean" type="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipPeriodPartialBean" name="periodBean" />
	
	<fr:form action="/viewTutorship.do?method=changeTutorship">
		<fr:edit id="periodBean" name="periodBean">
			<fr:schema bundle="PEDAGOGICAL_COUNCIL"
				type="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipPeriodPartialBean">
				<fr:slot name="tutorship.student.name" key="label.name" readOnly="true"/>
				<fr:slot name="teacher" layout="menu-select"
					key="label.tutorship.tutor">
					<fr:property name="providerClass"
						value="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TeacherTutorshipCreationBean$TutorsProvider" />
					<fr:property name="format"
						value="${teacher.teacherId} - ${name}" />
					<fr:property name="defaultText" value="<%="-- " + ((TutorshipPeriodPartialBean)periodBean).getTutorship().getPerson().getName() + " --" %>" />

					<fr:property name="sortBy" value="teacher.teacherId" />
				</fr:slot>
				<fr:slot name="tutorship.startDate" key="label.tutorship.view.begin.date" readOnly="true"/>
				<fr:slot name="endDate" key="label.tutorship.view.end.date"	layout="year-month">
					<validator
						class="pt.ist.fenixWebFramework.renderers.validators.DateValidator">
						<fr:property name="dateFormat" value="MM/yyyy" />
					</validator>
					<fr:property name="maxLength" value="7" />
					<fr:property name="formatText"
						value="<%="Actual: " + ((TutorshipPeriodPartialBean)periodBean).getTutorship().getEndDate() + " / Formato: MM/YYYY" %>" />
					<fr:property name="size" value="7" />
				</fr:slot>
			</fr:schema>

			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thleft mtop0" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>

		<html:link action="/viewTutorship.do?method=deleteTutorship"
			paramId="tutorshipID" paramName="periodBean" paramProperty="tutorship.externalId">
			<bean:message key="label.submit.delete" bundle="PEDAGOGICAL_COUNCIL" />
		</html:link>
		<p>
			<html:submit property="create"><bean:message key="label.submit.edit" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
		</p>		
	</fr:form>

</logic:present>
