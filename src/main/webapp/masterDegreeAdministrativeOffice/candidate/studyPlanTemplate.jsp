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
			                    <td>&nbsp;<b><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%></b><br/>
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
	    <tr align="right">
			<td>	
				<h2><bean:message key="label.masterDegree.studyPlan"/></h2>
			</td>
		</tr>
		
		<tr>
			<td>
				<table width="100%" border="0">
		          <tr>
		            <td width="30%"><strong>Processo de:</strong></td>
		            <td width="70%">&nbsp;</td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
		            <td> <bean:write name="infoMasterDegreeCandidate" property="infoPerson.nome"/> </td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
		            <td> <bean:write name="infoMasterDegreeCandidate" property="candidateNumber"/> </td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.masterDegree.administrativeOffice.specialization"/> </td>
		            <td> <bean:message name="infoMasterDegreeCandidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> </td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.givenCredits"/> </td>
		            <td> <bean:write name="infoMasterDegreeCandidate" property="givenCredits"/> </td>
		          </tr>
		          <tr>
		            <td> </td>
		            <td> <bean:write name="infoMasterDegreeCandidate" property="givenCreditsRemarks"/> </td>
		          </tr>
		          
		        </table>
			</td>
		</tr>
		


		<tr valign="top">
			<td>
				<table width="100%" border="0">
				 <tr>
				 <td>
			      <table width="100%" border="0">
					<tr>      
						<td>      
							<table>
						           	<tr>
						           		<td align="center">
					            			<strong><bean:message key="property.course"/></strong>
					            		</td>
						           		<td align="center">
						           			&nbsp;
						           			<%-- RESOLVER 
					            			<strong><bean:message key="property.curricularCourse.branch"/></strong>
					            			--%>
					            		</td>	
										<td align="center">
					            			<strong><bean:message key="message.manager.credits"/></strong>
					            		</td>	
									</tr>
					           	<logic:iterate id="candidateEnrolment" name="candidateEnrolments" >	
						           	<tr>
						           		<td>
					            			<bean:write name="candidateEnrolment" property="infoCurricularCourse.name"/>
					            		</td>
						           		<td>
						           			&nbsp;
							           		<%-- RESOLVER
					            			<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoBranch.name"/>
						            		--%>
					            		</td>
						           		<td>
					            			<bean:write name="candidateEnrolment" property="infoCurricularCourse.credits"/>
										</td>		
									</tr>
						       	</logic:iterate>
						       	<tr>
						       		<td>
						       			&nbsp;
						       		</td>
						       		<td>
						       			<strong><bean:message key="label.totalCredits"/></strong>
						       		</td>
						       		<td>
						       			<strong><bean:write name="totalCredits" /></strong>
						       		</td>
						       	</tr>
						    </table>
					    </td>
				    </tr>
			    </table>
			</td>    
		</tr>




		<tr valign="bottom">
		  <table width="100%">
		    <tr>
    	        <td width="20%">&nbsp;</td>
    	        <td width="40%">&nbsp;</td>
    	         <td width="90%" colspan="2" valign="bottom">
    	           &nbsp;<div align="center">&nbsp;</div>
    	           <div align="center">&nbsp;</div>
    	           <div align="center"><b>O Coordenador</b> <br/>
    	            <br/>
    	            <br/>
    	           </div>
    	          <hr align="center" width="300" size="1">
    	         </td>
    	     </tr>
	      </table>
        </tr>
		
	</table>
    </body>
</html>
