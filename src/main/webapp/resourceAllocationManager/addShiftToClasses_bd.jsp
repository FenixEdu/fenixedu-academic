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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
	   	<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<br/>
<br/>
<bean:message key="message.addshift.toclasses.warning"/>
<br/>
<br/>
<table>
<tr>
	<th class="listClasses-header">
				&nbsp;
	</th>
	<th class="listClasses-header">
				<bean:message key="label.class" /> 
	</th>
	<th class="listClasses-header">
				<bean:message key="label.degree" />
	</th>			
</tr>  
<html:form action="/addShiftToClasses">

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="addShiftToClasses"/>  	
<bean:define id="classesList" name="component" property="infoClasses"/>
<logic:iterate id="infoClass" name="component" property="infoClasses" type="net.sourceforge.fenixedu.dataTransferObject.InfoClass">
<bean:define id="externalId" name="infoClass" property="externalId"/>
<tr>
	<td class="listClasses">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.classesList"  property="classesList" value="<%= externalId.toString() %>" />
	</td>
	<td class="listClasses">
		<bean:write name="infoClass" property="nome"/>			
	</td>
	<bean:define id="infoExecutionDegree" name="infoClass" property="infoExecutionDegree"/>
	<bean:define id="infoDegreeCurricularPlan" name="infoExecutionDegree" property="infoDegreeCurricularPlan"/>
	<bean:define id="infoDegree" name="infoDegreeCurricularPlan" property="infoDegree"/>

	<td class="listClasses">
		<bean:write name="infoDegree" property="sigla"/>			
	</td>
</tr>
</logic:iterate>

</table>
<br/>
<br/>
<br/>

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

<html:submit/><html:reset/>
</html:form>
</logic:present>