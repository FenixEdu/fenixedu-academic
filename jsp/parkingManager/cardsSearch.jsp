<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="link.parkingCards" /></h2>

<p class="mtop15 mbottom05">	
	<span class="error0">
		<html:messages id="message" message="true" bundle="PARKING_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>
	
<fr:form action="/manageParkingPeriods.do?method=searchCards">
	<fr:edit id="parkingCardSearchBean" name="parkingCardSearchBean" schema="edit.parkingCardSearch">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.consultCards" bundle="PARKING_RESOURCES"/></html:submit>
	<logic:notEmpty name="parkingCardSearchBean" property="searchedParkingParties">
	
		<bean:define id="query" value=""/>
		<logic:notEmpty name="parkingCardSearchBean" property="parkingCardUserState">
			<bean:define id="parkingCardUserState" name="parkingCardSearchBean" property="parkingCardUserState"/>
			<bean:define id="query" value="<%="&parkingCardUserState="+ parkingCardUserState.toString()%>"/>
		</logic:notEmpty>
		<logic:notEmpty name="parkingCardSearchBean" property="parkingGroup">
			<bean:define id="parkingGroupID" name="parkingCardSearchBean" property="parkingGroup.idInternal"/>
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
				<fr:property name="sortUrl" value="<%= "/manageParkingPeriods.do?method=searchCards"+query.toString()%>"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortBy" value="<%= sortCriteria %>"/>
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="selectedParkingCards" />
				<fr:property name="checkboxValue" value="idInternal"/>
				<fr:property name="selectAllShown" value="true"/>
				<fr:property name="selectAllLocation" value="top,bottom"/>
			</fr:layout>
			<fr:destination name="parkingDetails" path="<%= "/manageParkingPeriods.do?method=showParkingDetails&parkingPartyID=${idInternal}"  + query.toString() %>"/>
		</fr:view>
		<p><html:submit property="prepareRenewal"><bean:message key="button.renewCards" bundle="PARKING_RESOURCES"/></html:submit></p>
	</logic:notEmpty>
	<logic:empty name="parkingCardSearchBean" property="searchedParkingParties">
		<p>Não foram encontrados utentes com os critérios definidos.</p>
	</logic:empty>
</fr:form>	