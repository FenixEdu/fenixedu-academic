<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2>Consulta de Corpo Docente por Disciplina</h2>
<logic:present name="executionYears">
	
	<html:form action="/searchProfessorships" >
		<html:hidden property="method" value="prepareForm"/>
	   <p>Ano Lectivo: 		
		<html:select property="executionYearId" onchange="this.form.submit()">
			<logic:iterate id="executionYear" name="executionYears" type="DataBeans.InfoExecutionYear"> 
				<bean:define    id="executionYearId"   name="executionYear" property="idInternal"/>
					<html:option value="<%= executionYearId.toString() %>">  
						<bean:write name="executionYear" property="year"/>
					</html:option>  
			</logic:iterate>
		</html:select>

		&nbsp;&nbsp;Semestre:
			<html:select property="semester" onchange="this.form.submit()">
				<html:option value="0">Ambos Semestres</html:option>
				<html:option value="1">1&ordm; Semestre</html:option>
				<html:option value="2">2&ordm; Semestre</html:option>
			</html:select>
		
		&nbsp;&nbsp;Docentes:
		<html:select property="teacherType" onchange="this.form.submit()">
			<html:option value="0">Todos</html:option>
			<html:option value="1">Apenas Respons&aacute;veis</html:option>
		</html:select>
		</p>
	</html:form>
	
 </logic:present>
 

<hr>
<bean:define id="semInt" name="semester" type="java.lang.Integer"/>
<bean:define id="teacher" name="teacherType" type="java.lang.Integer"/>
		
<h1 align="center">
	<%= request.getAttribute("searchDetails").toString() %>
</h1>

<h2>Consulta Por Curso</h2>
<html:form action="/searchProfessorships" >
	<html:hidden property="method" value="showProfessorshipsByExecutionDegree"/>
	<html:hidden property="semester" value='<%= request.getAttribute("semester").toString() %>'/>
	<html:hidden property="teacherType" value='<%= request.getAttribute("teacherType").toString() %>'/>
	<html:hidden property="searchDetails" value='<%= request.getAttribute("searchDetails").toString() %>'/>
	<html:select property="executionDegreeId">
		
	<logic:iterate id="executionDegree" name="executionDegrees" > 
   <bean:define    id="executionDegreeId"   name="executionDegree" property="idInternal"/>
		<html:option value="<%= executionDegreeId.toString() %>"> <bean:write name="executionDegree" 
			property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/> &nbsp;em&nbsp; <bean:write 
			name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/> </html:option>
	  </logic:iterate>
	</html:select>
	<html:submit>Submeter</html:submit>
</html:form>

<h2>Consulta Por Departmento</h2>
<html:form action="/searchProfessorships" >
	<html:hidden property="method" value="showTeachersBodyByDepartment"/>
	<html:hidden property="semester" value='<%= request.getAttribute("semester").toString() %>'/>
	<html:hidden property="teacherType" value='<%= request.getAttribute("teacherType").toString() %>'/>
	<html:hidden property="executionYearId" value='<%= request.getAttribute("executionYearId").toString() %>'/>
	<html:hidden property="searchDetails" value='<%= request.getAttribute("searchDetails").toString() %>'/>
	<html:select property="departmentId">
		
	<logic:iterate id="department" name="departments" > 
   <bean:define    id="departmentId"   name="department" property="idInternal"/>
		<html:option value="<%= departmentId.toString() %>"> <bean:write name="department" 
			property="name"/>  </html:option>
	  </logic:iterate>
	</html:select>
	<html:submit>Submeter</html:submit>
</html:form>
