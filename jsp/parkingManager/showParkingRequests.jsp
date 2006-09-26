<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.requestList" /></h2>
<logic:present name="parkingRequestSearch">
	<fr:edit name="parkingRequestSearch"
		schema="input.parkingRequestSearch" />

	<bean:define id="parkingRequests" name="parkingRequestSearch"
		property="search" />
	<logic:notEmpty name="parkingRequests">
		<fr:view name="parkingRequests" schema="show.parkingRequest.noDetail">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="link(view)"
					value="/parking.do?method=showRequest" />
				<fr:property name="key(view)" value="link.viewRequest" />
				<fr:property name="param(view)" value="idInternal" />
				<fr:property name="bundle(view)" value="PARKING_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="parkingRequests">
		<bean:message key="message.no.parkingRequests" />
	</logic:empty>
</logic:present>
