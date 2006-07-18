<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
	   	<% ArrayList iA = (ArrayList) request.getAttribute("infoAulasDeTurno"); %>
 <center>
 	<b>
		<html:link page="<%= "/listClasses.do?method=showClasses&amp;"
			+ SessionConstants.SHIFT_OID
			+ "="
		    + pageContext.findAttribute("shiftOID")
		    + "&amp;"
			+ SessionConstants.EXECUTION_PERIOD_OID
		  	+ "="
		  	+ pageContext.findAttribute("executionPeriodOID")
		  	+ "&amp;"
		  	+ SessionConstants.CURRICULAR_YEAR_OID
			+ "="
		  	+ pageContext.findAttribute("curricularYearOID")
		  	+ "&amp;"
		  	+ SessionConstants.EXECUTION_COURSE_OID
			+ "="
		  	+ pageContext.findAttribute("executionCourseOID")
		  	+ "&amp;"
			+ SessionConstants.EXECUTION_DEGREE_OID
		  	+ "="
			+ pageContext.findAttribute("executionDegreeOID") %>">
 			 <bean:message key="link.add.shift.classes"/>
 		</html:link>
 	</b>
 </center>
<br/>
<br/>
     <center>
            <span class="error">
                    <bean:message key="title.editAulaOfTurnoXPTO"/>
                    <bean:write name="shift" property="nome" scope="request" filter="true"/>
            </span>
        </center>
        <br/>
        <html:errors/>
        <html:form action="/editarAulasDeTurnoForm">

<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
			 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

            <center> <b> <bean:message key="listAulas.added"/> </b> </center>
            <br/>
            <logic:present name="infoAulasDeTurno" scope="request">
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
                            	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.indexAula" property="indexAula" value="<%= (new Integer(i)).toString() %>"/>
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
                            	<logic:notEmpty name="elem" property="infoSala.nome">
	                                <bean:write name="elem" property="infoSala.nome"/>
	                            </logic:notEmpty>    
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
                        	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation">
                            	<bean:message key="label.add.Aulas"/>
                       	 </html:submit>
        	            </td>
         	           <td width="20"> </td>
           	  	       <td>
               		   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation">
                        	<bean:message key="label.remove.Aula"/>
                        </html:submit>
                    </td>
                </tr>
            	</table>
            	</logic:present>
            	<logic:notPresent name="infoAulasDeTurno" scope="request">
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

                        		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation">
                            		<bean:message key="label.add.Aulas"/>
                       		 </html:submit>
        	            	</td>
		                </tr>
        	    	</table>
	            </logic:notPresent>
    	    </html:form>