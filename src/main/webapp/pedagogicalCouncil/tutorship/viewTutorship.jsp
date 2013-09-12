<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipPeriodPartialBean"%>
<%@page import="org.joda.time.Partial"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
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
