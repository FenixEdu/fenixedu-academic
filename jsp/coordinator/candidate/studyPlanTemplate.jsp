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
			                    <td>&nbsp;<b>INSTITUTO SUPERIOR TÉCNICO</b><br>
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
    	           <div align="center"><b>O Coordenador</b> <br>
    	            <br>
    	            <br>
    	           </div>
    	          <hr align="center" width="300" size="1">
    	         </td>
    	     </tr>
	      </table>
        </tr>
		
	</table>
    </body>
</html>
