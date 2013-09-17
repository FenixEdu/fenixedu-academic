<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="label.parking"  /></li>
	<li><html:link page="/parking.do?method=showParkingRequests&dontSearch=yes"><bean:message key="label.requests" /></html:link></li>
	<li><html:link page="/parking.do?method=prepareSearchParty"><bean:message key="link.users" /></html:link></li>
	<%--
	<li><html:link page="/manageParkingPeriods.do?method=showTerminatedContracts"><bean:message key="link.terminatedContracts" /></html:link></li>
	--%>
	<li><html:link page="/manageParkingPeriods.do?method=prepareManageRequestsPeriods"><bean:message key="link.manageRequestsPeriods" /></html:link></li>
	<li><html:link page="/manageParkingPeriods.do?method=prepareCardsSearch"><bean:message key="link.parkingCards" /></html:link></li>	
	<li><html:link page="/exportParkingDB.do?method=prepareExportFile"><bean:message key="link.mergeFiles" /></html:link></li>
</ul>

