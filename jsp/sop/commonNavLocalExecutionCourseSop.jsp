<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<!-- NOTA: Não foram incluidas tags do beans tipo <bean:message key="title.listClasses"/> -->

<p><strong>&raquo; 
  	<html:link page="<%= "/manageClasses.do?method=listClasses&amp;page=0&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
		Gest&atilde;o de Turmas
  	</html:link>
</strong></p>

<p><strong>&raquo; 
  	<html:link page="<%= "/manageShifts.do?method=listShifts&amp;page=0&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
		Gest&atilde;o de Turnos
  	</html:link>
</strong></p>