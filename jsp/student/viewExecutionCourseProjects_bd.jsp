<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="DataBeans.InfoGroupProperties"%>

<logic:present name="infoGroupPropertiesList">

<h2><bean:message key="title.ExecutionCourseProjects"/></h2>
<br>
	
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.student.viewExecutionCourseProjects.description" />
		</td>
	</tr>
</table>
<br>	


	
	<logic:empty name="infoGroupPropertiesList">
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	</logic:empty>
	<logic:notEmpty name="infoGroupPropertiesList">
	<br>
	<table border="0" style="text-align: left;">
        <tbody>
         <tr>
			<td class="listClasses-header" width="20%"><bean:message key="label.projectName" />
			</td>
			<td class="listClasses-header" width="45%"><bean:message key="label.projectDescription" />
			</td>
			<td class="listClasses-header" width="35%"><bean:message key="label.properties" />
			</td>		
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
                	 <b><bean:message key="label.student.viewExecutionCourseProjects.MaximumCapacity"/>:</b> <bean:write name="infoGroupProperties" property="maximumCapacity"/>
                	 <br/>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="idealCapacity">
                	 <b><bean:message key="label.student.viewExecutionCourseProjects.IdealCapacity"/>:</b> <bean:write name="infoGroupProperties" property="idealCapacity"/>
                	 <br/>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="minimumCapacity">
                	 <b><bean:message key="label.student.viewExecutionCourseProjects.MinimumCapacity"/>:</b> <bean:write name="infoGroupProperties" property="minimumCapacity"/>
                	 <br/>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="groupMaximumNumber">
                	 <b><bean:message key="label.student.viewExecutionCourseProjects.GroupMaximumNumber"/>:</b> <bean:write name="infoGroupProperties" property="groupMaximumNumber"/>
                   	 <br/>
                   	 </logic:notEmpty>
                   	 
                   	 <b><bean:message key="label.student.viewExecutionCourseProjects.GroupEnrolmentPolicy"/>:</b>
                   	 <%if((((InfoGroupProperties)infoGroupProperties).getEnrolmentPolicy()).getType().intValue()==1){%>
                   	 <bean:message key="label.atomic"/>
                   	 <%}else{%>
                   	 <bean:message key="label.individual"/>
                	<%}%>	
                	</td>
     
                </tr>

            </logic:iterate>
        </tbody>
</table>

<br>
<br>	
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.student.viewExecutionCourseProjects.propertiesDescription" />
		</td>
	</tr>
</table>
<br>


</logic:notEmpty>

	
</logic:present>

<logic:notPresent name="infoGroupPropertiesList">
<h2>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h2>
</logic:notPresent>