<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<jsp:include page="/commons/functionalities/breadCrumbs.jsp"/>

<bean:define id="site" name="homepage" toScope="request"/>
<bean:define id="siteActionName" value="/manageHomepage.do" toScope="request"/>
<bean:define id="siteContextParam" value="homepageID" toScope="request"/>
<bean:define id="siteContextParamValue" name="homepage" property="idInternal" toScope="request"/>

<em><bean:message key="label.person.main.title" /></em>