<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

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
			               <img src="<%= request.getContextPath() %>/posGraduacao/guide/images/istlogo.gif" alt="<bean:message key="istlogo" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
			              </td>
			              <td>
			                &nbsp;
			              </td>
			              <td>
			                <table border="0" width="100%" height="100%">
			                  <tr valign="top" align="left"> 
			                    <td>&nbsp;<b><bean:message bundle="GLOBAL_RESOURCES" key="institution.nameUpperCase" /></b><br/>
				                    &nbsp;<b><bean:message name="candidacy" property="executionDegree.degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/> - <bean:write name="candidacy" property="executionDegree.degreeCurricularPlan.degree.nome" /><br/> 
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
