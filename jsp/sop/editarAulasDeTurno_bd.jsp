<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DataBeans.InfoLesson" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
	   	<% ArrayList iA = (ArrayList) session.getAttribute("infoAulasDeTurno"); %>
        <center>
            <font color='#034D7A' size='5'>
                <b>
                    <bean:message key="title.editAulaOfTurnoXPTO"/>
                    <bean:write name="infoTurno" property="nome" scope="session" filter="true"/>
                </b>
            </font>
        </center>
        <br/>
        <html:errors/>
        <html:form action="/editarAulasDeTurnoForm">
            <center> <b> <bean:message key="listAulas.added"/> </b> </center>
            <br/>
            <logic:present name="infoAulasDeTurno" scope="session">
                <table align="center" border=1 cellpadding='5'>
                    <tr align='center'>
                        <th>
                        </th>
                        <th>
                            <bean:message key="property.aula.weekDay"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.time.begining"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.time.end"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.type"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.sala"/>
                        </th>
                    </tr>
		            <% int i = 0; %>
                    <logic:iterate id="elem" name="infoAulasDeTurno">
                       <% Integer iH = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.MINUTE)); %>
                       <tr align="center">
                            <td>
                            	<html:radio property="indexAula" value="<%= (new Integer(i)).toString() %>"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="diaSemana" />
                            </td>
                            <td>
                            	<%= iH.toString()%> : <%= iM.toString()%>
                            </td>
                            <td>
                            	<%= fH.toString()%> : <%= fM.toString()%>
                            </td>
                            <td>
                            	<%= ((InfoLesson) iA.get(i)).getTipo().toString()%>
                            </td>
                            <td>
                                <bean:write name="elem" property="infoSala.nome"/>
                            </td>
                        </tr>
                        <% i++; %>
                    </logic:iterate>           
                </table>
		        <br/>
         	    <br/>
            	<table align="center">
                	<tr align="center">
                    	<td>
                        	<html:submit property="operation">
                            	<bean:message key="label.add.Aulas"/>
                       	 </html:submit>
        	            </td>
         	           <td width="20"> </td>
           	  	       <td>
               		   	<html:submit property="operation">
                        	<bean:message key="label.remove.Aula"/>
                        </html:submit>
                    </td>
                </tr>
            	</table>
            	</logic:present>
            	<logic:notPresent name="infoAulasDeTurno" scope="session">
                	<table align="center" border='1' cellpadding='5'>
                    	<tr align="center">
                        	<td width='250'>
                            	<font color='red'> <bean:message key="errors.existAulasInTurno"/> </font>
                        	</td>
                    	</tr>
                	</table>
                	<br/>
                	<br/>
            		<table align="center">
                		<tr align="center">
                    		<td>
                        		<html:submit property="operation">
                            		<bean:message key="label.add.Aulas"/>
                       		 </html:submit>
        	            	</td>
		                </tr>
        	    	</table>
	            </logic:notPresent>
    	    </html:form>