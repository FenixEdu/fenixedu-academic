<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message  key="student.editCandidacyInformation" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="userView" name="<%=pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE%>" scope="session" ></bean:define>
<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<br/>
	
	<logic:equal name="candidacyInformationBean" property="valid" value="true">
		<p class="bluetxt">
			<em><bean:message key="label.candidacy.information.is.valid" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:equal>
	<logic:equal name="candidacyInformationBean" property="valid" value="false">
		<p class="redtxt">
			<em><bean:message key="label.candidacy.information.not.valid" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
		</p>
	</logic:equal>
	
	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<bean:define id="registrationID" name="candidacyInformationBean" property="registration.idInternal" />
	
	<fr:form action="/editCandidacyInformation.do?method=edit">
		<fr:edit id="candidacyInformationBean" name="candidacyInformationBean" visible="false" />
	
		<fr:edit id="candidacyInformationBean.editPersonalInformation"
			name="candidacyInformationBean"
			schema="CandidacyInformationBean.editPersonalInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
				<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
				
				<fr:destination name="invalid" path="/editCandidacyInformation.do?method=prepareEditInvalid" />
				<fr:destination name="cancel" path="<%= "/student.do?method=visualizeRegistration&registrationID=" + registrationID %>" />
			</fr:layout>
		</fr:edit>
		
		<br/>
		<strong><bean:message  key="label.previous.degree.information" bundle="STUDENT_RESOURCES"/></strong>
		<fr:edit id="candidacyInformationBean.editPrecedentDegreeInformation"
			name="candidacyInformationBean"
			schema="CandidacyInformationBean.editPrecedentDegreeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
				<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
				
				<fr:destination name="invalid" path="/editCandidacyInformation.do?method=prepareEditInvalid" />
				<fr:destination name="cancel" path="<%= "/student.do?method=visualizeRegistration&registrationID=" + registrationID %>" />
			</fr:layout>
		</fr:edit>	
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:cancel>	
	
	</fr:form>
	
</logic:present>

