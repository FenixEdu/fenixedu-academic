<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="eventAssociations" name="result" property="resultEventAssociations"/>

<logic:empty name="eventAssociations">
	<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.emptyList"/></em></p>
</logic:empty>
<logic:notEmpty name="eventAssociations">
	<fr:view name="eventAssociations" schema="resultEventAssociation.details">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
