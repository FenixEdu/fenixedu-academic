<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.requestList" /></h2>

<logic:present name="parkingRequestSearch">
	<fr:form action="/parking.do?method=showParkingRequests">
		<fr:edit id="parkingRequestSearch" name="parkingRequestSearch" schema="input.parkingRequestSearch">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
		<p>
			<html:submit>
				<bean:message key="button.submit" />
			</html:submit>
		</p>
	</fr:form>

	<logic:notPresent name="dontSearch">
		<bean:define id="parkingRequests" name="parkingRequestSearch" property="searchResult" />
		<bean:size id="parkingRequestsNumber" name="parkingRequests"/>
	
		<p class="mtop15">
			<em><bean:message key="message.requestsNumber" bundle="PARKING_RESOURCES" arg0="<%= parkingRequestsNumber.toString() %>"/></em>
		</p>
	
		<bean:define id="query" value=""/>
		<logic:notEmpty name="parkingRequestSearch" property="parkingRequestState">
			<bean:define id="parkingRequestState" name="parkingRequestSearch" property="parkingRequestState"/>
			<bean:define id="query" value="<%="&parkingRequestState="+ parkingRequestState.toString()%>"/>
		</logic:notEmpty>
		<logic:notEmpty name="parkingRequestSearch" property="partyClassification">
			<bean:define id="partyClassification" name="parkingRequestSearch" property="partyClassification"/>
			<bean:define id="query" value="<%=query+"&partyClassification="+ partyClassification.toString()%>"/>
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
			<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
			<html:link page="<%= "/parking.do?method=exportToExcel" + query.toString()%>">
				<bean:message key="link.exportToExcel" bundle="PARKING_RESOURCES"/>
			</html:link>
		
			<fr:view name="parkingRequests" schema="show.parkingRequest.noDetail">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle1 ulnomargin" />
					<fr:property name="columnClasses" value="acenter,,,," />
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
			<p>
				<em><bean:message key="message.no.parkingRequests" /></em>
			</p>
		</logic:empty>
	</logic:notPresent>
</logic:present>
