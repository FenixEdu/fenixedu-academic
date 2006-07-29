<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

   <span class="error"><html:errors/></span>

  <bean:define id="studentList" name="<%= SessionConstants.STUDENT_LIST %>" scope="request" />
  
  <logic:present name="infoExecutionDegree">
  	<table>
  		<tr>
	  		<td>
				<strong><bean:message key="label.degree" /></strong>
	  		</td>
	  		<td>
	  			<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>

	  		</td>
  		</tr>
  		<tr>
	  		<td>
	  			<strong><bean:message key="label.masterDegree.administrativeOffice.executionYear" /></strong>
	  		</td>
	  		<td>
	  			<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>
	  		</td>
  		</tr>
  	</table>
  </logic:present>
  
  
  <bean:define id="link">/studentCurriculum.do?method=getCurriculum<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
  	<p>
    <h3><%= ((List) studentList).size()%> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/></h3>        
    <% if (((List) studentList).size() != 0) { %>
        </p>
        <bean:message key="label.masterDegree.chooseOne"/><br/><br/><br/>
    
        <table>
        	<tr>
    			<th class="listClasses-header"><bean:message key="label.candidate.number" /></th>
    			<th class="listClasses-header"><bean:message key="label.person.name" /></th>
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
    	
    		
