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
  <body onload="print()">
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
			                    <td>&nbsp;<b><bean:message bundle="GLOBAL_RESOURCES" key="institution.nameUpperCase" /></b><br/>
				                    &nbsp;<b><bean:write name="candidacy" property="executionDegree.degreeCurricularPlan.degree.presentationName" /><br/> 
				                    &nbsp;<b><bean:write name="candidacy" property="executionDegree.executionYear.year" /><br/>
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
				<h2><bean:message key="label.registrationInformation" bundle="ADMIN_OFFICE_RESOURCES"/></h2>
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
										<strong><bean:message key="label.name" bundle="ADMIN_OFFICE_RESOURCES"/></strong>
								    </td>
									<td width"80%" >      
										<bean:write name="candidacy" property="person.name" />
								    </td>
							    </tr>
								<tr>      
									<td>      
										<strong><bean:message key="label.candidacy.number" bundle="ADMIN_OFFICE_RESOURCES"/></strong>
								    </td>
									<td>      
										<bean:write name="candidacy" property="number" />
								    </td>
							    </tr>
								<tr>      
									<td>      
										<strong><bean:message key="label.studentNumber" bundle="ADMIN_OFFICE_RESOURCES"/></strong>
								    </td>
									<td>      
										<bean:write name="candidacy" property="registration.student.number"/>
								    </td>
							    </tr>
							    <tr>      
									<td>      
										<strong><bean:message key="label.username" bundle="ADMIN_OFFICE_RESOURCES"/></strong>
								    </td>
									<td>      
										<bean:write name="candidacy" property="person.istUsername"/>
								    </td>
							    </tr>
						    </table>
						</td>    
					</tr>
				</table>
			</td>
		</tr>


        <tr valign="bottom">	 
	 	  <td>
		 	<table align="center" width="100%" valign="bottom">
		      <tr>
	          <td colspan="2" valign="bottom" >
	            <div align="center">
	              <font size="2"> <bean:message bundle="GLOBAL_RESOURCES" key="footer.computer.processedDocument"/></font> 
	            </div>
	            <hr size="1" color="#000000" width="100%">
	            <div align="center">
	              <font size="2"> <bean:message bundle="GLOBAL_RESOURCES" key="institution.address"/></font>
	            </div>
	          </td>
	          </tr>
	        </table>
	     </td>	 
	  </tr>
	
	</table>
  </body>
</html>
