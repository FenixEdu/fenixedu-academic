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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<br/>
<table width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.importGroupProperties.description" />
		</td>
	</tr>
</table>
<br/>

<h2><span class="error"><!-- Error messages go here --><html:errors /></span></h2>
<br/>

<table width="98%" border="0" style="text-align: left;">
        <tbody>
        <tr>
		</tr>
		 <tr>
		</tr>

		<tr>
		
			<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesName"/>
         	</th>
                    
			<td class="listClasses" align="left">
			<bean:write name="infoGrouping" property="name" />
    		</td>
		</tr>
		
		<tr>
			<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesProjectDescription"/>
         	</th>
         	
         	
         	<td class="listClasses">
             <logic:notEmpty name="infoGrouping" property="projectDescription">
            	  <bean:write name="infoGrouping" property="projectDescription" filter="false"/>
             </logic:notEmpty>
                	
             <logic:empty name="infoGrouping" property="projectDescription">
                	<bean:message key="message.project.wihtout.description"/>
             </logic:empty>
                	</td>
            
		</tr>
		
	    <tr>
		    <th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesEnrolmentBeginDay"/>
         	</th>
	    
			
			<td class="listClasses" align="left">
			<logic:empty name="infoGrouping" property="enrolmentBeginDayFormatted"><br />
			</logic:empty>
			
			<logic:notEmpty name="infoGrouping" property="enrolmentBeginDayFormatted">
				<bean:write name="infoGrouping" property="enrolmentBeginDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<bean:write name="infoGrouping" property="enrolmentBeginHourFormatted"/>
				<br />
			</logic:notEmpty>
			</td>
			
		</tr>
	    
 		 </tr>
	    	<tr>

				<th class="listClasses-header" align="left">
				<bean:message key="message.groupPropertiesEnrolmentEndDay"/>
         		</th>

			
			<td class="listClasses" align="left">
			<logic:empty name="infoGrouping" property="enrolmentEndDayFormatted">
			<br />				
			</logic:empty>
			
			<logic:notEmpty name="infoGrouping" property="enrolmentEndDayFormatted">
				<bean:write name="infoGrouping" property="enrolmentEndDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<bean:write name="infoGrouping" property="enrolmentEndHourFormatted"/>
				<br />			
			</logic:notEmpty>
			</td>
			
		</tr>
			
		<tr>

			<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesEnrolmentPolicy"/>
         	</th>

			<td class="listClasses" align="left">
			<bean:write name="enrolmentPolicyName"/>
			</td>
		</tr>
		
		<tr>
			<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesShiftType"/>
         	</th>
		
			<td class="listClasses" align="left">
			<bean:write name="shiftTypeName"/></td>		
		</tr>

		<tr>
			<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesMaximumCapacity"/>:
			<br/><bean:message key="label.teacher.insertGroupProperties.MaximumCapacityDescription"/>
			<br/>
         	</th>
			
			<td class="listClasses" align="left">
			<logic:empty name="infoGrouping" property="maximumCapacity">
			<br/>
			</logic:empty>
			<logic:notEmpty name="infoGrouping" property="maximumCapacity">
			<bean:write name="infoGrouping" property="maximumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>	
    	<tr>
    	
    		<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesMinimumCapacity"/>:
			<br/><bean:message key="label.teacher.insertGroupProperties.MinimumCapacityDescription"/>
			<br/>
         	</th>
    	
			<td class="listClasses" align="left">
			<logic:empty name="infoGrouping" property="minimumCapacity">
			<br/>
			</logic:empty>
			<logic:notEmpty name="infoGrouping" property="minimumCapacity">
			<bean:write name="infoGrouping" property="minimumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
		
			<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesIdealCapacity"/>:
			<br/><bean:message key="label.teacher.insertGroupProperties.IdealCapacityDescription"/>
			<br/>
         	</th>
		
			<td class="listClasses" align="left">
			<logic:empty name="infoGrouping" property="idealCapacity">
			<br/>
			</logic:empty>
			<logic:notEmpty name="infoGrouping" property="idealCapacity">
			<bean:write name="infoGrouping" property="idealCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
			<th class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesGroupMaximumNumber"/>:
         	</th>
		
			
			<td class="listClasses" align="left">
			<logic:empty name="infoGrouping" property="groupMaximumNumber">
			<br/>
			</logic:empty>
			<logic:notEmpty name="infoGrouping" property="groupMaximumNumber">
			<bean:write name="infoGrouping" property="groupMaximumNumber" />
			</logic:notEmpty>
			</td>
			
		</tr>
		
  </tbody>
</table>
<br />
<br />

<table>
<tr>

	<html:form action="/studentGroupManagement" >
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.accept"/>                    		         	
		</html:submit>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="acceptNewProjectProposal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

	<html:form action="/studentGroupManagement" >
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.reject"/>                    		         	
		</html:submit>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="rejectNewProjectProposal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

	<html:form action="/studentGroupManagement" >
	<td>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewNewProjectProposals"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
	</html:form>

</tr>
</table>
