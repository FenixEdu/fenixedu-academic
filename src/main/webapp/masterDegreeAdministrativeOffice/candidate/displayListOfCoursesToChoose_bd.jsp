<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><!-- Error messages go here --><html:errors /></span>


<%--
<logic:present name="infoExecutionDegree" >
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" /><br />
	<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>:<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" /><br />
</logic:present>
--%>

<logic:present name="candidate">

	<table>
	  <tr>
	    <td><h2><bean:message key="label.candidate.personalInfo" /><h2></td>
	  </tr>
	  <tr>
	    <td><bean:message key="label.person.name" /></td>
	    <td><bean:write name="candidate" property="infoPerson.nome"/></td>
	  </tr>
	  <!-- Candidate Number -->
	  <tr>
	    <td><bean:message key="label.candidate.candidateNumber" /></td>
	    <td><bean:write name="candidate" property="candidateNumber"/></td>
	  </tr>
	
	  <!-- Specialization -->
	  <tr>
	    <td><bean:message key="label.candidate.specialization" /></td>
	    <td><bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/></td>
	  </tr>
	
	  <!-- Specialization Area -->
	  <tr>
	    <td><bean:message key="label.candidate.specializationArea" /></td>
	    <td><bean:write name="candidate" property="specializationArea"/></td>
	  </tr>
	
	</table>
	<br />
	<br />
	
	<table>
	  <tr>
	    <td><h2><bean:message key="label.candidate.majorDegreeInfo" /><h2></td>
	  </tr>
	  <!-- Licenciatura -->
	  <tr>
	    <td><bean:message key="label.candidate.majorDegree" /></td>
	    <td><bean:write name="candidate" property="majorDegree"/></td>
	  </tr>
	  <!-- Ano de Licenciatura -->
	  <tr>
	    <td><bean:message key="label.candidate.majorDegreeYear" /></td>
	    <td><bean:write name="candidate" property="majorDegreeYear"/></td>
	  </tr>
	  <!-- Escola de Licenciatura -->
	  <tr>
	    <td><bean:message key="label.candidate.majorDegreeSchool" /></td>
	    <td><bean:write name="candidate" property="majorDegreeSchool"/></td>
	  </tr>
	  <!-- Media -->
	  <tr>
	    <td><bean:message key="label.candidate.average" /></td>
	    <td><bean:write name="candidate" property="average"/> <bean:message key="label.candidate.values"/></td>
	  </tr>
	</table>
	
	
	<logic:present name="curricularCourses">
		<h2><bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourseToStudyPlan" /></h2>
	
		<html:form action="/displayCourseListToStudyPlan?method=chooseCurricularCourses">
	   	  	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/> 
	   	  	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidateID" property="candidateID"/>
	   	  	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/> 
			<!-- CurricularCourse -->
			<table>
				<tr>
					<td>
						<bean:message key="label.givenCredits"/>
					</td>
					<td>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.attributedCredits" property="attributedCredits" size="2"/>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.givenCreditsRemarks"/>
					</td>
					<td>
						<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.givenCreditsRemarks" property="givenCreditsRemarks" cols="30" rows="5" />
					</td>
				</tr>
			</table>
			
			<table>
				<logic:present name="candidateEnrolments">		
					<h2><bean:message key="label.masterDegree.alreadyChosenCourses"/></h2>
	
					<logic:iterate id="candidateEnrolment" name="candidateEnrolments" indexId="index">
						<tr>
							<td>
	<%--							<html:multibox alt='<%= "selection[" + index.intValue() + "]" %>' property='<%= "selection[" + index.intValue() + "]" %>'>
	--%>						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selection" property="selection" >	
	
									<bean:write name="candidateEnrolment" property="infoCurricularCourse.externalId"/>
								</html:multibox>
							</td>
							<td>
								<bean:write name="candidateEnrolment" property="infoCurricularCourse.name"/>
							</td>
							<%-- <td>
								<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoBranch.name"/> <br/>
							</td> --%>
						</tr>
					</logic:iterate>
				</logic:present>
			</table>
	
			<logic:iterate id="curricularCourseElem" name="curricularCourses" length="1">
				<h2><bean:message key="label.masterDegree.newCourses"/> <bean:write name="curricularCourseElem" property="infoDegreeCurricularPlan.infoDegree.nome" /></h2>
			</logic:iterate>
			<br />
			<br />
			
			<div class="infoop">
				<span class="error"><!-- Error messages go here --><strong>Nota:</strong></span>
				<br />&nbsp;&nbsp;&nbsp;&nbsp;O nome das Áreas Científicas aparecem por baixo do nome da disciplina e ligeiramente mais à frente. Nesse caso a opção é feita no quadrado à frente da área científica.
				<br />&nbsp;&nbsp;&nbsp;&nbsp;Caso a disciplina não pertença a nenhuma Área Científica, a opção para a sua selecção aparecerá imediatamente antes do seu nome. 
			</div>
	
			<br />
			<br />
			
			<table>
				<logic:iterate id="curricularCourseElem" name="curricularCourses"  indexId="index">
				   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
					<tr>
						<td>
						<bean:define id="offset" value="0"/>
							<bean:size id="ccsSize" name="curricularCourseElem" property="curricularCourse.scopes" />
							<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="curricularCourse.scopes" 
									  offset="0" length="1">
		        						<logic:equal name="curricularCourseScope" property="branch.name"  value='<%= new String("") %>'>
		        							<bean:define id="offset" value="1"/>
		            						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selection" property="selection">
		                						<bean:write name="curricularCourseScope" property="curricularCourse.externalId"/>
		            						</html:multibox>
		        						</logic:equal>
		        						<logic:notEqual name="curricularCourseScope" property="branch.name"  value='<%= new String("") %>'>
		        							<bean:define id="offset" value="0"/>
		        						</logic:notEqual>
		        						<strong><bean:write name="curricularCourseElem" property="name"/></strong><br />
							</logic:iterate>
							
						    <blockquote>
		            			<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="curricularCourse.scopes" 
		            						    offset="<%= new String(offset) %>" length="<%= String.valueOf(ccsSize.intValue() - Integer.parseInt(offset)) %>">
		                						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selection" property="selection">
		                    						<bean:write name="curricularCourseElem" property="externalId"/>
		                						</html:multibox>
		               							<bean:write name="curricularCourseScope" property="branch.name"/> <br/>
								</logic:iterate>	
						    </blockquote>
						</td>
					</tr>
				</logic:iterate>
			</table>		
	<br />
			
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/>
	</html:form>
	</logic:present>
</logic:present>
	