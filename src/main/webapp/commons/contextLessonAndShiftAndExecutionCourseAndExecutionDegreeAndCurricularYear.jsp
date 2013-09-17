<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.LESSON %>" scope="request">
	<bean:define id="lesson"
				 name="<%= PresentationConstants.LESSON %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="lessonOID"
				 type="java.lang.String"
				 name="lesson"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>