<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>


<logic:present name="parkingParty">
	<h2><bean:message key="label.actualState" /></h2>
	<fr:view name="parkingParty" schema="show.parkingParty">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 ulnomargin" />
			<fr:property name="columnClasses" value="acenter,,,," />
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="parkingPartyHistories">
	<h2><bean:message key="label.history" /></h2>
	<fr:view name="parkingPartyHistories" schema="show.parkingPartyHistory">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 ulnomargin" />
			<fr:property name="columnClasses" value="acenter,,,," />
		</fr:layout>
	</fr:view>
</logic:present>
