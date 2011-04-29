<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.edit" bundle="PEDAGOGICAL_COUNCIL" /></h2>


<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="tutors">

	<bean:define id="studentId" name="studentId"/>
	<fr:form action="<%= "/viewTutorship.do?method=createNewTutorship&studentId=" + studentId %>">
		<fr:edit id="tutors" name="tutors">
			<fr:schema bundle="PEDAGOGICAL_COUNCIL"
				type="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TeacherTutorshipCreationBean">
				<fr:slot name="teacher" layout="menu-select"
					key="label.tutorship.tutor">
					<fr:property name="providerClass"
						value="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TeacherTutorshipCreationBean$TutorsProvider" />
					<fr:property name="format"
						value="${teacher.teacherId} - ${name}" />
					<fr:property name="sortBy" value="teacher.teacherId" />
				</fr:slot>
			</fr:schema>

			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thleft mtop0" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		
		<p>
			<html:submit property="create"><bean:message key="label.submit.create" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
		</p>
		
</fr:form>


</logic:present>
