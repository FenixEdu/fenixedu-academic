<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="siteActionName" value="/scientificCouncil/viewSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="unitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="idInternal" toScope="request"/>
<bean:define id="unit" name="site" property="unit" toScope="request"/>
