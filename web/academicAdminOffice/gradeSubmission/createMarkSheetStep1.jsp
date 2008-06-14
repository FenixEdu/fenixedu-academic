<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></h2>

<p class="breadcumbs"><span class="actual"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet.step.one"/></span> &gt; <span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet.step.two"/></span></p>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<bean:define id="urlPath" name="edit" property="url" />

<fr:edit id="edit"
		 name="edit"
		 type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean"
		 schema="markSheet.create.step.one"
		 action='<%= "/createMarkSheet.do?method=createMarkSheetStepOne" + urlPath %>'>
	<fr:destination name="postBack" path="/createMarkSheet.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/createMarkSheet.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:destination name="cancel" path='<%= "/createMarkSheet.do?method=backSearchMarkSheet" + urlPath %>'/>
	<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thright thlight ulnomargin"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>


