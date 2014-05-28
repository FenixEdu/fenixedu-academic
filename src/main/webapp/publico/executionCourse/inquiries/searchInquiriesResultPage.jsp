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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml />

<style>

/* CSS Iframe QUC */
* { margin: 0; padding: 0; }
#button_results a {
background: none;
background: url(<%= request.getContextPath() %>/images/inq_button.gif) no-repeat center left;
padding: 8px 18px 8px 18px;
margin: 0;
font-family: "Lucida Sans", Arial, sans-serif;
font-size: 11px;
color: #fff;
text-decoration: none;
font-weight: bold; 
}
#button_results  a:hover {
color: #fff;
text-decoration: underline;
}

#button_results {
padding-bottom: 7px;
}

form label {
color:#999;
font-family: "Lucida Sans", Arial, sans-serif;
font-size: 11px;
}

form select {
color:#555;
font-family: "Lucida Sans", Arial, sans-serif;
font-size: 11px;
}

</style>

<html:form action="/searchInquiriesResultPage.do">
	<html:hidden property="method" value="prepare"/>
	
	<p>
	<label for="executionSemesterID">Semestre e Ano:</label><br/>
		<html:select property="executionSemesterID" onchange="this.form.method.value='selectExecutionSemester';this.form.submit();">
			<html:option value="">Escolha uma opção</html:option>
	 		<html:options collection="executionSemesters" property="oid" labelProperty="qualifiedName"/>
		</html:select>
	</p><br/>
	
	<p>
		<label for="executionDegreeID">Curso:</label><br/>
		<html:select property="executionDegreeID" onchange="this.form.method.value='selectExecutionDegree';this.form.submit();">
			<html:option value="">Escolha uma opção</html:option>
	 		<html:options collection="executionDegrees" property="oid" labelProperty="presentationName"/>
		</html:select>
	</p><br/>

	<p>
		<label for="executionCourseID">Unidade Curricular:</label><br/>
		<html:select property="executionCourseID" onchange="this.form.method.value='selectExecutionCourse';this.form.submit();">
			<html:option value="">Escolha uma opção</html:option>
	 		<html:options collection="executionCourses" property="oid" labelProperty="nome"/>
		</html:select>
	</p><br/>
		
</html:form>

<c:if test="${not empty executionCourse}">
	<br/>
	<bean:define id="executionCourseLink"><c:out value="${pageContext.request.contextPath}" /><c:out value="${executionCourse.site.reversePath}" />/resultados-quc</bean:define>	
	<div>
		<p id="button_results">
			<span>&nbsp;</span><!-- NO_CHECKSUM --><a href="<%= executionCourseLink %>" target="_blank" title="Ir para p&aacute;gina de Resultados">Ir para p&aacute;gina de Resultados</a>	
		</p>
	</div>
	
</c:if>