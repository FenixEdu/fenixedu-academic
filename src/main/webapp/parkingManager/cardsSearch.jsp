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

<h2><bean:message key="link.parkingCards" /></h2>
	
<script type="text/javascript">
$(function () { $("th a label").map(function(i,e) { var el = $(e); var p = el.parent(); p.html(el.html()); }) })
</script>

<fr:form action="/manageParkingCards.do?method=searchCards">
	<fr:edit id="parkingCardSearchBean" name="parkingCardSearchBean" schema="edit.parkingCardSearch">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.consultCards" bundle="PARKING_RESOURCES"/></html:submit>
	<logic:notEmpty name="parkingCardSearchBean" property="searchedParkingParties">
	
		<logic:messagesPresent message="true">
			<p class="mtop15 mbottom05">	
				<span class="warning0">
					<html:messages id="message" message="true" bundle="PARKING_RESOURCES">
						<bean:write name="message" />
					</html:messages>
				</span>
			</p>
		</logic:messagesPresent>
		
		<bean:define id="query" value=""/>
		<logic:notEmpty name="parkingCardSearchBean" property="parkingCardUserState">
			<bean:define id="parkingCardUserState" name="parkingCardSearchBean" property="parkingCardUserState"/>
			<bean:define id="query" value="<%="&parkingCardUserState="+ parkingCardUserState.toString()%>"/>
		</logic:notEmpty>
		<logic:notEmpty name="parkingCardSearchBean" property="parkingGroup">
			<bean:define id="parkingGroupID" name="parkingCardSearchBean" property="parkingGroup.externalId"/>
			<bean:define id="query" value="<%=query+"&parkingGroupID="+ parkingGroupID.toString()%>"/>
		</logic:notEmpty>
		<logic:notEmpty name="parkingCardSearchBean" property="actualEndDate">
			<bean:define id="actualEndDate" name="parkingCardSearchBean" property="actualEndDate"/>
			<bean:define id="query" value="<%=query+"&actualEndDate="+ actualEndDate.toString()%>"/>
		</logic:notEmpty>
		<logic:notEmpty name="parkingCardSearchBean" property="parkingCardSearchPeriod">
			<bean:define id="parkingCardSearchPeriod" name="parkingCardSearchBean" property="parkingCardSearchPeriod"/>
			<bean:define id="query" value="<%=query+"&parkingCardSearchPeriod="+ parkingCardSearchPeriod.toString()%>"/>
		</logic:notEmpty>
		
		<%
			String sortCriteria = request.getParameter("sortBy");	
			if (sortCriteria == null) {
			    sortCriteria = "cardEndDateToCompare=ascending";
			} else if(sortCriteria.startsWith("cardStartDate")) {
				sortCriteria = sortCriteria.replace("cardStartDate","cardStartDateToCompare");
			} else if(sortCriteria.startsWith("cardEndDate")){
				sortCriteria = sortCriteria.replace("cardEndDate","cardEndDateToCompare");
			}		
		%>
		<bean:size id="searchedParkingPartiesSize" name="parkingCardSearchBean" property="searchedParkingParties"/>
		<p>		
			Foram encontrados <strong><bean:write name="searchedParkingPartiesSize"/></strong> utentes de acordo com os critérios especificiados.<br/>
		</p>
		<fr:view name="parkingCardSearchBean" property="searchedParkingParties" schema="show.searchedParkingCards">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle1 tdcenter"/>
				<fr:property name="columnClasses" value=",,smalltxt color888,,,,aleft"/>
				<fr:property name="sortUrl" value="<%= "/manageParkingCards.do?method=searchCards"+query.toString()%>"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortBy" value="<%= sortCriteria %>"/>
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="selectedParkingCards" />
				<fr:property name="checkboxValue" value="externalId"/>
				<fr:property name="selectAllShown" value="true"/>
				<fr:property name="selectAllLocation" value="top,bottom"/>
			</fr:layout>
			<fr:destination name="parkingDetails" path="<%= "/manageParkingCards.do?method=showParkingDetails&parkingPartyID=${externalId}"  + query.toString() %>"/>
		</fr:view>
		<p><html:submit property="prepareRenewal"><bean:message key="button.renewCards" bundle="PARKING_RESOURCES"/></html:submit></p>
	</logic:notEmpty>
	<logic:empty name="parkingCardSearchBean" property="searchedParkingParties">
		<p>Não foram encontrados utentes com os critérios definidos.</p>
	</logic:empty>
</fr:form>	