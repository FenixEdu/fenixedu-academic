<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="unitAssociations" name="result" property="resultUnitAssociations"/>

	<logic:empty name="unitAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="unitAssociations">
		<fr:view name="unitAssociations" schema="resultUnitAssociation.details">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
				<fr:property name="columnClasses" value=",acenter,acenter"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>