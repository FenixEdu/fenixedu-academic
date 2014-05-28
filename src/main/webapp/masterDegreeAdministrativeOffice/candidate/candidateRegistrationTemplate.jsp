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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

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
			               <img src="<%= request.getContextPath() %>/masterDegreeAdministrativeOffice/guide/images/istlogo.gif" alt="<bean:message key="istlogo" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
			              </td>
			              <td>
			                &nbsp;
			              </td>
			              <td>
			                <table border="0" width="100%" height="100%">
			                  <tr valign="top" align="left"> 
			                    <td>&nbsp;<b><bean:message key="institution.nameUpperCase" bundle="GLOBAL_RESOURCES"/></b><br/>
				                    &nbsp;<b>Curso: <bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/><br/>
				                    &nbsp;<b>Ano Lectivo: <bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/><br/>
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
	              <font size="2"> Av. Rovisco Pais, 1 1049-001 Lisboa Codex Telefone: 218417336 Fax: 218419531 Contribuinte Nº: 501507930</font>
	            </div>
	          </td>
	          </tr>
	        </table>
	     </td>	 
	  </tr>
	
	</table>
  </body>
</html>
