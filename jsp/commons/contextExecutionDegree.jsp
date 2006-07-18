<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<jsp:include page="context.jsp"/>

<logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request">
	<bean:define id="executionDegree"
				 name="<%= SessionConstants.EXECUTION_DEGREE %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionDegreeOID"
				 type="java.lang.Integer"
				 name="executionDegree"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>
</logic:present>
