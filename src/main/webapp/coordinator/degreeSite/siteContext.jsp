<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<bean:define id="site" name="degreeCurricularPlan" property="degree.site" toScope="request"/>

<bean:define id="siteActionName" value="/degreeSiteManagement.do" toScope="request"/>
<bean:define id="siteContextParam" value="degreeCurricularPlanID" toScope="request"/>
<bean:define id="siteContextParamValue" name="degreeCurricularPlan" property="externalId" toScope="request"/>

<jsp:include page="/coordinator/context.jsp"/>