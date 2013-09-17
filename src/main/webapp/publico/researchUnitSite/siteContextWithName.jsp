<%@ page language="java" %>

<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h1 class="cnone">
	<fr:view name="site" property="unit.nameWithAcronym"/>
</h1>

<jsp:include page="siteContext.jsp" flush="true"/>