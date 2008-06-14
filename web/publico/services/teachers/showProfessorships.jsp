<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%--<h2><bean:message key="label.teachers.search"/></h2>--%>

<h1>Consulta de Corpo Docente</h1>


<p>
	Critério de pesquisa:
	<logic:notPresent name="executionDegree">
		<b><%= request.getAttribute("searchTarget").toString() %></b> - 
	</logic:notPresent>
	<logic:present name="executionDegree">
		<b>
			<bean:message bundle="ENUMERATION_RESOURCES"
				name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso.name" />
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />
			<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/> 
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
				<bean:write name="detailedProfessorship" property="infoProfessorship.infoTeacher.infoPerson.nome"/> &nbsp;
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