<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<logic:present name="justificationMotive">
	<h2><bean:message key="link.editJustification" /></h2>
	<bean:define id="employee" name="UserView" property="person.employee" />

	<p>
	<span class="error0">
		<html:messages id="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
	</p>

	<bean:define id="isUsed" name="justificationMotive" property="isUsed"/>
	<logic:equal name="isUsed" value="false">
		<fr:edit id="editJustificationMotive" name="justificationMotive"
			type="net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive"
			schema="edit.justificationMotives"
			action="viewAssiduousness.do?method=showJustificationMotives">
			<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
			<fr:destination name="invalid" path="/assiduousnessParametrization.do?method=sendErrorToEditJustificationMotive" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>
	<logic:notEqual name="isUsed" value="false">
		<fr:edit id="editJustificationMotive" name="justificationMotive"
			type="net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive"
			schema="edit.regularizationMotives"
			action="viewAssiduousness.do?method=showJustificationMotives">
			<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
			<fr:destination name="invalid" path="/assiduousnessParametrization.do?method=sendErrorToEditJustificationMotive" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	</logic:notEqual>
</logic:present>

<logic:notPresent name="justificationMotive">
	<h2><bean:message key="link.createJustification" /></h2>
	<bean:define id="employee" name="UserView" property="person.employee" />
	
	<p>	
	<span class="error0">
		<html:messages id="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
	</p>

	<fr:create id="createJustificationMotive"
		type="net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive"
		schema="create.justificationMotives"
		action="viewAssiduousness.do?method=showJustificationMotives">
		<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
		<fr:destination name="invalid" path="/assiduousnessParametrization.do?method=sendErrorToEditJustificationMotive" />
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:create>
</logic:notPresent>
