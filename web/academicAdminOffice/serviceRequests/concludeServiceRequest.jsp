<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<bean:define id="academicServiceRequest" name="academicServiceRequest" type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2 class="mbottom1"><bean:write name="academicServiceRequest" property="description"/></h2>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong></p>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<fr:view name="academicServiceRequest" schema="<%= simpleClassName + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>


<logic:present name="academicServiceRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="request.situation"/></strong></p>
	<bean:define id="schema" name="academicServiceRequest" property="activeSituation.class.simpleName" />
	<fr:view name="academicServiceRequest" property="activeSituation" schema="<%= schema.toString() + ".view" %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<bean:define id="registrationID" name="academicServiceRequest" property="registration.idInternal" />
<bean:define id="academicServiceRequestId" name="academicServiceRequest" property="idInternal" />

<html:form action="<%= "/academicServiceRequestsManagement.do?registrationID=" + registrationID.toString() + "&amp;academicServiceRequestId=" + academicServiceRequestId.toString() %>">
	<html:hidden property="method" value="concludeAcademicServiceRequest" />
	
	<br/>
	<strong><bean:message key="label.serviceRequests.sendEmailToStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong><html:radio property="sendEmailToStudent" value="true"><bean:message key="label.yes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio><html:radio property="sendEmailToStudent" value="false"><bean:message key="label.no" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio>
	<br/>
	<br/>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='backToViewRegistration'"><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
</html:form>
