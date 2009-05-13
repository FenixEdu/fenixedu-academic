<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.grant.utils.PresentationConstants"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="userView" name="<%=pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE%>" scope="session" ></bean:define>
<logic:present name="userView" property="person.student">

<h2><bean:message  key="label.fill.information" bundle="STUDENT_RESOURCES"/></h2>

<div class="infoop2 mtop2">
	<strong>
		<bean:message  key="label.fill.missing.candidacy.information.message" bundle="STUDENT_RESOURCES"/>
	</strong>	
</div>

<br/>

<strong><bean:message  key="label.registraitons.with.missing.candidacy.information" bundle="STUDENT_RESOURCES"/></strong><br/>
<logic:iterate id="eachRegistration" name="registrationsWithMissingCandidacyInformation">
	<bean:define id="eachRegistrationId" name="eachRegistration" property="idInternal" />
	<bean:write name="eachRegistration" property="degree.presentationName"/> 
	<logic:equal name="candidacyInformationBean" property="registration.idInternal" value="<%= eachRegistrationId.toString() %>">
		<i><bean:message  key="label.editing" bundle="STUDENT_RESOURCES"/></i>
	</logic:equal>
	<br/>
</logic:iterate>

<br/><br/>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:form action="/editMissingCandidacyInformation.do?method=edit">

	<fr:edit id="candidacyInformationBean" name="candidacyInformationBean" visible="false" />

	<fr:edit id="candidacyInformationBean.editPersonalInformation"
		name="candidacyInformationBean"
		schema="CandidacyInformationBean.editPersonalInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
			<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
			<fr:destination name="invalid" path="/editMissingCandidacyInformation.do?method=prepareEditInvalid" />
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
			<fr:destination name="invalid" path="/editMissingCandidacyInformation.do?method=prepareEditInvalid" />
		</fr:layout>
	</fr:edit>	

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
	
</fr:form>

	
</logic:present>

