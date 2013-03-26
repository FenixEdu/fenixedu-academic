<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
        </td>
  	</tr>
</table>
<h2><bean:message key="title.editTurno"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
	<html:form action="/editarTurnoForm">
<table>
  	<tr>
        <td><bean:message key="property.turno.name"/></td>
        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.nome" property="nome" size="11" maxlength="20"/></td>
   	</tr>
   	<tr>
       	<td><bean:message key="property.turno.capacity"/></td>
       	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.lotacao" property="lotacao" size="11" maxlength="20"/></td>
   	</tr>
</table>
<br />
<html:link page="<%= "/prepararEditarAulasDeTurno.do?"
	+ PresentationConstants.SHIFT_OID
	+ "="
    + pageContext.findAttribute("shiftOID")
    + "&amp;"
	+ PresentationConstants.EXECUTION_PERIOD_OID
  	+ "="
  	+ pageContext.findAttribute("executionPeriodOID")
  	+ "&amp;"
  	+ PresentationConstants.CURRICULAR_YEAR_OID
	+ "="
  	+ pageContext.findAttribute("curricularYearOID")
  	+ "&amp;"
  	+ PresentationConstants.EXECUTION_COURSE_OID
	+ "="
  	+ pageContext.findAttribute("executionCourseOID")
  	+ "&amp;"
	+ PresentationConstants.EXECUTION_DEGREE_OID
  	+ "="
	+ pageContext.findAttribute("executionDegreeOID") %>">
	 <bean:message key="link.add.remove.aulas"/>
</html:link>
<br />
<br />
<html:link page="<%= "/listClasses.do?method=showClasses&amp;"
	+ PresentationConstants.SHIFT_OID
	+ "="
    + pageContext.findAttribute("shiftOID")
    + "&amp;"
	+ PresentationConstants.EXECUTION_PERIOD_OID
  	+ "="
  	+ pageContext.findAttribute("executionPeriodOID")
  	+ "&amp;"
  	+ PresentationConstants.CURRICULAR_YEAR_OID
	+ "="
  	+ pageContext.findAttribute("curricularYearOID")
  	+ "&amp;"
  	+ PresentationConstants.EXECUTION_COURSE_OID
	+ "="
  	+ pageContext.findAttribute("executionCourseOID")
  	+ "&amp;"
	+ PresentationConstants.EXECUTION_DEGREE_OID
  	+ "="
	+ pageContext.findAttribute("executionDegreeOID") %>">
	 <bean:message key="link.add.shift.classes"/>
</html:link>
<br />
<br />
<br />
<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
			 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.save"/></html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>