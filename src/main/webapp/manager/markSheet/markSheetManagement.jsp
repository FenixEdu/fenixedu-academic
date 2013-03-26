<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.markSheet.management"/></h2>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<h3 class="mbottom025"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.searchMarkSheet"/></h3>

<fr:edit id="search"
		 name="edit"
		 type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean"
		 schema="markSheet.base.manager"
		 action="/markSheetManagement.do?method=searchConfirmedMarkSheets">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>

	<fr:destination name="postBack" path="/markSheetManagement.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/markSheetManagement.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:destination name="cancel" path="/markSheetManagement.do?method=prepareSearchMarkSheet" />
</fr:edit>



<logic:present name="searchResult">
<div class="mtop15">
	<p>
		<strong><bean:write name="edit" property="curricularCourse.name"/></strong>
		
		<bean:define id="executionPeriodID" name="edit" property="executionPeriod.idInternal" />
		<bean:define id="degreeID" name="edit" property="degree.idInternal" />
		<bean:define id="degreeCurricularPlanID" name="edit" property="degreeCurricularPlan.idInternal" />
		<bean:define id="curricularCourseID" name="edit" property="curricularCourse.idInternal" />
	
		<bean:define id="url" name="url"/>	
	
		<logic:empty name="searchResult">
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.noMarkSheetsFound"/></em>
		</logic:empty>
	</p>
	
	<logic:notEmpty name="searchResult">
	
		<fr:view name="searchResult" schema="markSheet.manager.confirmed.list">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="link(removeGrades)" value='<%= "/markSheetManagement.do?method=listMarkSheet" + url %>'/>
				<fr:property name="key(removeGrades)" value="label.remove.grades"/>
				<fr:property name="param(removeGrades)" value="idInternal/msID"/>				
				<fr:property name="bundle(removeGrades)" value="ACADEMIC_OFFICE_RESOURCES" />				
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>
</div>
</logic:present>
