<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.EnrolmentStateFilterType"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.ViewType"%>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<ul class="mtop2">
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

<logic:present name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>


<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>


<h3 class="mbottom025"><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mtop025">
	<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>
	<fr:view name="registration" property="curriculum">
		<fr:layout>
			<fr:property name="visibleCurricularYearEntries" value="false" />
		</fr:layout>
	</fr:view>
</p>

<h3 class="mbottom025"><bean:message key="label.title.RegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/> e <bean:message key="final.degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mtop025">
	<p>
		<bean:message key="conclusion.date.sugested" bundle="ACADEMIC_OFFICE_RESOURCES"/> <fr:view name="registration" property="lastApprovementDate"/>
	</p>
	<p>
		<bean:message key="final.average.sugested" bundle="ACADEMIC_OFFICE_RESOURCES"/> <fr:view name="registration" property="average" type="java.lang.Integer"/> valores
	</p>
	<fr:edit id="mata" name="registrationStateBean" schema="student.manageRegistrationState.conclusionProcess" action="/manageRegistrationState.do?method=createNewState">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop025"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
</p>
