<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.EXECUTION_COURSE %>" scope="request">
	<bean:define id="executionCourse"
				 name="<%= PresentationConstants.EXECUTION_COURSE %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionCourseOID"
				 type="java.lang.String"
				 name="executionCourse"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>