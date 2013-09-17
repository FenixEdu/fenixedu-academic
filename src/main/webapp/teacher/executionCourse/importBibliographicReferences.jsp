<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>	
	<bean:define id="submitUrl">/manageExecutionCourse.do?method=listExecutionCoursesToImportBibliographicReferences&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>
	<bean:define id="showBibliographicReferences">/manageExecutionCourse.do?method=bibliographicReference&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="postBackLink">/manageExecutionCourse.do?method=prepareImportBibliographicReferencesPostBack&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="invalidLink">/manageExecutionCourse.do?method=prepareImportBiliographicReferencesInvalid&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>	

	<h2><bean:message key="label.import.bibliographicReferences.title"/></h2>
	
	<fr:form action="<%= showBibliographicReferences %>">
		<fr:edit nested="true" id="importContentBean" name="importContentBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings"
			 action="<%= submitUrl %>">
			<fr:destination name="postBack" path="<%= postBackLink %>"/>			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:cancel><bean:message key="button.cancel"/></html:cancel>
	</fr:form>						

	<logic:notEmpty name="importContentBean" property="curricularYear">	
	 	<logic:notEmpty name="importContentBean" property="executionPeriod">	
	 		<logic:notEmpty name="importContentBean" property="executionDegree">	
				<bean:define id="importBibliographicReferencesUrl">/manageExecutionCourse.do?method=importBibliographicReferences&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>		
				<h3 class="mtop2 mbottom025"><bean:message key="label.choose.course"/></h3>
				<fr:edit id="importContentBeanWithExecutionCourse" name="importContentBean" schema="ListExecutionCoursesToImportContent" 
					action="<%= importBibliographicReferencesUrl %>">
					<fr:destination name="cancel" path="<%= showBibliographicReferences %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
				        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>
	
</logic:present>
