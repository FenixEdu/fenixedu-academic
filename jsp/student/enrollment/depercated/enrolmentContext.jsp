<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td bgcolor="#FFFFFF" class="infoselected">
	    <bean:define id="executionYear" name="infoEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" />
		<bean:message key="label.execution.year" arg0="<%= executionYear.toString() %>"/> - <bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name" />	
		<br /><br />
		<b><bean:write name="infoEnrolmentContext"	property="infoStudentActiveCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/></b>
    </td>
  </tr>
</table>
<br/>
<html:errors/>