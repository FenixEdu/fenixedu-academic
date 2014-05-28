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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<%--<h2><bean:message key="label.teachers.search"/></h2>--%>

<h1>Consulta de Corpo Docente</h1>


<p>
	Critério de pesquisa:
	<logic:notPresent name="executionDegree">
		<b><%= request.getAttribute("searchTarget").toString() %></b> - 
	</logic:notPresent>
	<logic:present name="executionDegree">
		<b>
			<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.degree.presentationName"/> 
		</b> - 
	</logic:present>
	<%= request.getAttribute("searchDetails").toString() %> - 
	<%= request.getAttribute("searchType").toString() %>
</p>


<logic:notPresent name="detailedProfessorShipsListofLists">
<p><b><bean:message key="message.public.notfound.professorships"/></b></p>
</logic:notPresent>

	
		
<logic:present name="detailedProfessorShipsListofLists">
<table class="tab_lay" cellspacing="0" width="90%">
	<tr>
		<th>Disciplina</th>
		<th>Cursos</th>
		<th>Semestre</th>
		<th>Corpo Docente</th>
	</tr>
	<%
		boolean alternate = true;
	%>
	<logic:iterate id="detailedProfessorShipsList" name="detailedProfessorShipsListofLists" indexId="i">
	<% 
		String cssClass = alternate ? "" : "bluecell";
		alternate = alternate ? false : true;
	%>
	<tr class="<%= cssClass %>">
		<logic:iterate id="detailedProfessorship" name="detailedProfessorShipsList" length="1">
			<td>
				<bean:write name="detailedProfessorship" property="infoProfessorship.infoExecutionCourse.nome"/>
			</td>
			<td>
				<logic:iterate id="curricularCourse" name="detailedProfessorship" property="executionCourseCurricularCoursesList">
				<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>&nbsp;
				</logic:iterate>
			</td>	    
			<td>
				<bean:write name="detailedProfessorship" property="infoProfessorship.infoExecutionCourse.infoExecutionPeriod.name"/> -
				<bean:write name="detailedProfessorship" property="infoProfessorship.infoExecutionCourse.infoExecutionPeriod.infoExecutionYear.year"/>    
			</td>
		</logic:iterate> 
		<td>
			<logic:iterate id="detailedProfessorship" name="detailedProfessorShipsList">
					<bean:write name="detailedProfessorship" property="infoProfessorship.person.name"/> &nbsp;
					<logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
						 (respons&aacute;vel)
					</logic:equal>
				<br/>
			</logic:iterate>
		</td>
	</tr>
	</logic:iterate>
</table>
    
</logic:present>