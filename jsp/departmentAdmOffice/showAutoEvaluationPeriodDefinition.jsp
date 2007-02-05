<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
<bean:message key="label.defineAutoAvaliationPeriod"/>
</h2>

<fr:form action="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod">
<bean:message key="label.common.chooseExecutionYear"/>: <fr:edit id="executionYear" name="bean" slot="executionYear"> 
		<fr:layout name="menu-select-postback">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider"/>
			<fr:property name="format" value="${year}"/>
			<fr:destination name="postback" path="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="switchNone">
		<bean:message key="label.next"/>
	</html:submit>
</fr:form>

<bean:define id="executionYearId" name="bean" property="executionYear.idInternal"/>
<p>
<logic:present name="period">
	<fr:view name="period" schema="editAutoAvaliationPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
	<html:link page="<%= "/autoEvaluationTeacherExpectationManagementAction.do?method=editPeriod&executionYearId=" + executionYearId %>">
	  <bean:message key="link.edit"/>
	</html:link>
</logic:present>

<logic:notPresent name="period">
	<bean:message key="label.noAutoEvaluationPeriodDefined"/>
	<html:link page="<%= "/autoEvaluationTeacherExpectationManagementAction.do?method=createPeriod&executionYearId=" + executionYearId %>">
		<bean:message key="label.teacher-institution-working-time.create"/>
	</html:link>
</logic:notPresent>
</p>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>