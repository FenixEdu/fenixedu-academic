<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
	
<logic:present name="<%= SessionConstants.EXAM_DATEANDTIME %>">
	<dt:format pattern="yyyy/MM/dd - HH:mm">
		<bean:write name="examDateAndTime"/>
	</dt:format>
</logic:present>
