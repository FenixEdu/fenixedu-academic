<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
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
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value="acenter,,"/>
				<fr:property name="sortBy" value="personOrder"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
