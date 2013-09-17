<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.view" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:edit id="tutorsBean" name="tutorsBean" action="/viewTutors.do?method=listTutors">
	<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.ViewTutorsDA$ViewTutorsBean" bundle="PEDAGOGICAL_COUNCIL">
		<fr:slot name="executionSemester" layout="menu-select-postback" key="label.tutorship.year">
			<fr:property name="providerClass"
				value="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.ViewTutorsDA$ExecutionSemestersProvider" />
			<fr:property name="bundle" value="PEDAGOGICAL_COUNCIL" />
			<fr:property name="format" value="${executionYear.year}" />
			<fr:property name="destination" value="semesterPostBack" />
		</fr:slot>
		<fr:slot name="executionDegree" layout="menu-select" key="label.degree">
			<fr:property name="providerClass"
				value="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.ViewTutorsDA$ContextDegreesProvider"/>
			<fr:property name="bundle" value="PEDAGOGICAL_COUNCIL" />
			<fr:property name="format" value="${presentationName}" />
			<fr:property name="sortBy" value="presentationName" />
		</fr:slot>
	</fr:schema>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thleft mtop0" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="semesterPostBack" path="/viewTutors.do?method=listTutors"/>
</fr:edit>


<logic:notEmpty name="tutorsBean" property="tutors">		
	<fr:view name="tutorsBean" property="tutors">
		<fr:schema type="net.sourceforge.fenixedu.domain.TutorshipIntention" bundle="APPLICATION_RESOURCES">
			<fr:slot name="teacher.person.istUsername" key="label.istUsername"/>
			<fr:slot name="teacher.person.name" key="label.name"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="link(view)" value="/viewTutors.do?method=viewStudentsOfTutorship"/>
			<fr:property name="param(view)" value="externalId/tutorshipIntentionID"/>
			<fr:property name="key(view)" value="label.tutorship.students.view"/>
			<fr:property name="bundle(view)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIfNot(view)" value="deletable"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>	

<logic:empty name="tutorsBean" property="tutors">
	<p><bean:message key="message.tutorship.dontExist.tutors" bundle="PEDAGOGICAL_COUNCIL" /></p>
</logic:empty>