<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2>Consulta de Corpo Docente por Disciplina</h2>
<logic:present name="executionYears">
	
	<html:form action="/searchProfessorships" >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareForm"/>
	   <p>Ano Lectivo: 		
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearId" property="executionYearId" onchange="this.form.submit()">
			<logic:iterate id="executionYear" name="executionYears" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear"> 
				<bean:define    id="executionYearId"   name="executionYear" property="idInternal"/>
					<html:option value="<%= executionYearId.toString() %>">  
						<bean:write name="executionYear" property="year"/>
					</html:option>  
			</logic:iterate>
		</html:select>

		&nbsp;&nbsp;Semestre:
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.semester" property="semester" onchange="this.form.submit()">
				<html:option value="0">Ambos Semestres</html:option>
				<html:option value="1">1&ordm; Semestre</html:option>
				<html:option value="2">2&ordm; Semestre</html:option>
			</html:select>
		
		&nbsp;&nbsp;Docentes:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.teacherType" property="teacherType" onchange="this.form.submit()">
			<html:option value="0">Todos</html:option>
			<html:option value="1">Apenas Respons&aacute;veis</html:option>
		</html:select>
		&nbsp;&nbsp;
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
		</p>
	</html:form>
	
 </logic:present>
 

<hr/>
<bean:define id="semInt" name="semester" type="java.lang.Integer"/>
<bean:define id="teacher" name="teacherType" type="java.lang.Integer"/>
		
<h1 align="center">
	<%= request.getAttribute("searchDetails").toString() %>
</h1>

<h2>Consulta Por Curso</h2>
<html:form action="/searchProfessorships" method="get">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showProfessorshipsByExecutionDegree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.semester" property="semester" value='<%= request.getAttribute("semester").toString() %>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherType" property="teacherType" value='<%= request.getAttribute("teacherType").toString() %>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.searchDetails" property="searchDetails" value='<%= request.getAttribute("searchDetails").toString() %>'/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeId" property="executionDegreeId">
		
	<logic:iterate id="executionDegree" name="executionDegrees" > 
   <bean:define    id="executionDegreeId"   name="executionDegree" property="idInternal"/>
   <bean:define id="degreeType"  name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>
       <logic:equal name="degreeType" value="DEGREE" >
	   		<html:option value="<%= executionDegreeId.toString() %>"> 
	   		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
	   		&nbsp;em&nbsp; <bean:write 
			name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/> </html:option>
		</logic:equal>    
		<logic:equal name="degreeType" value="MASTER_DEGREE" >
		    <html:option value="<%= executionDegreeId.toString() %>">
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
		    	   		&nbsp;em&nbsp; <bean:write 
			name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/> </html:option>
		</logic:equal>   
	  </logic:iterate>
	</html:select>
	<html:submit>Submeter</html:submit>
</html:form>

<h2>Consulta Por Departmento</h2>
<html:form action="/searchProfessorships" method="get" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTeachersBodyByDepartment"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.semester" property="semester" value='<%= request.getAttribute("semester").toString() %>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherType" property="teacherType" value='<%= request.getAttribute("teacherType").toString() %>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearId" property="executionYearId" value='<%= request.getAttribute("executionYearId").toString() %>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.searchDetails" property="searchDetails" value='<%= request.getAttribute("searchDetails").toString() %>'/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.departmentId" property="departmentId">
		
	<logic:iterate id="department" name="departments" > 
   <bean:define    id="departmentId"   name="department" property="idInternal"/>
		<html:option value="<%= departmentId.toString() %>"> <bean:write name="department" 
			property="name"/>  </html:option>
	  </logic:iterate>
	</html:select>
	<html:submit>Submeter</html:submit>
</html:form>
