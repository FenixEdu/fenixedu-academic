<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page language="java" %>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="registration.show.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

	
	<div style="float: right;">
		<bean:define id="personID" name="registration" property="student.person.idInternal"/>
		<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>

	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<span class="error0"> <bean:write name="message" /> </span>
		<br />
	</html:messages>

	<h3 class="mbottom05"><bean:message key="registration.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

	<bean:define id="registrationId" name="registration" property="idInternal" />

	<fr:create schema="RegistrationRegime.create" type="net.sourceforge.fenixedu.domain.student.RegistrationRegime"
		action='<%= "/registration.do?method=showRegimes&amp;registrationId=" + registrationId.toString() %>'>
		<fr:hidden slot="registration" name="registration"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="cancel" path='<%= "/registration.do?method=showRegimes&amp;registrationId=" + registrationId.toString() %>'/>
	</fr:create>

</logic:present>
