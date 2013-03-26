<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.gratuity.penaltyExemption" bundle="APPLICATION_RESOURCES" /></strong></h2>

<fr:edit name="gratuitySituation" schema="registration.gratuitySituation.editPenaltyExemption" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop025" />
		<fr:property name="columnClasses" value="width12em,,tdclear tderror1" />
	</fr:layout>
</fr:edit>