<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
	<logic:present name="<%= SessionConstants.EXAM_DATEANDTIME_STR %>">
    	<bean:write name="<%= SessionConstants.EXAM_DATEANDTIME_STR %>"/>
		<br/>
	</logic:present>
