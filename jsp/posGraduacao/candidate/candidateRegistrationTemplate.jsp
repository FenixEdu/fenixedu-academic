<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
  <body>
    <table width="100%" height="100%" border="0">
	    <tr height="30">
    	    <td>
			     <table width="100%" border="0" valign="top">
			      <tr> 
			        <td height="100" colspan="2">
			          <table border="0" width="100%" height="100" align="center" cellpadding="0" cellspacing="0">
			            <tr> 
			              <td width="50" height="100">
			               <img src="<%= request.getContextPath() %>/posGraduacao/guide/images/istlogo.gif" alt="<bean:message key="istlogo" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
			              </td>
			              <td>
			                &nbsp;
			              </td>
			              <td>
			                <table border="0" width="100%" height="100%">
			                  <tr valign="top" align="left"> 
			                    <td>&nbsp;<b>INSTITUTO SUPERIOR T�CNICO</b><br>
				                    &nbsp;<b>Curso: <bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/><br>
				                    &nbsp;<b>Ano Lectivo: <bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/><br>
			                      <hr size="1">
			                    </td>
			                  </tr>
			                </table>
			              </td>
			            </tr>
			          </table>
			        </td>
			      </tr>
				</table>
			</td>
		</tr>
	    <tr valign="top" align="left">
			<td>	
				<h2><bean:message key="label.masterDegree.administrativeOffice.applicationInformation" /></h2>
			</td>
		</tr>
		
		<tr valign="top">
			<td>
				<table width="100%" border="0">
					 <tr>
						 <td>
						      <table width="100%" border="0">
								<tr>      
									<td width="20%">      
										<strong><bean:message key="label.masterDegree.administrativeOffice.name" /></strong>
								    </td>
									<td width"80%" >      
										<bean:write name="infoCandidateRegistration" property="infoMasterDegreeCandidate.infoPerson.nome" />
								    </td>
							    </tr>
								<tr>      
									<td>      
										<strong><bean:message key="label.masterDegree.administrativeOffice.number" /></strong>
								    </td>
									<td>      
										<bean:write name="infoCandidateRegistration" property="infoMasterDegreeCandidate.candidateNumber" />
								    </td>
							    </tr>
								<tr>      
									<td>      
										<strong><bean:message key="label.masterDegree.administrativeOffice.specialization" /></strong>
								    </td>
									<td>      
										<bean:message name="infoCandidateRegistration" property="infoMasterDegreeCandidate.specialization.name"  bundle="ENUMERATION_RESOURCES"/>
								    </td>
							    </tr>
						    </table>
						</td>    
					</tr>
				</table>
			</td>
		</tr>

	    <tr valign="top" align="left">
			<td valign="top">	
				<h2><bean:message key="label.masterDegree.administrativeOffice.masterDegreeGraduationInformation" /></h2>
			</td>
		</tr>


		<tr valign="top">
			<td>
				<table width="100%" border="0">
					 <tr>
						 <td>
						      <table width="100%" border="0">
								<tr>
									<td width="20%">      
										<strong><bean:message key="label.masterDegree.administrativeOffice.number" /></strong>
								    </td>
									<td width"80%" >      
										<bean:write name="infoCandidateRegistration" property="infoStudentCurricularPlan.infoStudent.number" />
								    </td>
							    </tr>
								<tr>      
									<td>      
										<strong><bean:message key="label.masterDegree.administrativeOffice.specialization" /></strong>
								    </td>
									<td>      
										<bean:message name="infoCandidateRegistration" property="infoStudentCurricularPlan.specialization.name"  bundle="ENUMERATION_RESOURCES"/>
								    </td>
							    </tr>
								<tr>      
									<td>      
										<strong><bean:message key="label.candidate.username" /></strong>
								    </td>
									<td>      
										<bean:write name="infoCandidateRegistration" property="infoMasterDegreeCandidate.infoPerson.username" />
								    </td>
							    </tr>
								<tr>      
									<td>      
										<strong><bean:message key="label.givenCredits" /></strong>
								    </td>
									<td>      
										<bean:write name="infoCandidateRegistration" property="infoStudentCurricularPlan.givenCredits" />
								    </td>
							    </tr>
						    </table>
						</td>    
					</tr>
				</table>
			</td>
		</tr>

		<logic:present name="infoCandidateRegistration" property="enrolments">
		    <tr valign="top" align="left">
				<td valign="top">	
					<h2><bean:message key="label.masterDegree.enrollment" /></h2>
				</td>
			</tr>
	
			
			<tr>
				<td>
					<table width="100%" border="0">
						<tr>
			           		<td align="center">
		            			<strong><bean:message key="property.course"/></strong>
		            		</td>
			           		<td align="center">
		            			<strong><bean:message key="property.curricularCourse.branch"/></strong>
		            		</td>
						</tr>
						<logic:iterate id="enrolment" name="infoCandidateRegistration" property="enrolments">
							<tr>
				           		<td>
			            			<bean:write name="enrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
			            		</td>
				           		<td>
			            			<bean:write name="enrolment" property="infoCurricularCourseScope.infoBranch.name"/>
			            		</td>							
			            	</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
		</logic:present>

        <tr valign="bottom">	 
	 	  <td>
		 	<table align="center" width="100%" valign="bottom">
		      <tr>
	          <td colspan="2" valign="bottom" >
	            <div align="center">
	              <font size="2"> Documento processado por computador. </font> 
	            </div>
	            <hr size="1" color="#000000" width="100%">
	            <div align="center">
	              <font size="2"> Av. Rovisco Pais, 1 1049-001 Lisboa Codex Telefone: 218417336 Fax: 218419531 Contribuinte N�: 501507930</font>
	            </div>
	          </td>
	          </tr>
	        </table>
	     </td>	 
	  </tr>
	
	</table>
  </body>
</html>
