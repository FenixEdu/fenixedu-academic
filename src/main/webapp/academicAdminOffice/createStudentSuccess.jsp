<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<p class="mtop15">
	<span class="success0">
		<bean:message key="message.student.registerStudent.success" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</span>
</p>

<fr:view name="registration" schema="student.show.personInformationWithIstUsername">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright"/>
        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<fr:view name="registration" schema="student.show.registrationInformationWithNumber" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright"/>
        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<bean:define id="registrationID" name="registration" property="externalId" />
<ul>
<%-- 
	<li>
		<html:link action="<%="/createStudent.do?method=printRegistrationDeclarationTemplate&amp;registrationID=" + registrationID%>" target="_blank"><bean:message key="link.student.printRegistrationDeclaration" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
--%>
<bean:define id="program" name="registration" property="degree" type="net.sourceforge.fenixedu.domain.AcademicProgram"/>
<academic:allowed operation="SERVICE_REQUESTS" program="<%= program %>">
	<li>
		<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest&schema=DocumentRequestCreateBean.chooseDocumentRequestQuickType" paramId="registrationId" paramName="registration" paramProperty="externalId">
			<bean:message key="link.student.createSchoolRegistrationDeclarationRequest" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>	
	</li>
</academic:allowed>
</ul>