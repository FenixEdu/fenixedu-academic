<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
        </td>
  	</tr>
</table>
<h2><bean:message key="title.editTurno"/></h2>
<br />
<span class="error"><html:errors/></span>
<br />
	<html:form action="/editarTurnoForm">
<table>
  	<tr>
        <td><bean:message key="property.turno.name"/></td>
        <td><html:text property="nome" size="11" maxlength="20"/></td>
   	</tr>
   	<tr>
       	<td><bean:message key="property.turno.capacity"/></td>
       	<td><html:text property="lotacao" size="11" maxlength="20"/></td>
   	</tr>
</table>
<br />
<html:link page="<%= "/prepararEditarAulasDeTurno.do?"
	+ SessionConstants.SHIFT_OID
	+ "="
    + pageContext.findAttribute("shiftOID")
    + "&amp;"
	+ SessionConstants.EXECUTION_PERIOD_OID
  	+ "="
  	+ pageContext.findAttribute("executionPeriodOID")
  	+ "&amp;"
  	+ SessionConstants.CURRICULAR_YEAR_OID
	+ "="
  	+ pageContext.findAttribute("curricularYearOID")
  	+ "&amp;"
  	+ SessionConstants.EXECUTION_COURSE_OID
	+ "="
  	+ pageContext.findAttribute("executionCourseOID")
  	+ "&amp;"
	+ SessionConstants.EXECUTION_DEGREE_OID
  	+ "="
	+ pageContext.findAttribute("executionDegreeOID") %>">
	 <bean:message key="link.add.remove.aulas"/>
</html:link>
<br />
<br />
<html:link page="<%= "/listClasses.do?method=showClasses&amp;"
	+ SessionConstants.SHIFT_OID
	+ "="
    + pageContext.findAttribute("shiftOID")
    + "&amp;"
	+ SessionConstants.EXECUTION_PERIOD_OID
  	+ "="
  	+ pageContext.findAttribute("executionPeriodOID")
  	+ "&amp;"
  	+ SessionConstants.CURRICULAR_YEAR_OID
	+ "="
  	+ pageContext.findAttribute("curricularYearOID")
  	+ "&amp;"
  	+ SessionConstants.EXECUTION_COURSE_OID
	+ "="
  	+ pageContext.findAttribute("executionCourseOID")
  	+ "&amp;"
	+ SessionConstants.EXECUTION_DEGREE_OID
  	+ "="
	+ pageContext.findAttribute("executionDegreeOID") %>">
	 <bean:message key="link.add.shift.classes"/>
</html:link>
<br />
<br />
<br />
<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
			 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>
<html:submit styleClass="inputbutton"><bean:message key="label.save"/></html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>