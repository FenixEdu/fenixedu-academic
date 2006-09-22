<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="idInternal"/>	
	<bean:define id="submitUrl">/manageExecutionCourse.do?method=listExecutionCoursesToImportBibliographicReferences&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
	<bean:define id="showBibliographicReferences">/manageExecutionCourse.do?method=bibliographicReference&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="postBackLink">/manageExecutionCourse.do?method=prepareImportBibliographicReferencesPostBack&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="invalidLink">/manageExecutionCourse.do?method=prepareBiliographicReferencesInvalid&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	

	<H2><bean:message key="label.import.bibliographicReferences.title"/></H2>
	
	<fr:form action="<%= showBibliographicReferences %>">
		<fr:edit nested="true" id="importContentBean" name="importContentBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings"
			 action="<%= submitUrl %>">
			<fr:destination name="postBack" path="<%= postBackLink %>"/>			
			<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
		<html:cancel><bean:message key="button.cancel"/></html:cancel>
	</fr:form>						

	<logic:notEmpty name="importContentBean" property="curricularYear">	
	 	<logic:notEmpty name="importContentBean" property="executionPeriod">	
	 		<logic:notEmpty name="importContentBean" property="executionDegree">	
				<br/>
				<bean:define id="importBibliographicReferencesUrl">/manageExecutionCourse.do?method=importBibliographicReferences&executionCourseID=<bean:write name="executionCourseID"/></bean:define>		
				<H3><bean:message key="label.choose.course"/></H3>			
				<fr:edit id="importContentBeanWithExecutionCourse" name="importContentBean" schema="ListExecutionCoursesToImportContent" 
					action="<%= importBibliographicReferencesUrl %>" >
					<fr:destination name="cancel" path="<%= showBibliographicReferences %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:layout name="tabular" >
							<fr:property name="classes" value="tstyle4"/>
					        <fr:property name="columnClasses" value="listClasses,,"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>
	
</logic:present>
