<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

	
<h2><bean:message key="label.submited.markSheets" bundle="APPLICATION_RESOURCES"/></h2>

<p>
	<bean:define id="executionCourseID" name="executionCourseID" />
	<html:link  page="/evaluation/finalEvaluationIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	<logic:empty name="markSheets">
		<em><bean:message bundle="APPLICATION_RESOURCES" key="label.no.submited.markSheets.found"/></em>
	</logic:empty>
	<logic:notEmpty name="markSheets">
		<fr:view name="markSheets"
				 schema="markSheet.teacher.gradeSubmission.view.submited.markSheets">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="creationDateDateTime"/>
					<fr:property name="classes" value="tstyle4"/>
				    <fr:property name="columnClasses" value="listClasses,,"/>
				    <fr:property name="link(viewMarkSheet)" value='<%= "/markSheetManagement.do?method=viewMarkSheet&amp;executionCourseID=" + executionCourseID %>'/>
					<fr:property name="key(viewMarkSheet)" value="label.view.markSheet"/>
					<fr:property name="param(viewMarkSheet)" value="externalId/msID"/>				
					<fr:property name="bundle(viewMarkSheet)" value="APPLICATION_RESOURCES" />				    
				</fr:layout>
		</fr:view>
	</logic:notEmpty>
</p>