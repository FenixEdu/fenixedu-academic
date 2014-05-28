<%--

    Copyright © 2014 Instituto Superior Técnico

    This file is part of Fenix Parking.

    Fenix Parking is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Fenix Parking is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<h2><bean:message key="label.requestList" /></h2>

<script type="text/javascript">
$(function () { $("th a label").map(function(i,e) { var el = $(e); var p = el.parent(); p.html(el.html()); }) })
</script>

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
					<fr:property name="param(viewUser)" value="parkingParty.party.externalId/partyID" />
					<fr:property name="bundle(viewUser)" value="PARKING_RESOURCES" />
					<fr:property name="link(viewRequest)" value="<%="/parking.do?method=showRequest"+query.toString()%>" />
					<fr:property name="key(viewRequest)" value="link.viewRequest" />
					<fr:property name="param(viewRequest)" value="externalId" />
					<fr:property name="bundle(viewRequest)" value="PARKING_RESOURCES" />
					<fr:property name="link(viewHistory)" value="<%="/parking.do?method=showHistory"+query.toString()%>" />
					<fr:property name="key(viewHistory)" value="link.viewHistory" />
					<fr:property name="param(viewHistory)" value="externalId" />
					<fr:property name="bundle(viewHistory)" value="PARKING_RESOURCES" />
					<fr:property name="visibleIf(viewHistory)" value="hasHistory" />
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
