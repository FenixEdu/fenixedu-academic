<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
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