<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="Javascript" type="text/javascript">
<!--
function changedExecutionYear(){
	document.forms[0].method.value="chooseDegreeCurricularPlan";
	document.forms[0].page.value="0";
	document.forms[0].submit();
}
// -->
</script>

<span class="error"><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/><br /></span>

<html:form action="/chooseExecutionYearAndDegreeCurricularPlan">
	<html:hidden property="method" value="showActiveCurricularCourseScope"/>
	

	<strong><bean:message key="label.curriculumHistoric.chooseExecutionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>:</strong><br />
	<html:select property="executionYearID" onchange="changedExecutionYear()">
		<option value=""><bean:message key="label.curriculumHistoric.chooseExecutionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/></option>
		<logic:notEmpty name="executionYears">
			<html:options collection="executionYears" property="idInternal" labelProperty="year" />
		</logic:notEmpty>
	</html:select>
	<br /><br />
	<logic:empty name="degreeCurricularPlans">
		<html:hidden property="page" value="1"/>
	</logic:empty>
	<logic:notEmpty name="degreeCurricularPlans">
		<html:hidden property="page" value="2"/>
		<strong><bean:message key="label.curriculumHistoric.chooseDegreeCurricularPlan" bundle="CURRICULUM_HISTORIC_RESOURCES"/>:</strong><br />
		<html:select property="degreeCurricularPlanID">
			<html:options collection="degreeCurricularPlans" property="idInternal" labelProperty="name" />
		</html:select>
	</logic:notEmpty>
	<br /><br />
	<html:submit styleClass="inputbutton"><bean:message key="button.continue" bundle="CURRICULUM_HISTORIC_RESOURCES"/></html:submit>
</html:form>