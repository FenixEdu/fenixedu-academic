<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionDegree.jsp"/>

<logic:present name="<%= PresentationConstants.CURRICULAR_YEAR %>" scope="request">
	<bean:define id="curricularYear"
				 name="<%= PresentationConstants.CURRICULAR_YEAR %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="curricularYearOID"
				 type="java.lang.String"
				 name="curricularYear"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>