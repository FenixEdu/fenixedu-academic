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
				<h2>Despacho de Aceitaï¿½ï¿½o</h2>
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
