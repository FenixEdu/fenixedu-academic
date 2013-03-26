<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>

<ul>		
	<li class="navheader"><bean:message key="label.material" bundle="RESOURCE_MANAGER_RESOURCES"/></li>
	<li><html:link page="/materialManagement.do?method=prepareMaterialManage"><bean:message key="link.management" bundle="RESOURCE_MANAGER_RESOURCES"/></html:link></li>
	
	<li class="navheader"><bean:message key="label.vehicles" bundle="RESOURCE_MANAGER_RESOURCES"/></li>
	<li><html:link page="/vehicleManagement.do?method=prepareVehicleManage"><bean:message key="link.management" bundle="RESOURCE_MANAGER_RESOURCES"/></html:link></li>
</ul>		