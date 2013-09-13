<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.SHIFT %>" scope="request">
	<bean:define id="shift"
				 name="<%= PresentationConstants.SHIFT %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="shiftOID"
				 type="java.lang.String"
				 name="shift"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>