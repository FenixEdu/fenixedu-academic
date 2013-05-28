<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.LESSON %>" scope="request">
	<bean:define id="lesson"
				 name="<%= PresentationConstants.LESSON %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="lessonOID"
				 type="java.lang.Integer"
				 name="lesson"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>