<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.List" %>

<table>
	<tr>
			<td>
				<h3><bean:message key="label.manager.execution.course.name"/></h3>
			</td>
			<td>
			<bean:parameter id="executionCourseName" name="executionCourseName"/>
				<h2><b><bean:write name="executionCourseName" /></b></h2>
			</td>	
	</tr>
	<tr>
      		<h2><bean:message key="message.insert.professorShip.nonAffiliatedTeacher" /></h2>
	</tr>
</table>
<span class="error"><html:errors/></span>

<html:form action="/insertProfessorShipNonAffiliatedTeacher" method="get"> 
	<html:hidden property="method" value="insertNonAffiliatedTeacher" /> 
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>
	<html:hidden property="executionCourseName" value="<%= request.getParameter("executionCourseName") %>"/>

	<table>
		<tr>
			<td>
				<bean:message key="message.search.nonAffiliatedTeacher.name"/>:
			</td>
			<td>
				<html:text size="50" property="nonAffiliatedTeacherName" />
			</td>
		</tr>
	</table>
	
	<br>

	<html:submit styleClass="inputbutton" onclick="this.form.method.value='search'">
		<bean:message key="button.search"/>
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
				<td><td><html:link page="<%="/insertProfessorShipNonAffiliatedTeacher.do?method=insertProfessorship&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId") + "&amp;nonAffiliatedTeacherID=" + nonAffiliatedTeacherID %>" paramId="executionCourseName" paramName="executionCourseName">Inserir</html:link></td>
			</tr>
		</logic:iterate>
	</table>
	</logic:notEmpty>
	
	<br/>
	<h3><bean:message key="label.manager.insert.new.nonAffiliated.teacher"/></h3>

	<table cellpadding="5">
		<tr>
			<td>
				<bean:message key="message.insert.nonAffiliatedTeacher.name"/>:
			</td>
			<td>
				<html:text size="50" property="nonAffiliatedTeacherNameToInsert" />
			</td>
		</tr>
		<tr>
			<td> Escolha a instituição à qual o docente pertence </td>		
			<td>
				<html:select property="institutionID">
					<logic:notEmpty name="institutions">
						<html:options collection="institutions" property="idInternal" labelProperty="name" />
					</logic:notEmpty>
				</html:select>	
			</td>
		</tr>
		<logic:notPresent parameter="insertInstitution">
			<tr>
				<td><html:link page="<%="/insertProfessorShipNonAffiliatedTeacher.do?method=prepare&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId") + "&amp;insertInstitution=true"%>" paramId="executionCourseName" paramName="executionCourseName">Inserir nova instituição</html:link></td>
			</tr>
		</logic:notPresent>
		<logic:present parameter="insertInstitution"> 
			<tr><td colspan="2"><hr></td></tr>
			<tr>
				<td>Introduza o nome da instituição:</td>
				<td>
					<html:text size="50" property="institutionName" />
				</td>			
			</tr>
			<tr>
				<td><html:submit styleClass="inputbutton" onclick="this.form.method.value='insertInstitution'">Inserir Instituição</html:submit></td>
			</tr>
			<tr><td colspan="2"><hr></td></tr>			
		</logic:present>	
	</table>
	<br/>
	<html:submit styleClass="inputbutton">Inserir docente</html:submit>
</html:form>