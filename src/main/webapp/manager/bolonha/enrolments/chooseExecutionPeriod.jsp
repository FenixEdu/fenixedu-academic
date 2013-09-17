<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="MANAGER">

	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>

	<html:form action="/bolonhaStudentEnrolment.do">
		<html:hidden property="method" value="prepareShowDegreeModulesToEnrol"/>
		<html:hidden property="scpId" />
		
		<fr:edit id="infoExecutionPeriod" name="infoExecutionPeriod" schema="student.bolonhaEnrolment.choose.executionPeriodInInfo" />
		
		<p class="mtop05 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="MANAGER_RESOURCES"  key="button.continue"/></html:submit>
		</p>		
		
	</html:form>

</logic:present>