<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="executionPeriod">
	<bean:write name="executionPeriod" property="name"/> -
	<bean:write name="executionPeriod" property="infoExecutionYear.year"/>
</logic:present>
<br />
<logic:present name="executionCourse">
	<bean:write name="executionCourse" property="sigla"/> - 
	<bean:write name="executionCourse" property="nome"/>
</logic:present>