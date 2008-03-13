<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.scientificCommission.members"/></h2>

<logic:iterate id="executionDegree" name="executionDegrees">

	<em><bean:write name="executionDegree" property="degreeCurricularPlan.degree.presentationName"/> (<bean:write name="executionDegree" property="executionYear.year"/>)</em>

	<fr:view name="executionDegree" property="scientificCommissionMembers" schema="coordinator.commissionTeam.manage.contacts">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",acenter,"/>
		</fr:layout>
	</fr:view>
</logic:iterate>

