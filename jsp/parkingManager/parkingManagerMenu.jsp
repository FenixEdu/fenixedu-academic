<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="label.parking"  /></li>
	<li><html:link page="/parking.do?method=showParkingRequests&sortBy=parkingParty.mostSignificantNumber=ascending"><bean:message key="label.requests" /></html:link></li>
	<li><html:link page="/parking.do?method=prepareSearchParty"><bean:message key="link.users" /></html:link></li>
	<li><html:link page="/exportParkingDB.do?method=exportFile"><bean:message key="link.export" /></html:link></li>
	<li><html:link page="/exportParkingDB.do?method=prepareExportFile"><bean:message key="link.mergeFiles" /></html:link></li>
</ul>

