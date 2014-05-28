<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
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