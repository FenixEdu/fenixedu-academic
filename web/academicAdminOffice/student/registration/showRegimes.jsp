<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page language="java" %>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="registration.show.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

	<ul class="mtop2 list5">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
				<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
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

	<h3 class="mbottom05"><bean:message key="registration.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<ul class="list5">
		<li>
			<html:link page="/registration.do?method=prepareCreateRegime" paramId="registrationId" paramName="registration" paramProperty="idInternal">
				<bean:message key="registration.regime.create" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>

	<logic:empty name="registrationRegimes">
		<strong><em><bean:message key="registration.no.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></strong>
	</logic:empty>

	<logic:notEmpty name="registrationRegimes">
		<bean:define id="registrationId" name="registration" property="idInternal" />

		<fr:view name="registrationRegimes" schema="RegistrationRegime.view">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="link(delete)" value='<%= "/registration.do?method=deleteRegime&amp;registrationId=" + registrationId.toString() %>' />
				<fr:property name="param(delete)" value="idInternal/registrationRegimeId" />
				<fr:property name="key(delete)" value="label.delete"/>
				<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="confirmationKey(delete)" value="registration.confirm.delete.regime" />
				<fr:property name="confirmationBundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
	
				<fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortUrl" value='<%= "/registration.do?method=showRegimes&amp;registrationId=" + registrationId.toString() %>'/>
	   	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "executionYear=desc,regimeType" : request.getParameter("sortBy") %>"/>

			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:present>
