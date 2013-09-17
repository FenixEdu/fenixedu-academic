<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="unitAssociations" name="result" property="resultUnitAssociations"/>

<logic:empty name="unitAssociations">
	<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.emptyList"/></em></p>
</logic:empty>
<logic:notEmpty name="unitAssociations">
	<fr:view name="unitAssociations" schema="resultUnitAssociation.details">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value=",acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
