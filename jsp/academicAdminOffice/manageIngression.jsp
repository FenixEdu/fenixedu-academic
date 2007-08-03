<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><strong><bean:message key="link.student.manageIngression" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>

<bean:define id="registration" name="ingressionBean" property="registration" />

<ul class="mtop2 list5">
	<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="ingressionBean" paramProperty="registration.idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
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

<logic:present name="registration" property="ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>

<fr:edit name="ingressionBean" schema="ingression.information-edit" action="/manageIngression.do?method=editIngression">
	<fr:destination name="ingressionPostBack"
		path="/manageIngression.do?method=postBack" />
	<fr:destination name="entryPhasePostBack"
		path="/manageIngression.do?method=postBack" />
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
</fr:edit>

