<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h1 class="cnone">
	<fr:view name="site" property="unit.nameWithAcronym"/>
</h1>

<jsp:include page="siteContext.jsp" flush="true"/>