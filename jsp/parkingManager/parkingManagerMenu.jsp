<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="label.parking"  /></li>
	<li><html:link page="/parking.do?method=showParkingRequests"><bean:message key="label.requestList"  /></html:link></li>
	<li><html:link page="/parking.do?method=prepareSearchParty"><bean:message key="label.search"  /></html:link></li>
</ul>

