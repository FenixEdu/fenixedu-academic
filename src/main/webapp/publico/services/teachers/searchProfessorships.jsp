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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h1>Consulta de Corpo Docente</h1>

<logic:present name="executionYears">
	<html:form action="/searchProfessorships">
		<div class="form1">
			<fieldset>
				<legend>Opções</legend>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareForm"/>
				<p>
					Ano Lectivo: 		
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearId" property="executionYearId" onchange="this.form.submit()">
						<logic:iterate id="executionYear" name="executionYears" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear"> 
							<bean:define    id="executionYearId"   name="executionYear" property="externalId"/>
								<html:option value="<%= executionYearId.toString() %>">  
									<bean:write name="executionYear" property="year"/>
								</html:option>  
						</logic:iterate>
					</html:select>
			
					<span style="padding-left: 1em;">Semestre:</span>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.semester" property="semester" onchange="this.form.submit()">
						<html:option value="0">Ambos Semestres</html:option>
						<html:option value="1">1&ordm; Semestre</html:option>
						<html:option value="2">2&ordm; Semestre</html:option>
					</html:select>
					
					<span style="padding-left: 1em;">Docentes:</span>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.teacherType" property="teacherType" onchange="this.form.submit()">
						<html:option value="0">Todos</html:option>
						<html:option value="1">Apenas Respons&aacute;veis</html:option>
					</html:select>

					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</p>
			</fieldset>
		</div>
	</html:form>
 </logic:present>
 

<bean:define id="semInt" name="semester" type="java.lang.Integer"/>
<bean:define id="teacher" name="teacherType" type="java.lang.Integer"/>

<%--
<p>
	<%= request.getAttribute("searchDetails").toString() %>
</p>
--%>


<html:form action="/searchProfessorships" method="get">
	<div class="form1">
		<fieldset>
			<legend>Por curso</legend>
			<p>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showProfessorshipsByExecutionDegree"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.semester" property="semester" value='<%= request.getAttribute("semester").toString() %>'/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherType" property="teacherType" value='<%= request.getAttribute("teacherType").toString() %>'/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.searchDetails" property="searchDetails" value='<%= request.getAttribute("searchDetails").toString() %>'/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeId" property="executionDegreeId">
					<logic:iterate id="executionDegree" name="executionDegrees" > 
						<bean:define    id="executionDegreeId"   name="executionDegree" property="externalId"/>
						<bean:define id="degreeType"  name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.degreeType"/>
						<html:option value="<%= executionDegreeId.toString() %>"> 
						<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.degree.presentationName"/> </html:option>
					</logic:iterate>
				</html:select>
			</p>
			<p style="margin-bottom: 0;">
				<html:submit>Consultar »</html:submit>
			</p>
		</fieldset>
	</div>
</html:form>



<html:form action="/searchProfessorships" method="get" >
	<div class="form1">
		<fieldset>
			<legend>Por departmento</legend>
			<p>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTeachersBodyByDepartment"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.semester" property="semester" value='<%= request.getAttribute("semester").toString() %>'/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherType" property="teacherType" value='<%= request.getAttribute("teacherType").toString() %>'/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearId" property="executionYearId" value='<%= request.getAttribute("executionYearId").toString() %>'/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.searchDetails" property="searchDetails" value='<%= request.getAttribute("searchDetails").toString() %>'/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.departmentId" property="departmentId">
					<logic:iterate id="department" name="departments"> 
						<bean:define id="departmentId" name="department" property="externalId"/>
						<html:option value="<%= departmentId.toString() %>"> <bean:write name="department" property="name"/></html:option>
					</logic:iterate>
				</html:select>
			</p>
			<p style="margin-bottom: 0;">
				<html:submit>Consultar »</html:submit>
			</p>
		</fieldset>
	</div>
</html:form>
