<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<jsp:include page="context.jsp"/>
<logic:present name="<%= SessionConstants.EXAM_DATEANDTIME %>" scope="request">
	<bean:define id="examDateAndTime"
				 name="<%= SessionConstants.EXAM_DATEANDTIME %>"
				 property="timeInMillis"
				 toScope="request"
				 scope="request"/>
</logic:present>