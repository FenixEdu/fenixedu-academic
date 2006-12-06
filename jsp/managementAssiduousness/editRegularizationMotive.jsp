<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>

<logic:present name="justificationMotive">
	<h2><bean:message key="link.editRegularization" /></h2>
	<bean:define id="employee" name="UserView" property="person.employee" />
	<span class="error0 mtop0"><html:messages id="message" message="true">
		<bean:write name="message" />
		<br />
	</html:messages></span>
	<fr:edit id="editRegularizationMotive" name="justificationMotive"
		type="net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive"
		schema="edit.regularizationMotives"
		action="viewAssiduousness.do?method=showRegularizationMotives">
		<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
		<fr:destination name="invalid" path="/assiduousnessParametrization.do?method=sendErrorToEditRegularizationMotive" />
	</fr:edit>
</logic:present>
<logic:notPresent name="justificationMotive">
	<h2><bean:message key="link.createRegularization" /></h2>
	<bean:define id="employee" name="UserView" property="person.employee" />
	<span class="error0 mtop0"><html:messages id="message" message="true">
		<bean:write name="message" />
		<br />
	</html:messages></span>

	<fr:create id="createRegularizationMotive"
		type="net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive"
		schema="show.regularizationMotives"
		action="viewAssiduousness.do?method=showRegularizationMotives">
		<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
		<fr:destination name="invalid" path="/assiduousnessParametrization.do?method=sendErrorToEditRegularizationMotive" />
	</fr:create>
</logic:notPresent>
