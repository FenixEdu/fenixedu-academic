<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
  <logic:present name="infoExecutionDegree">
  	<table>
  		<tr>
	  		<td width="100px"><strong><bean:message key="label.degree" />:</strong></td>
	  		<td><bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/></td>
  		</tr>
  		<tr>
	  		<td width="100px"><strong><bean:message key="label.masterDegree.administrativeOffice.executionYear" />:</strong></td>
	  		<td><bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/></td>
  		</tr>
  	</table>
  </logic:present>
  <bean:define id="studentList" name="<%= SessionConstants.STUDENT_LIST %>" scope="request" />
  <bean:define id="link">/studentCurriculum.do?method=getCurriculum<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
  	<p>
    <span class="emphasis"><%= ((List) studentList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/>       
    <% if (((List) studentList).size() != 0) { %>
        </p>
        <bean:message key="label.masterDegree.chooseOne"/><br><br><br>
    
        <table width="70%">
        	<tr>
    			<td class="listClasses-header"><bean:message key="label.candidate.number" /></td>
    			<td class="listClasses-header"><bean:message key="label.person.name" /></td>
    		</tr>
     	<logic:iterate id="studentCP" name="studentList">
        	<bean:define id="studentLink">
        		<bean:write name="link"/><bean:write name="studentCP" property="idInternal"/>
        	</bean:define>
        <tr>
        	<td class="listClasses">
        	<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>
    			<bean:write name="studentCP" property="infoStudent.number"/>
    		</html:link>
            </td>
            <td class="listClasses">
    	        <bean:write name="studentCP" property="infoStudent.infoPerson.nome"/>
    	    </td>
        </logic:iterate>
      	</table>    	
   	<% } %>  
