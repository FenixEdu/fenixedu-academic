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
                <bean:message key="property.turno"/> <bean:write name="infoTurno" property="nome" scope="session" filter="true"/>
            </b>
        </font>
    </center>
    <br/>
    <br/>
    <center> <b> <bean:message key="listAulas.OfTurno"/> </b> </center>
    <br/>
    <logic:present name="infoAulasDeTurno" scope="session">
        <table align="center" border=1 cellpadding='5'>
            <tr align='center'>
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
    </logic:present>
    <logic:notPresent name="infoAulasDeTurno" scope="session">
        <table align="center" border='1' cellpadding='15'>
            <tr align="center">
                <td>
                    <font color='red'> <bean:message key="errors.existAulas"/> </font>
                </td>
            </tr>
        </table>
    </logic:notPresent>
</body>