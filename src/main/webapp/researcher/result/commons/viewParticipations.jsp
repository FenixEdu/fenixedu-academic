<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="participations" name="result" property="resultParticipations"/>
<bean:define id="newParticipationsSchema" value="resultParticipation.withoutRole" type="java.lang.String"/>
<logic:present name="participationsSchema">
	<bean:define id="newParticipationsSchema" name="participationsSchema" type="java.lang.String"/>
</logic:present>

<logic:empty name="participations">
	<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.emptyList"/></em></p>
</logic:empty>
<logic:notEmpty name="participations">
	<fr:view name="participations" schema="<%= newParticipationsSchema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value="acenter,,,aleft"/>
			<fr:property name="sortBy" value="personOrder"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
