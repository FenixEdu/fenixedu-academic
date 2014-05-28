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
			                    <td>&nbsp;<b><bean:message bundle="GLOBAL_RESOURCES" key="institution.nameUpperCase" /></b><br/>
				                    &nbsp;<b><bean:message bundle="ADMIN_OFFICE_RESOURCES" key="label.degree" />: <bean:write name="degreeName" /><br/> 
				                    &nbsp;<b><bean:message bundle="ADMIN_OFFICE_RESOURCES" key="label.executionYear" />: <bean:write name="currentExecutionYear" /><br/>
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
				<h2><bean:message bundle="ADMIN_OFFICE_RESOURCES" key="label.acceptanceDispatch" /></h2>
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
							<h2><bean:message key="label.situation" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:message key="ADMITTED" bundle="CANDIDATE_RESOURCES"/></h2>
							<logic:present name="admittedCandidacies">							
								<logic:notEmpty name="admittedCandidacies">
									<table width="100%">
							        	<logic:iterate id="candidacy" name="admittedCandidacies" >
							        		<tr width="100%">
							        			<td><bean:write name="candidacy" property="candidacy.person.name"/></td>
							        			<td><bean:message name="candidacy" property="selectionSituation.name" bundle="CANDIDATE_RESOURCES"/></td>
							        			<td><bean:write name="candidacy" property="remarks"/></td>
							        		</tr>
							        	</logic:iterate> 									
									</table>
								</logic:notEmpty>
							</logic:present>							
						
							<h2><bean:message key="label.situation" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:message key="SUBSTITUTE" bundle="CANDIDATE_RESOURCES"/></h2>
							<logic:present name="substituteCandidacies">							
								<logic:notEmpty name="substituteCandidacies">
									<table width="100%">
							        	<logic:iterate id="candidacy" name="substituteCandidacies" >
							        		<tr width="100%">
							        			<td><bean:write name="candidacy" property="candidacy.person.name"/></td>
							        			<td><bean:message name="candidacy" property="selectionSituation.name" bundle="CANDIDATE_RESOURCES"/></td>
							        			<td><bean:write name="candidacy" property="remarks"/></td>
						        				<td><bean:write name="candidacy" property="order"/></td>
							        		</tr>
							        	</logic:iterate> 									
									</table>
								</logic:notEmpty>
							</logic:present>	

							<h2><bean:message key="label.situation" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:message key="NOT_ADMITTED" bundle="CANDIDATE_RESOURCES"/></h2>
							<logic:present name="notAdmittedCandidacies">							
								<logic:notEmpty name="notAdmittedCandidacies">
									<table width="100%">
							        	<logic:iterate id="candidacy" name="notAdmittedCandidacies" >
							        		<tr width="100%">
							        			<td><bean:write name="candidacy" property="candidacy.person.name"/></td>
							        			<td><bean:message name="candidacy" property="selectionSituation.name" bundle="CANDIDATE_RESOURCES"/></td>
							        			<td><bean:write name="candidacy" property="remarks"/></td>

							        		</tr>
							        	</logic:iterate> 									
									</table>
								</logic:notEmpty>
							</logic:present>																								
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
    	           <div align="center"><b><bean:message key="label.theCoordinator" bundle="ADMIN_OFFICE_RESOURCES"/></b> <br/>
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
