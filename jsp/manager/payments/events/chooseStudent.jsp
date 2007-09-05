<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">

	<fr:edit id="studentNumberBean" name="studentNumberBean"
		schema="StudentNumberBean.edit"
		action="/payments.do?method=showEvents">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle5 thmiddle thright thlight mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="cancel" path="/payments.do?method=chooseStudent" />
		<fr:destination name="invalid"
			path="/payments.do?method=chooseStudentInvalid" />
	</fr:edit>

</logic:present>