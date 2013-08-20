<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>	
	<bean:define id="submitUrl">/manageExecutionCourse.do?method=listExecutionCoursesToImportSections&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
	<bean:define id="showSections">/manageExecutionCourseSite.do?method=sections&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="postBackLink">/manageExecutionCourse.do?method=prepareImportSectionsPostBack&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="invalidLink">/manageExecutionCourse.do?method=prepareImportBiliographicReferencesInvalid&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	

	<H2><bean:message key="label.import.sections"/></H2>
			
	<fr:form action="<%= showSections %>">							
		<fr:edit id="importContentBean" name="importContentBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings"
			 action="<%= submitUrl %>">
			<fr:destination name="postBack" path="<%= postBackLink %>"/>			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:cancel><bean:message key="button.cancel"/></html:cancel>
	</fr:form>	

	<logic:notEmpty name="importContentBean" property="curricularYear">	
	 	<logic:notEmpty name="importContentBean" property="executionPeriod">	
	 		<logic:notEmpty name="importContentBean" property="executionDegree">	
				<br/>
				<bean:define id="importSectionsUrl">/manageExecutionCourse.do?method=importSections&executionCourseID=<bean:write name="executionCourseID"/></bean:define>		
				<H3><bean:message key="label.choose.course"/></H3>			
				<fr:edit id="importContentBeanWithExecutionCourse" name="importContentBean" schema="ListExecutionCoursesToImportContent" 
					action="<%= importSectionsUrl %>" >
					<fr:destination name="cancel" path="<%= showSections %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>
	
	<logic:notEmpty name="shifts">		
		<br/>	
		<H3><bean:message key="label.choose.shift"/></H3>
		<html:form action="/manageExecutionCourse">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="importLessonPlanningsBySummaries"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<table class="tstyle5">	
				<tr>
					<th><bean:message key="label.shift"/>:</th>		
					<td>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shiftID" >
							<html:options collection="shifts" property="value" labelProperty="label"/>
						</html:select>				
					</td>					
				</tr>				
			</table>
			<html:submit><bean:message key="label.submit"/></html:submit>
			<html:button bundle="HTMLALT_RESOURCES" altKey="button.cancel" property="cancel" onclick="this.form.method.value='sections';this.form.submit()"><bean:message key="button.cancel"/></html:button>	
		</html:form>
	</logic:notEmpty>
	
</logic:present>
