<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

   <span class="error"><html:errors/></span>

  <bean:define id="studentList" name="<%= SessionConstants.STUDENT_LIST %>" scope="request" />
  
  <bean:define id="link">/studentCurriculum.do?method=getCurriculum<%= "&" %>page=0<%= "&" %>studentID=</bean:define>
  	<p>
    <h3><%= ((List) studentList).size()%> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/></h3>        
    <% if (((List) studentList).size() != 0) { %>
        </p>
        <bean:message key="label.masterDegree.chooseOne"/><br><br><br>
   	<% } %>  
    
 	<logic:iterate id="student" name="studentList">
    	<bean:define id="studentLink">
    		<bean:write name="link"/><bean:write name="student" property="idInternal"/>
    	</bean:define>
    	<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>
			<bean:write name="student" property="infoPerson.nome"/>
        </html:link>
        <br>
	</logic:iterate>
    	
