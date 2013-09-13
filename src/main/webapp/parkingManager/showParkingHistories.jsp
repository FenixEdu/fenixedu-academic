<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
