<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

  <bean:define id="studentList" name="<%= PresentationConstants.STUDENT_LIST %>" scope="request" />
  <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
  <bean:define id="link">/studentCurriculum.do?method=getCurriculumForCoordinator<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
	<p>
		<span class="emphasis"><%= ((List) studentList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/>       
		<% if (((List) studentList).size() != 0) { %>
	</p>

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
    
        <table class="tstyle4">
        	<tr>
        		<logic:equal name="viewPhoto" value="true">
					<th><bean:message key="label.photo" /></th>
			 	</logic:equal>
    			<th><bean:message key="label.candidate.number" /></th>
    			<th><bean:message key="label.person.name" /></th>
    			<th><bean:message key="label.masterDegree.gratuity.SCPlan" /></th>
    		</tr>
     	<logic:iterate id="studentCP" name="studentList">
        	<bean:define id="studentLink">
        		<bean:write name="link"/><bean:write name="studentCP" property="externalId"/>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID%>
        	</bean:define>
        <tr>
	        <logic:equal name="viewPhoto" value="true">
				<th>
					<bean:define id="personID" name="studentCP" property="infoStudent.infoPerson.externalId"/>
					<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
			   </th>
			</logic:equal> 
        	<td>
        	<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>
    			<bean:write name="studentCP" property="infoStudent.number"/>
    		</html:link>
            </td>
            <td>
    	        <bean:write name="studentCP" property="infoStudent.infoPerson.nome"/>
    	    </td>    	    
            <td>
    	        <bean:message name="studentCP" property="currentState.name" bundle="ENUMERATION_RESOURCES"/>
    	    </td>
        </logic:iterate>
      	</table>    	
   	<% } %>  
