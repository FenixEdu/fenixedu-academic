<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" /> - 
<bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name" />
<html:errors />