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

	<p>
		<span class="error0">
			<html:messages id="message" message="true">
				<bean:write name="message" />
			</html:messages>
		</span>
	</p>

	<fr:edit id="editRegularizationMotive" name="justificationMotive"
		type="net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive"
		schema="edit.regularizationMotives"
		action="viewAssiduousness.do?method=showRegularizationMotives">
		<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
		<fr:destination name="invalid" path="/assiduousnessParametrization.do?method=sendErrorToEditRegularizationMotive" />
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:present>
<logic:notPresent name="justificationMotive">
	<h2><bean:message key="link.createRegularization" /></h2>
	<bean:define id="employee" name="UserView" property="person.employee" />

	<p>
	<span class="error0 mtop0">
		<html:messages id="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
	</p>

	<fr:create id="createRegularizationMotive"
		type="net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive"
		schema="show.regularizationMotives"
		action="viewAssiduousness.do?method=showRegularizationMotives">
		<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
		<fr:destination name="invalid" path="/assiduousnessParametrization.do?method=sendErrorToEditRegularizationMotive" />
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:create>
</logic:notPresent>
