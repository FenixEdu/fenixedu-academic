<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.List" %>

<table>
	<tr>
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.course.name"/></h3>
			</td>
			<td>
			<bean:parameter id="executionCourseName" name="executionCourseName"/>
				<h2><b><bean:write name="executionCourseName" /></b></h2>
			</td>	
	</tr>
	<tr>
      		<h2><bean:message bundle="MANAGER_RESOURCES" key="message.insert.professorShip.nonAffiliatedTeacher" /></h2>
	</tr>
</table>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>


<html:form action="/insertProfessorShipNonAffiliatedTeacher" method="get"> 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertNonAffiliatedTeacher" /> 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= request.getParameter("executionCourseName") %>"/>

	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.search.nonAffiliatedTeacher.name"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.nonAffiliatedTeacherName" size="50" property="nonAffiliatedTeacherName" />
			</td>
		</tr>
	</table>
	
	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='search'">
		<bean:message bundle="MANAGER_RESOURCES" key="button.search"/>
	</html:submit>			

	<br/>
	<br/>

	<logic:notEmpty name="nonAffiliatedTeachers">
	<table cellspacing="10">
		<logic:iterate name="nonAffiliatedTeachers" id="nonAffiliatedTeacher">
			<tr>
				<td><bean:write name="nonAffiliatedTeacher" property="name"/></td>
				<td><bean:write name="nonAffiliatedTeacher" property="infoInstitution.name"/></td>
				<bean:define id="nonAffiliatedTeacherID" name="nonAffiliatedTeacher" property="idInternal"/>
				<td><td><html:link module="/manager" page="<%="/insertProfessorShipNonAffiliatedTeacher.do?method=insertProfessorship&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId") + "&amp;nonAffiliatedTeacherID=" + nonAffiliatedTeacherID %>" paramId="executionCourseName" paramName="executionCourseName">Associar à disciplina</html:link></td>
			</tr>
		</logic:iterate>
	</table>
	</logic:notEmpty>
	
	<br/>
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.new.nonAffiliated.teacher"/></h3>

	<table cellpadding="5">
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.insert.nonAffiliatedTeacher.name"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.nonAffiliatedTeacherNameToInsert" size="50" property="nonAffiliatedTeacherNameToInsert" />
			</td>
		</tr>
		<tr>
			<td> Escolha a instituição à qual o docente pertence </td>		
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.institutionID" property="institutionID">
					<logic:notEmpty name="institutions">
						<html:options collection="institutions" property="idInternal" labelProperty="name" />
					</logic:notEmpty>
				</html:select>	
			</td>
		</tr>
		<logic:notPresent parameter="insertInstitution">
			<tr>
				<td><html:link module="/manager" page="<%="/insertProfessorShipNonAffiliatedTeacher.do?method=prepare&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId") + "&amp;insertInstitution=true"%>" paramId="executionCourseName" paramName="executionCourseName">Inserir nova instituição</html:link></td>
			</tr>
		</logic:notPresent>
		<logic:present parameter="insertInstitution"> 
			<tr><td colspan="2"><hr/></td></tr>
			<tr>
				<td>Introduza o nome da instituição:</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.institutionName" size="50" property="institutionName" />
				</td>			
			</tr>
			<tr>
				<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='insertInstitution'">Inserir Instituição</html:submit></td>
			</tr>
			<tr><td colspan="2"><hr/></td></tr>			
		</logic:present>	
	</table>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">Inserir docente</html:submit>
</html:form>