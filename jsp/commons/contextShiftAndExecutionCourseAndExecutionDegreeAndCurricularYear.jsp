<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<jsp:include page="contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= SessionConstants.SHIFT %>" scope="request">
	<bean:define id="shift"
				 name="<%= SessionConstants.SHIFT %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="shiftOID"
				 type="java.lang.Integer"
				 name="shift"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>
</logic:present>