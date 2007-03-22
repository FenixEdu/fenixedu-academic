<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="link.markSheet.management"/></h2>

<ul>
	<li><html:link action="/createMarkSheet.do?method=prepareCreateMarkSheet"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link></li>
	<li><html:link action="/printMarkSheetWeb.do?method=choosePrinterMarkSheetsWeb"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.print.web.markSheets"/></html:link></li>
	<logic:equal name="UserView" property="person.employee.administrativeOffice.administrativeOfficeType" value="DEGREE">
		<li><html:link action="/markSheetSendMail.do?method=prepareSearchSendMail"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.send.mail"/></html:link></li>
	</logic:equal>
</ul>


<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="DEGREE_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<h3 class="mbottom025"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.searchMarkSheet"/></h3>

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
		 - 
		<html:link action='<%= "/createMarkSheet.do?method=prepareCreateMarkSheetFilled" + url %>'><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link>
	
		<logic:empty name="searchResult">
			<em><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.noMarkSheetsFound"/></em>
		</logic:empty>
	</p>
	
	<logic:notEmpty name="searchResult">

			<logic:iterate id="entry" name="searchResult" >
				<bean:define id="markSheetType" name="entry" property="key"/>
				<bean:define id="markSheetResult" name="entry" property="value"/>

				<div style="margin-left: 2em;">

				<p>
					<strong><bean:message bundle="DEGREE_OFFICE_RESOURCES" name="markSheetType" property="name" bundle="ENUMERATION_RESOURCES"/></strong>
					<logic:equal name="markSheetResult" property="showStatistics" value="true">
						(<bean:write name="markSheetResult" property="numberOfEnroledStudents"/> <bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.of"/> <bean:write name="markSheetResult" property="totalNumberOfStudents"/> <bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.evaluatedStudents"/>)
					</logic:equal>
				</p>
				<logic:iterate id="markSheet" name="markSheetResult" property="markSheetsSortedByEvaluationDate" type="net.sourceforge.fenixedu.domain.MarkSheet">

					<p>
					
					<bean:define id="evaluationDateDateTime" type="org.joda.time.DateTime" name="markSheet" property="evaluationDateDateTime"/>
					<bean:define id="evaluationDate" ><%= evaluationDateDateTime.toString("dd/MM/yyyy") %></bean:define>
					

						<bean:write name="evaluationDate"/>
	
						<bean:message bundle="DEGREE_OFFICE_RESOURCES" name="markSheet" property="markSheetType.name" bundle="ENUMERATION_RESOURCES"/>
						
						(<bean:write name="markSheet" property="enrolmentEvaluationsCount"/> <bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.students"/>)
						- D<bean:write name="markSheet" property="responsibleTeacher.teacherNumber"/> -
						<em><bean:message bundle="DEGREE_OFFICE_RESOURCES" name="markSheet" property="markSheetState.name" bundle="ENUMERATION_RESOURCES"/></em>
							<logic:equal name="markSheet" property="submittedByTeacher" value="true">
								(<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.submittedByTeacher"/>)
							</logic:equal>
							
							 - 

							<html:link action='<%= "/markSheetManagement.do?method=viewMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
								<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.view" />
							</html:link> 
							
							<logic:equal name="markSheet" property="notConfirmed" value="true">
								<html:link action='<%= "/editMarkSheet.do?method=prepareEditMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.edit" />
								</html:link> 
								<html:link action='<%= "/markSheetManagement.do?method=prepareDeleteMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.remove" />
								</html:link> 
								<html:link action='<%= "/markSheetManagement.do?method=prepareConfirmMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.confirm" />
								</html:link>
							</logic:equal>							
							<logic:equal name="markSheet" property="confirmed" value="true">
								<%-- 
								<html:link action='<%= "/editMarkSheet.do?method=prepareEditArchiveInformation" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.archive" />
								</html:link>
								--%>
								<html:link action='<%= "/rectifyMarkSheet.do?method=prepareRectifyMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.rectify" />
								</html:link>
							</logic:equal>
							(<bean:write name="markSheet" property="prettyCheckSum"/>)
						</p>							

				</logic:iterate>
				</div>
			</logic:iterate>

	</logic:notEmpty>
</div>
</logic:present>
