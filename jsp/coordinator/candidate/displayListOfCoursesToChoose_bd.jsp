<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>

<p><span class="error"><html:errors/></span></p>

<br />

<logic:present name="candidate">

	<h2><bean:message key="title.candidate.studyPlan" /></h2>
	<p><span class="error"><html:errors/></span></p>
	<table width="100%" cellspacing="0">
	  <tr>
	  	<td class="infoop" width="50px"><span class="emphasis-box">1</span>
	    <td class="infoop"><strong><bean:message key="title.candidate.info" /></strong></td>
	  </tr>
	</table>
	<br />
	<table>
	  <tr>
	    <td><bean:message key="label.person.name" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="infoPerson.nome"/></span></td>
	  </tr>
	  <!-- Candidate Number -->
	  <tr>
	    <td><bean:message key="label.candidate.candidateNumber" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="candidateNumber"/></span></td>
	  </tr>
	  <!-- Specialization -->
	  <tr>
	    <td><bean:message key="label.candidate.specialization" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="specialization"/></span></td>
	  </tr>
	
	  <!-- Specialization Area -->
	  <tr>
	    <td><bean:message key="label.candidate.specializationArea" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="specializationArea"/></span></td>
	  </tr>
	</table>
	<br />
	<table width="100%" cellspacing="0">
	  <tr>
	  	<td class="infoop" width="50px"><span class="emphasis-box">2</span>
	    <td class="infoop"><strong><bean:message key="label.candidate.majorDegreeInfo" /></strong></td>
	  </tr>
	</table>
	<table>
	  <!-- Licenciatura -->
	  <tr>
	    <td><bean:message key="label.candidate.majorDegree" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="majorDegree"/></span></td>
	  </tr>
	  <!-- Ano de Licenciatura -->
	  <tr>
	    <td><bean:message key="label.candidate.majorDegreeYear" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="majorDegreeYear"/></span></td>
	  </tr>
	  <!-- Escola de Licenciatura -->
	  <tr>
	    <td><bean:message key="label.candidate.majorDegreeSchool" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="majorDegreeSchool"/></span></td>
	  </tr>
	  <!-- Media -->
	  <tr>
	    <td><bean:message key="label.candidate.average" /></td>
	    <td><span class="greytxt"><bean:write name="candidate" property="average"/></span> <bean:message key="label.candidate.values"/></td>
	  </tr>
	</table>
	<%--
	<logic:present name="infoExecutionDegree" >
		<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" /><br />
		<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>:<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" /><br />
	</logic:present>
	--%>
	<br />
	<logic:present name="curricularCourses">
	<table width="100%" cellspacing="0">
		<tr>
			<td class="infoop" width="50px"><span class="emphasis-box">3</span></td>
			<td class="infoop"><strong><bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourseToStudyPlan" /></strong></td>
		</tr>
	</table>
		<html:form action="/displayCourseListToStudyPlan?method=chooseCurricularCourses">
	   	  	<html:hidden property="page" value="1"/> 
	   	  	<html:hidden property="candidateID"/> 
	   	  	<html:hidden property="executionYear"/> 
			<!-- CurricularCourse -->
			<table>
				<tr>
					<td><bean:message key="label.givenCredits"/>: <html:text property="attributedCredits" size="2"/></td>
				</tr>
				<tr>
					<td><bean:message key="label.givenCreditsRemarks"/>:</td>
				</tr>
				<tr>
					<td>
						<html:textarea property="givenCreditsRemarks" cols="40" rows="5" />
					</td>
				</tr>
			</table>
			<table>
				<logic:present name="candidateEnrolments">		
					<h2><bean:message key="label.masterDegree.alreadyChosenCourses"/></h2>
	
					<logic:iterate id="candidateEnrolment" name="candidateEnrolments" indexId="index">
						<tr>
							<td>
	<%--							<html:multibox property='<%= "selection[" + index.intValue() + "]" %>'>
	--%>						<html:multibox property="selection" >	
	
									<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.idInternal"/>
								</html:multibox>
							</td>
							<td>
								<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
							</td>
							<td>
								<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoBranch.name"/> <br/>
							</td>
						</tr>
					</logic:iterate>
				</logic:present>
			</table>
	
			<br />
			<br />
			
			<div class="infoop">
				<span class="error"><strong>Nota:</strong></span>
				<br />&nbsp;&nbsp;&nbsp;&nbsp;O nome das Áreas Científicas aparecem por baixo do nome da disciplina e ligeiramente mais à frente. Nesse caso a opção é feita no quadrado à frente da área científica.
				<br />&nbsp;&nbsp;&nbsp;&nbsp;Caso a disciplina não pertença a nenhuma Área Científica, a opção para a sua selecção aparecerá imediatamente antes do seu nome. 
			</div>
	
			<br />
			<br />
	
	
			<h2><bean:message key="label.masterDegree.chooseNewCourses"/></h2>
			
			<table>
				<logic:iterate id="curricularCourseElem" name="curricularCourses"  indexId="index">
				   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
					<tr>
						<td>
						<bean:define id="offset" value="0"/>
							<bean:size id="ccsSize" name="curricularCourseElem" property="infoScopes" />
							<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="infoScopes" 
									  offset="0" length="1">
		        						<logic:equal name="curricularCourseScope" property="infoBranch.name"  value='<%= new String("") %>'>
		        							<bean:define id="offset" value="1"/>
		            						<html:multibox property="selection">
		                						<bean:write name="curricularCourseScope" property="idInternal"/>
		            						</html:multibox>
		        						</logic:equal>
		        						<logic:notEqual name="curricularCourseScope" property="infoBranch.name"  value='<%= new String("") %>'>
		        							<bean:define id="offset" value="0"/>
		        						</logic:notEqual>
		        						<strong><bean:write name="curricularCourseElem" property="name"/></strong><br />
							</logic:iterate>
							
						    <blockquote>
		            			<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="infoScopes" 
		            						    offset="<%= new String(offset) %>" length="<%= String.valueOf(ccsSize.intValue() - Integer.parseInt(offset)) %>">
		                						<html:multibox property="selection">
		                    						<bean:write name="curricularCourseScope" property="idInternal"/>
		                						</html:multibox>
		               							<bean:write name="curricularCourseScope" property="infoBranch.name"/> <br/>
								</logic:iterate>	
						    </blockquote>
						</td>
					</tr>
				</logic:iterate>
			</table>		
	<br />
			
	<html:submit value="Submeter" styleClass="inputbutton" property="ok"/>
	</html:form>
	</logic:present>
</logic:present>
