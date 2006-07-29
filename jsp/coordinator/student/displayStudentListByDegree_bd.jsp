<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
  <bean:define id="studentList" name="<%= SessionConstants.STUDENT_LIST %>" scope="request" />
  <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
  <bean:define id="link">/studentCurriculum.do?method=getCurriculumForCoordinator<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
  	<p>
    <span class="emphasis"><%= ((List) studentList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/>       
    <% if (((List) studentList).size() != 0) { %>
        </p>
        <bean:message key="label.masterDegree.chooseOne"/><br/><br/><br/>
        <logic:equal name="viewPhoto" value="true">
	        <html:link page="<%= "/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0&viewPhoto=false&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
    	    	<bean:message key="label.notViewPhoto"/>
        	</html:link>
        </logic:equal>
        <logic:notEqual name="viewPhoto" value="true">
	        <html:link page="<%= "/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0&viewPhoto=true&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
    	    	<bean:message key="label.viewPhoto"/>
        	</html:link>
        </logic:notEqual>
    
        <table width="70%">
        	<tr>
        		<logic:equal name="viewPhoto" value="true">
					<th class="listClasses-header"><bean:message key="label.photo" /></th>
			 	</logic:equal>
    			<th class="listClasses-header"><bean:message key="label.candidate.number" /></th>
    			<th class="listClasses-header"><bean:message key="label.person.name" /></th>
    			<th class="listClasses-header"><bean:message key="label.masterDegree.gratuity.SCPlan" /></th>
    		</tr>
     	<logic:iterate id="studentCP" name="studentList">
        	<bean:define id="studentLink">
        		<bean:write name="link"/><bean:write name="studentCP" property="idInternal"/>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID%>
        	</bean:define>
        <tr>
	        <logic:equal name="viewPhoto" value="true">
				<th class="listClasses-header">
					<bean:define id="personID" name="studentCP" property="infoStudent.infoPerson.idInternal"/>
					<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
			   </th>
			</logic:equal> 
        	<td class="listClasses">
        	<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>
    			<bean:write name="studentCP" property="infoStudent.number"/>
    		</html:link>
            </td>
            <td class="listClasses">
    	        <bean:write name="studentCP" property="infoStudent.infoPerson.nome"/>
    	    </td>    	    
            <td class="listClasses">
    	        <bean:message name="studentCP" property="currentState.name" bundle="ENUMERATION_RESOURCES"/>
    	    </td>
        </logic:iterate>
      	</table>    	
   	<% } %>  
