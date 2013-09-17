<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="context.jsp"/>
<logic:present name="<%= PresentationConstants.EXAM_DATEANDTIME %>" scope="request">
	<bean:define id="examDateAndTime"
				 name="<%= PresentationConstants.EXAM_DATEANDTIME %>"
				 property="timeInMillis"
				 toScope="request"
				 scope="request"/>
</logic:present>