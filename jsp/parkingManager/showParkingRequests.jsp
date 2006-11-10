<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.requestList" /></h2>
<logic:present name="parkingRequestSearch">
	<fr:form action="/parking.do?method=showParkingRequests">
		<fr:edit id="parkingRequestSearch" name="parkingRequestSearch" schema="input.parkingRequestSearch" />
		<p><html:submit>
			<bean:message key="button.submit" />
		</html:submit></p>
	</fr:form>
	<bean:define id="parkingRequests" name="parkingRequestSearch" property="search" />
	<bean:size id="parkingRequestsNumber" name="parkingRequests"/>
	<br/>
	<p class="infoop2"><bean:message key="message.requestsNumber" bundle="PARKING_RESOURCES" arg0="<%= parkingRequestsNumber.toString() %>"/></p>
	<br/>
	
	<bean:define id="query" value=""/>
	<logic:notEmpty name="parkingRequestSearch" property="parkingRequestState">
		<bean:define id="parkingRequestState" name="parkingRequestSearch" property="parkingRequestState"/>
		<bean:define id="query" value="<%="&parkingRequestState="+ parkingRequestState.toString()%>"/>
	</logic:notEmpty>
	<logic:notEmpty name="parkingRequestSearch" property="parkingPartyClassification">
		<bean:define id="parkingPartyClassification" name="parkingRequestSearch" property="parkingPartyClassification"/>
		<bean:define id="query" value="<%=query+"&parkingPartyClassification="+ parkingPartyClassification.toString()%>"/>
	</logic:notEmpty>
	<logic:notEmpty name="parkingRequestSearch" property="personName">
		<bean:define id="personName" name="parkingRequestSearch" property="personName"/>
		<bean:define id="query" value="<%=query+"&personName="+ personName.toString()%>"/>
	</logic:notEmpty>
	<logic:notEmpty name="parkingRequestSearch" property="carPlateNumber">
		<bean:define id="carPlateNumber" name="parkingRequestSearch" property="carPlateNumber"/>
		<bean:define id="query" value="<%=query+"&carPlateNumber="+ carPlateNumber.toString()%>"/>
	</logic:notEmpty>
	
	<%
		String sortCriteria = request.getParameter("sortBy");
	
		if (sortCriteria == null) {
		    sortCriteria = "parkingParty.mostSignificantNumber=ascending";
		}
	%>
		
	<logic:notEmpty name="parkingRequests">
		<fr:view name="parkingRequests" schema="show.parkingRequest.noDetail">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="link(viewUser)" value="/parking.do?method=showParkingPartyRequests" />
				<fr:property name="key(viewUser)" value="link.viewUser" />
				<fr:property name="param(viewUser)" value="parkingParty.party.idInternal/partyID" />
				<fr:property name="bundle(viewUser)" value="PARKING_RESOURCES" />
				<fr:property name="link(viewRequest)" value="<%="/parking.do?method=showRequest"+query.toString()%>" />
				<fr:property name="key(viewRequest)" value="link.viewRequest" />
				<fr:property name="param(viewRequest)" value="idInternal" />
				<fr:property name="bundle(viewRequest)" value="PARKING_RESOURCES" />
				<fr:property name="sortUrl" value="<%= "/parking.do?method=showParkingRequests"+query.toString()%>"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortBy" value="<%= sortCriteria %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="parkingRequests">
		<bean:message key="message.no.parkingRequests" />
	</logic:empty>
</logic:present>
