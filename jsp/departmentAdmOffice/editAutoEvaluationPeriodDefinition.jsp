<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="period">
	<fr:edit name="period" schema="editAutoAvaliationPeriod" layout="tabular">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>		
		</fr:layout>
		<fr:destination name="success" path="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod"/>
		<fr:destination name="cancel" path="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod"/>
	</fr:edit>
</logic:present>