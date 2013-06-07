<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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
