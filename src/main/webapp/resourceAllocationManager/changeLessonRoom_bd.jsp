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
		<td class="infoselected">
			<p>
				O curso seleccionado &eacute;:
			</p>
			<strong>
				<jsp:include page="context.jsp"/>
			</strong>
       	</td>
   	</tr>
</table>
<br />
<h2>
	<bean:message key="title.editAula"/>
</h2>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span>
<html:form action="/editarAulaForm" focus="nomeSala">
<table> 
	<tr>
			<td class="formTD">
				<bean:message key="property.aula.weekDay"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.diaSemana" property="diaSemana"/>
						<bean:write name="weekDayString"/>
					</b>
				</span>
			</td>
   	</tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.time.begining"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaInicio" property="horaInicio" write="true"/>
						:
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosInicio" property="minutosInicio" write="true"/>
					</b>
      		</span>
      	</td>
  	</tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.time.end"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaFim" property="horaFim" write="true"/>
						:
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosFim" property="minutosFim" write="true"/>
					</b>
       		</span>
       	</td>
    </tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.type"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.tipoAula" property="tipoAula">
             	<html:options collection="tiposAula" property="value" labelProperty="label"/>
						</html:select>
					</b>
           	</span>
      	</td>
   	</tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.sala"/>
				:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.nomeSala" property="nomeSala" size="1" >
            	<html:options collection="listaSalas" property="value" labelProperty="label"/>
           	</html:select>
     	</td>
  	</tr>
</table>
<br />
<bean:define id="infoAula_oid"
			 name="infoAula"
			 property="externalId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoAula_oid" property="infoAula_oid"
			 value="<%= pageContext.findAttribute("infoAula_oid").toString() %>"/>

<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton">
		<bean:message key="label.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>