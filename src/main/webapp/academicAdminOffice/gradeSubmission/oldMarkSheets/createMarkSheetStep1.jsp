<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createOldMarkSheet"/></h2>

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
		 schema="oldMarkSheet.create.step.one"
		 action='<%= "/oldCreateMarkSheet.do?method=createMarkSheetStepOne" + urlPath %>'>
	<fr:destination name="postBack" path="/oldCreateMarkSheet.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/oldCreateMarkSheet.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:destination name="cancel" path='<%= "/oldCreateMarkSheet.do?method=backSearchMarkSheet" + urlPath %>'/>
	<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thright thlight ulnomargin"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>


