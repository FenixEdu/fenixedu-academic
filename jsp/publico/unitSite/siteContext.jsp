<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="siteActionName" value="/units/viewSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="unitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="idInternal" toScope="request"/>
<bean:define id="site" name="unit" property="site" toScope="request"/>
