<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="infoGroupPropertiesList">
<style>
td.listClasses p { margin: 6px 0; padding: 0; }
#legend { margin-top: 2em; }
#legend p { margin: 0; padding: 0; }
</style>


<h2><bean:message key="title.ExecutionCourseProjects.short"/></h2>

	<span class="error"><!-- Error messages go here --><html:errors /></span> 	
			
	<logic:empty name="infoGroupPropertiesList">
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	</logic:empty>
	<logic:notEmpty name="infoGroupPropertiesList">
	<div class="infoop3">
		<p><strong>Disciplina:</strong> <bean:write name="infoExecutionCourse" property="nome"/></p>
	</div>
	
	<p><bean:message key="label.student.viewExecutionCourseProjects.description" /></p>

	<table class="style1" style="text-align: left;">
         <tr>
			<th class="listClasses-header" width="20%"><bean:message key="label.projectTable.project" />
			</th>
			<th class="listClasses-header" width="30%"><bean:message key="label.projectTable.description" />
			</th>
			<th class="listClasses-header" width="20%"><bean:message key="label.projectTable.properties" />
			</th>		
			</td>
			<th class="listClasses-header" width="20%" ><bean:message key="label.projectTable.newProjectProposal" />
			</th>
		</tr>
		
            <logic:iterate id="infoGroupProperties" name="infoGroupPropertiesList">
                <tr>
                	
                    <td class="listClasses">
                                                              
                        <html:link page="<%="/viewShiftsAndGroups.do?method=execute&executionCourseCode=" + request.getParameter("executionCourseCode") %>" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="idInternal">
							<b><bean:write name="infoGroupProperties" property="name"/></b>
						</html:link>
						
						
							
                    </td>
                     <td class="listClasses">
                   
             		<logic:notEmpty name="infoGroupProperties" property="projectDescription">
                     	<bean:write name="infoGroupProperties" property="projectDescription" filter="false"/>
                	</logic:notEmpty>
                	
             		<logic:empty name="infoGroupProperties" property="projectDescription">
                     	<bean:message key="message.project.wihtout.description"/>
                	</logic:empty>
                	</td>
                    
                     <td class="listClasses" >
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="maximumCapacity">
                	 <p> <abbr title="<bean:message key="label.projectTable.MaximumCapacity.title" />"><bean:message key="label.student.viewExecutionCourseProjects.MaximumCapacity"/></abbr>: <bean:write name="infoGroupProperties" property="maximumCapacity"/> <bean:message key="label.students.lowercase" /></p>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="idealCapacity">
                	 <p> <abbr title="<bean:message key="label.projectTable.IdealCapacity.title" />"> <bean:message key="label.student.viewExecutionCourseProjects.IdealCapacity"/></abbr>: <bean:write name="infoGroupProperties" property="idealCapacity"/> <bean:message key="label.students.lowercase" /></p>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="minimumCapacity">
                	 <p> <abbr title="<bean:message key="label.projectTable.MinimumCapacity.title" />"> <bean:message key="label.student.viewExecutionCourseProjects.MinimumCapacity"/></abbr>: <bean:write name="infoGroupProperties" property="minimumCapacity"/> <bean:message key="label.students.lowercase" /></p>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="groupMaximumNumber">
                	 <p> <abbr title="<bean:message key="label.projectTable.GroupMaximumNumber.title" />"> <bean:message key="label.student.viewExecutionCourseProjects.GroupMaximumNumber"/></abbr>: <bean:write name="infoGroupProperties" property="groupMaximumNumber"/></p>
                   	 </logic:notEmpty>
                   	 
                   	 <p><b><bean:message key="label.student.viewExecutionCourseProjects.GroupEnrolmentPolicy"/>:</b>
                   	 <%if((((net.sourceforge.fenixedu.dataTransferObject.InfoGrouping) infoGroupProperties).getEnrolmentPolicy()).getType().intValue()==1){%>
                   	 <bean:message key="label.atomic"/>
                   	 <%}else{%>
                   	 <bean:message key="label.individual"/>
                   	 <%}%>
                   	 </p>
                </td>
                	
                	<td class="listClasses">
                		<bean:size id="count" name="infoGroupProperties" property="infoExportGroupings"/>
                		<logic:greaterThan name="count" value="1">
            		    <logic:iterate id="infoGroupPropertiesExecutionCourseElement" name="infoGroupProperties" property="infoExportGroupings" >
                		<bean:define id="infoExecutionCourse" name="infoGroupPropertiesExecutionCourseElement" property="infoExecutionCourse" />
							<bean:write name="infoExecutionCourse" property="nome"/></br>
                    	 </logic:iterate>
                    		</logic:greaterThan>
						<logic:equal name="count" value="1">
							<bean:message key="message.project.wihtout.coavaliation"/>
                    	</logic:equal>
                    </td>
     
                </tr>

            </logic:iterate>
</table>
<div id="legend">
	<p><strong><bean:message key="label.projectTable.properties"/>:</strong></p>
	<p><em><bean:message key="label.student.viewExecutionCourseProjects.MaximumCapacity"/></em> - <bean:message key="label.projectTable.MaximumCapacity.title" /></p>
	<p><em><bean:message key="label.student.viewExecutionCourseProjects.IdealCapacity"/></em> - <bean:message key="label.projectTable.IdealCapacity.title" /></p>
	<p><em><bean:message key="label.student.viewExecutionCourseProjects.MinimumCapacity"/></em> - <bean:message key="label.projectTable.MinimumCapacity.title" /></p>
	<p><em><bean:message key="label.student.viewExecutionCourseProjects.GroupMaximumNumber"/></em> - <bean:message key="label.projectTable.GroupMaximumNumber.title" /></p>
	<p><em><bean:message key="label.student.viewExecutionCourseProjects.GroupEnrolmentPolicy"/></em> - <bean:message key="label.projectTable.GroupEnrolmentPolicy.title" /></p>
</div>

<br />

<div class="infoop">
<ul>
<li><strong><bean:message key="label.student.viewExecutionCourseProjects.atomicPolicy" />:</strong> <bean:message key="label.student.viewExecutionCourseProjects.atomicDescription" />.</li>
<li><strong><bean:message key="label.student.viewExecutionCourseProjects.individualPolicy" />:</strong> <bean:message key="label.student.viewExecutionCourseProjects.individualDescription" />.</li>
</ul>
</div>


</logic:notEmpty>
</logic:present>

<logic:notPresent name="infoGroupPropertiesList">
<h2>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h2>
</logic:notPresent>