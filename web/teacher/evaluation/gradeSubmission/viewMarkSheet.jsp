<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

	
<h2><bean:message key="label.submited.markSheets" bundle="APPLICATION_RESOURCES"/></h2>

<p>
	<bean:define id="executionCourseID" name="executionCourseID"/>
	<html:link action='<%="/markSheetManagement.do?method=viewSubmitedMarkSheets&amp;executionCourseID=" + executionCourseID%>'><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>

	<fr:view name="markSheet" schema="markSheet.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
	
	<fr:view name="markSheet" property="enrolmentEvaluationsSortedByStudentNumber"
			 schema="markSheet.view.evaluation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight tdcenter"/>
		</fr:layout>
	</fr:view>
</p>