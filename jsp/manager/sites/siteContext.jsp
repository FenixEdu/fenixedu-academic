<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<jsp:include page="/commons/functionalities/breadCrumbs.jsp"/>

<bean:define id="siteActionName" value="/manageSites.do" toScope="request"/>
<bean:define id="siteContextParam" value="siteID" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="idInternal" toScope="request"/>
