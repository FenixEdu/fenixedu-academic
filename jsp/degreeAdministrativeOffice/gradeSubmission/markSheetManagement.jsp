<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.markSheet.management"/></h2>
<br/>
<html:link action="/createMarkSheet.do?method=prepareCreateMarkSheet"><bean:message key="label.createMarkSheet"/></html:link>
<br/>
<br/>

<h2><strong><bean:message key="label.searchMarkSheet"/></strong></h2>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
</logic:messagesPresent>

<fr:edit id="edit"
		 name="edit"
		 type="net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean"
		 schema="markSheet.search"
		 action="/markSheetManagement.do?method=searchMarkSheets">
	<fr:destination name="postBack" path="/markSheetManagement.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/markSheetManagement.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:layout name="tabular"/>
</fr:edit>

<br/>
<br/>

<logic:present name="searchResult">

	<logic:empty name="searchResult">
		<em><bean:message key="label.noMarkSheetsFound"/></em>
	</logic:empty>
	
	<logic:notEmpty name="searchResult">
	
		<strong><bean:write name="edit" property="curricularCourse.name"/></strong>
		
		<bean:define id="executionPeriodID" name="edit" property="executionPeriod.idInternal" />
		<bean:define id="degreeID" name="edit" property="degree.idInternal" />
		<bean:define id="degreeCurricularPlanID" name="edit" property="degreeCurricularPlan.idInternal" />
		<bean:define id="curricularCourseID" name="edit" property="curricularCourse.idInternal" />
		
		<bean:define id="path">
			 /createMarkSheet.do?method=prepareCreateMarkSheetFilled&epID=<%= executionPeriodID %>&dID=<%= degreeID %>&dcpID=<%= degreeCurricularPlanID %>&ccID=<%= curricularCourseID %>
		</bean:define>
		<html:link action="<%= path %>"><bean:message key="label.createMarkSheet"/></html:link>
		<br/><br/>
		<bean:define id="url" name="url"/>
		<table>
			<logic:iterate id="entry" name="searchResult" >
			
				<bean:define id="markSheetType" name="entry" property="key"/>
				<bean:define id="markSheetResult" name="entry" property="value"/>
				<tr>
					<td colspan="2">
						<strong><bean:message name="markSheetType" property="name" bundle="ENUMERATION_RESOURCES"/></strong>
					</td>
				</tr>
				<logic:iterate id="markSheet" name="markSheetResult" property="markSheets" type="net.sourceforge.fenixedu.domain.MarkSheet">
					<tr>
						<td>
							<fr:view name="markSheet" layout="values" schema="markSheet.view"/>
						</td>
						<td>
							<html:link action='<%= "/markSheetManagement.do?method=viewMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
								<bean:message key="label.markSheet.view" />
							</html:link>
							<% if(markSheet.getMarkSheetState() == net.sourceforge.fenixedu.domain.MarkSheetState.RECTIFICATION_NOT_CONFIRMED || markSheet.getMarkSheetState() == net.sourceforge.fenixedu.domain.MarkSheetState.NOT_CONFIRMED){ %>
								<html:link action='<%= "/editMarkSheet.do?method=prepareEditMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.edit" />
								</html:link>
								<html:link action='<%= "/markSheetManagement.do?method=prepareDeleteMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.remove" />
								</html:link>
								<html:link action='<%= "/markSheetManagement.do?method=prepareConfirmMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.confirm" />
								</html:link>
							<% } else { %>
								<html:link action='<%= "/markSheetManagement.do?method=prepareRectifyMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.rectify" />
								</html:link>
							<% } %>																					
						</td>
					</tr>
				</logic:iterate>
				<tr><td></td><td></td></tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>