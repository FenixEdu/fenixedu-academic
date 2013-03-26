<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.SHIFT %>" scope="request">
	<bean:define id="shift"
				 name="<%= PresentationConstants.SHIFT %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="shiftOID"
				 type="java.lang.Integer"
				 name="shift"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>
</logic:present>