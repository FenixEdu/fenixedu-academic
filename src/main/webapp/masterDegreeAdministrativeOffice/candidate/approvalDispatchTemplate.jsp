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
				<h2>Despacho de Aceitação</h2>
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
							<logic:iterate id="group" name="infoGroup" >
								<h2>Situação: <bean:write name="group" property="situationName"/></h2>
						  		<table width="100%">
						        	<logic:iterate id="candidate" name="group" property="candidates">
						        		<tr width="100%">
						        			<td><bean:write name="candidate" property="candidateName"/></td>
						        			<td><bean:write name="candidate" property="situationName"/></td>
						        			<td><bean:write name="candidate" property="remarks"/></td>
						        		    <logic:present name="candidate" property="orderPosition">
						        				<td><bean:write name="candidate" property="orderPosition"/></td>
						        			</logic:present>
						        		</tr>
						        	</logic:iterate> 
						   		</table>
							</logic:iterate> 
				
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
