<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.markSheet.management"/></h2>

<ul>
	<li><html:link action="/createMarkSheet.do?method=prepareCreateMarkSheet"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link></li>
	<li><html:link action="/printMarkSheetWeb.do?method=choosePrinterMarkSheetsWeb"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.print.web.markSheets"/></html:link></li>
	<logic:equal name="UserView" property="person.employee.administrativeOffice.administrativeOfficeType" value="DEGREE">
		<li><html:link action="/markSheetSendMail.do?method=prepareSearchSendMail"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.send.mail"/></html:link></li>
	</logic:equal>
</ul>


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
		 schema="markSheet.search"
		 action="/markSheetManagement.do?method=searchMarkSheets">
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
		<logic:equal name="edit" property="curricularCourse.canCreateMarkSheet" value="true">
		 - 
			<html:link action='<%= "/createMarkSheet.do?method=prepareCreateMarkSheetFilled" + url %>'><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link>
		</logic:equal>
	
		<logic:empty name="searchResult">
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.noMarkSheetsFound"/></em>
		</logic:empty>
	</p>
	
	<logic:notEmpty name="searchResult">

			<logic:iterate id="entry" name="searchResult" >
				<bean:define id="markSheetType" name="entry" property="key"/>
				<bean:define id="markSheetResult" name="entry" property="value"/>

				<div style="margin-left: 2em;">

					<p>
						<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" name="markSheetType" property="name" bundle="ENUMERATION_RESOURCES"/></strong>
						<logic:equal name="markSheetResult" property="showStatistics" value="true">
							(<bean:write name="markSheetResult" property="numberOfEnroledStudents"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.of"/> <bean:write name="markSheetResult" property="totalNumberOfStudents"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.evaluatedStudents"/>)
						</logic:equal>
					</p>
				
					<fr:view name="markSheetResult" property="markSheetsSortedByEvaluationDate" schema="markSheet.search.result.list">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle4" />
							<fr:property name="link(view)" value='<%= "/markSheetManagement.do?method=viewMarkSheet" + url %>' />
							<fr:property name="key(view)" value="label.markSheet.view" />
							<fr:property name="param(view)" value="idInternal/msID" />
							<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="order(view)" value="1" />
							<fr:property name="link(rectify)" value='<%= "/rectifyMarkSheet.do?method=prepareRectifyMarkSheet" + url %>' />
							<fr:property name="key(rectify)" value="label.markSheet.rectify" />
							<fr:property name="param(rectify)" value="idInternal/msID" />
							<fr:property name="bundle(rectify)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(rectify)" value="canRectify" />
							<fr:property name="order(rectify)" value="2" />
							<fr:property name="link(edit)" value='<%= "/editMarkSheet.do?method=prepareEditMarkSheet" + url %>' />
							<fr:property name="key(edit)" value="label.markSheet.edit" />
							<fr:property name="param(edit)" value="idInternal/msID" />
							<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(edit)" value="canEdit" />
							<fr:property name="order(edit)" value="3" />														
							<fr:property name="link(delete)" value='<%= "/markSheetManagement.do?method=prepareDeleteMarkSheet" + url %>' />
							<fr:property name="key(delete)" value="label.markSheet.remove" />
							<fr:property name="param(delete)" value="idInternal/msID" />
							<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(delete)" value="canEdit" />
							<fr:property name="order(delete)" value="4" />														
							<fr:property name="link(confirm)" value='<%= "/markSheetManagement.do?method=prepareConfirmMarkSheet" + url %>' />
							<fr:property name="key(confirm)" value="label.markSheet.confirm" />
							<fr:property name="param(confirm)" value="idInternal/msID" />
							<fr:property name="bundle(confirm)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(confirm)" value="canEdit" />
							<fr:property name="order(confirm)" value="5" />																																			
						</fr:layout>
					</fr:view>
				</div>
			</logic:iterate>

	</logic:notEmpty>
</div>
</logic:present>
