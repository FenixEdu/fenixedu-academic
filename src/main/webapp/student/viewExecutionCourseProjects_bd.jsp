<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<logic:present name="infoGroupPropertiesList">

<h2><bean:message key="title.ExecutionCourseProjects.short"/></h2>

	<span class="error"><!-- Error messages go here --><html:errors /></span> 	
			
	<logic:empty name="infoGroupPropertiesList">
		<em><bean:message key="title.student.portalTitle"/></em>
		<h2><bean:message key="title.ExecutionCourseProjects.short"/></h2>
		<p class="mtop15"><span class="warning0"><bean:message key="message.infoGroupPropertiesList.not.available" /></span></p>
	</logic:empty>
	
	<logic:notEmpty name="infoGroupPropertiesList">
	<div class="infoop2">
		<p class="mvert05">Disciplina: <bean:write name="infoExecutionCourse" property="nome"/></p>
	</div>
	
	<p class="mtop15 mbottom05"><bean:message key="label.student.viewExecutionCourseProjects.description" /></p>

	<table class="tstyle4" style="text-align: left;">
         <tr>
			<th width="20%"><bean:message key="label.projectTable.project" />
			<th width="30%"><bean:message key="label.projectTable.description" />
			<th width="20%"><bean:message key="label.projectTable.properties" />
			<th width="20%" ><bean:message key="label.projectTable.newProjectProposal" />
		</tr>
		
            <logic:iterate id="infoGroupProperties" name="infoGroupPropertiesList">
                <tr>   
                             	
                    <td class="acenter">                                                              
                        <html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode") %>" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="externalId">
							<b><bean:write name="infoGroupProperties" property="name"/></b>
						</html:link>						
                    </td>
                    
                    <td class="acenter">	                   
	             		<logic:notEmpty name="infoGroupProperties" property="projectDescription">
	                     	<bean:write name="infoGroupProperties" property="projectDescription" filter="false"/>
	                	</logic:notEmpty>
	                	
	             		<logic:empty name="infoGroupProperties" property="projectDescription">
	                     	<bean:message key="message.project.wihtout.description"/>
	                	</logic:empty>
                	</td>
                    
                    <td class="acenter">
                	 
                	 	 <logic:notEmpty name="infoGroupProperties" property="maximumCapacity">
	                		 <p style="margin: 6px 0; padding: 0;"> <abbr title="<bean:message key="label.projectTable.MaximumCapacity.title" />"><bean:message key="label.student.viewExecutionCourseProjects.MaximumCapacity"/></abbr>: <bean:write name="infoGroupProperties" property="maximumCapacity"/> <bean:message key="label.students.lowercase" /></p>
	                	 </logic:notEmpty>
	                	 
	                	 <logic:notEmpty name="infoGroupProperties" property="idealCapacity">
	                		 <p style="margin: 6px 0; padding: 0;"> <abbr title="<bean:message key="label.projectTable.IdealCapacity.title" />"> <bean:message key="label.student.viewExecutionCourseProjects.IdealCapacity"/></abbr>: <bean:write name="infoGroupProperties" property="idealCapacity"/> <bean:message key="label.students.lowercase" /></p>
	                	 </logic:notEmpty>
	                	 
	                	 <logic:notEmpty name="infoGroupProperties" property="minimumCapacity">
	                		 <p style="margin: 6px 0; padding: 0;"> <abbr title="<bean:message key="label.projectTable.MinimumCapacity.title" />"> <bean:message key="label.student.viewExecutionCourseProjects.MinimumCapacity"/></abbr>: <bean:write name="infoGroupProperties" property="minimumCapacity"/> <bean:message key="label.students.lowercase" /></p>
	                	 </logic:notEmpty>
	                	 
	                	 <logic:notEmpty name="infoGroupProperties" property="groupMaximumNumber">
	                		 <p style="margin: 6px 0; padding: 0;"> <abbr title="<bean:message key="label.projectTable.GroupMaximumNumber.title" />"> <bean:message key="label.student.viewExecutionCourseProjects.GroupMaximumNumber"/></abbr>: <bean:write name="infoGroupProperties" property="groupMaximumNumber"/></p>
	                   	 </logic:notEmpty>
	                   	 
	                   	 <p style="margin: 6px 0; padding: 0;"><b><bean:message key="label.student.viewExecutionCourseProjects.GroupEnrolmentPolicy"/>:</b>
		                   	 <%if((((net.sourceforge.fenixedu.dataTransferObject.InfoGrouping) infoGroupProperties).getEnrolmentPolicy()).getType().intValue()==1){%>
		                   	 <bean:message key="label.atomic"/>
		                   	 <%}else{%>
		                   	 <bean:message key="label.individual"/>
		                   	 <%}%>
	                   	 </p>
               		</td>
                	
                	<td class="acenter">
                		<bean:size id="count" name="infoGroupProperties" property="infoExportGroupings"/>
                		<logic:greaterThan name="count" value="1">
            		    <logic:iterate id="infoGroupPropertiesExecutionCourseElement" name="infoGroupProperties" property="infoExportGroupings" >
                		<bean:define id="infoExecutionCourse" name="infoGroupPropertiesExecutionCourseElement" property="infoExecutionCourse" />
							<bean:write name="infoExecutionCourse" property="nome"/><br/>
                    	 </logic:iterate>
                    		</logic:greaterThan>
						<logic:equal name="count" value="1">
							<bean:message key="message.project.wihtout.coavaliation"/>
                    	</logic:equal>
                    </td>
     
                </tr>

            </logic:iterate>
</table>

<div id="legend" style="margin-top: 1em;">
	<p style="margin: 0; padding: 0;"><strong><bean:message key="label.projectTable.properties"/>:</strong></p>
	<p style="margin: 0; padding: 0;"><em><bean:message key="label.student.viewExecutionCourseProjects.MaximumCapacity"/></em> - <bean:message key="label.projectTable.MaximumCapacity.title" /></p>
	<p style="margin: 0; padding: 0;"><em><bean:message key="label.student.viewExecutionCourseProjects.IdealCapacity"/></em> - <bean:message key="label.projectTable.IdealCapacity.title" /></p>
	<p style="margin: 0; padding: 0;"><em><bean:message key="label.student.viewExecutionCourseProjects.MinimumCapacity"/></em> - <bean:message key="label.projectTable.MinimumCapacity.title" /></p>
	<p style="margin: 0; padding: 0;"><em><bean:message key="label.student.viewExecutionCourseProjects.GroupMaximumNumber"/></em> - <bean:message key="label.projectTable.GroupMaximumNumber.title" /></p>
	<p style="margin: 0; padding: 0;"><em><bean:message key="label.student.viewExecutionCourseProjects.GroupEnrolmentPolicy"/></em> - <bean:message key="label.projectTable.GroupEnrolmentPolicy.title" /></p>
</div>



<div class="infoop mtop2">
	<ul>
		<li><strong><bean:message key="label.teacher.viewExecutionCourseProjects.atomicPolicy" bundle="APPLICATION_RESOURCES" />:</strong> <bean:message key="label.teacher.viewExecutionCourseProjects.atomicDescription" bundle="APPLICATION_RESOURCES" />.</li>
		<li><strong><bean:message key="label.teacher.viewExecutionCourseProjects.individualPolicy" bundle="APPLICATION_RESOURCES" />:</strong> <bean:message key="label.teacher.viewExecutionCourseProjects.individualDescription" bundle="APPLICATION_RESOURCES" />.</li>
	</ul>
</div>


</logic:notEmpty>
</logic:present>


<logic:notPresent name="infoGroupPropertiesList">
<p>
	<span class="warning0"><bean:message key="message.infoGroupPropertiesList.not.available" /></span>
</p>
</logic:notPresent>