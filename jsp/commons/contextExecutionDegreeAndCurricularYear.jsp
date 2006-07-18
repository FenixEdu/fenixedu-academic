<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<jsp:include page="contextExecutionDegree.jsp"/>

<logic:present name="<%= SessionConstants.CURRICULAR_YEAR %>" scope="request">
	<bean:define id="curricularYear"
				 name="<%= SessionConstants.CURRICULAR_YEAR %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="curricularYearOID"
				 type="java.lang.Integer"
				 name="curricularYear"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>
</logic:present>