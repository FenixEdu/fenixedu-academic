<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DataBeans.InfoLesson" %>

     	<% ArrayList iA = (ArrayList) session.getAttribute("listaAulas"); %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	          <tr>
	            <td bgcolor="#FFFFFF" class="infoselected"><p>A licenciatura seleccionada
	              &eacute;:</p>
				  <strong><jsp:include page="context.jsp"/></strong>
	            </td>
	          </tr>
	    </table>
	    <br />
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.manage.aulas"/> </b> </font></center>
        <br/>
        <br/>
        <html:errors/>
        <center> <b> <bean:message key="listAulas.existing"/> </b> </center>
        <html:form action="/manipularAulasForm">
            <logic:present name="listaAulas" scope="session">
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
                    <logic:iterate id="elem" name="listaAulas">
                       <% Integer iH = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.MINUTE)); %>
                       <% String appendStartMinute = ""; %>
                       <% String appendEndMinute = ""; %>
                       <% if (iM.intValue() == 0) { %>
                       <% 	appendStartMinute = "0"; } %>
                       <% if (fM.intValue() == 0) { %>
                       <% 	appendEndMinute = "0"; } %>
                       <tr align="center">
                            <td>
                            	<html:radio property="indexAula" value="<%= (new Integer(i)).toString() %>"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="diaSemana" />
                            </td>
                            <td>
                            	<%= iH.toString()%> : <%= iM.toString() + appendStartMinute %>
                            </td>
                            <td>
                            	<%= fH.toString()%> : <%= fM.toString() + appendEndMinute %>
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
                            <bean:message key="label.edit.Aula"/>
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:submit property="operation">
                            <bean:message key="label.delete.Aula"/>
                        </html:submit>
                    </td>
                </tr>
            </table>

            </logic:present>
            <logic:notPresent name="listaAulas" scope="session">
                <table align="center" border="1" cellpadding='5'>
                    <tr align="center">
                        <td>
                            <font color='red'> <bean:message key="errors.existAulas"/> </font>
                        </td>
                    </tr>
                </table>
                
            <br/>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit property="operation">
                            <bean:message key="label.back"/>
                        </html:submit>
                    </td>
                </tr>
            </table>
                
            </logic:notPresent>

        </html:form>
