<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="context.jsp"/>

<logic:present name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request">
	<bean:define id="executionDegree"
				 name="<%= PresentationConstants.EXECUTION_DEGREE %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionDegreeOID"
				 type="java.lang.String"
				 name="executionDegree"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>
