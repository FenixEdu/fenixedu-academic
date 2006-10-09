<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="label.parking"  /></li>
	<li><html:link page="/parking.do?method=showParkingRequests&sortBy=creationDate=descending"><bean:message key="label.requests" /></html:link></li>
	<li><html:link page="/parking.do?method=prepareSearchParty"><bean:message key="link.users" /></html:link></li>
</ul>

