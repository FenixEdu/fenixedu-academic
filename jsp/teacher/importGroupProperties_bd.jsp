<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>




<logic:present name="siteView" property="component"> 

<h2><bean:message key="title.importGroupProperties"/></h2>

<bean:define id="infoSiteGroupProperties" name="siteView" property="component"/>
<bean:define id="groupProperties" name="infoSiteGroupProperties" property="infoGroupProperties"/>

<br>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.importGroupProperties.description" />
		</td>
	</tr>
</table>
<br>

<h2><span class="error"><html:errors/></span></h2>
<br>

<table width="100%" border="0" style="text-align: left;">
        <tbody>
        <tr>
		</tr>
		 <tr>
		</tr>

		<tr>
		
			<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesName"/>
         	</td>
                    
			<td class="listClasses" align="left">
			<bean:write name="groupProperties" property="name" />
    		</td>
		</tr>
		
		<tr>
			<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesProjectDescription"/>
         	</td>
         	
         	
         	<td class="listClasses">
             <logic:notEmpty name="groupProperties" property="projectDescription">
            	  <bean:write name="groupProperties" property="projectDescription" filter="false"/>
             </logic:notEmpty>
                	
             <logic:empty name="groupProperties" property="projectDescription">
                	<bean:message key="message.project.wihtout.description"/>
             </logic:empty>
                	</td>
            
		</tr>
		
	    <tr>
		    <td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesEnrolmentBeginDay"/>
         	</td>
	    
			
			<td class="listClasses" align="left">
			<logic:empty name="groupProperties" property="enrolmentBeginDayFormatted"><br />
			</logic:empty>
			
			<logic:notEmpty name="groupProperties" property="enrolmentBeginDayFormatted">
				<bean:write name="groupProperties" property="enrolmentBeginDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<bean:write name="groupProperties" property="enrolmentBeginHourFormatted"/>
				<br />
			</logic:notEmpty>
			</td>
			
		</tr>
	    
 		 </tr>
	    	<tr>

				<td class="listClasses-header" align="left">
				<bean:message key="message.groupPropertiesEnrolmentEndDay"/>
         		</td>

			
			<td class="listClasses" align="left">
			<logic:empty name="groupProperties" property="enrolmentEndDayFormatted">
			<br />				
			</logic:empty>
			
			<logic:notEmpty name="groupProperties" property="enrolmentEndDayFormatted">
				<bean:write name="groupProperties" property="enrolmentEndDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<bean:write name="groupProperties" property="enrolmentEndHourFormatted"/>
				<br />			
			</logic:notEmpty>
			</td>
			
		</tr>
			
		<tr>

			<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesEnrolmentPolicy"/>
         	</td>

			<td class="listClasses" align="left">
			<bean:write name="enrolmentPolicyName"/>
			</td>
		</tr>
		
		<tr>
			<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesShiftType"/>
         	</td>
		
			<td class="listClasses" align="left">
			<bean:write name="shiftTypeName"/></td>		
		</tr>

		<tr>
			<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesMaximumCapacity"/>:
			<br><bean:message key="label.teacher.insertGroupProperties.MaximumCapacityDescription"/>
			<br>
         	</td>
			
			<td class="listClasses" align="left">
			<logic:empty name="groupProperties" property="maximumCapacity">
			<br>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="maximumCapacity">
			<bean:write name="groupProperties" property="maximumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>	
    	<tr>
    	
    		<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesMinimumCapacity"/>:
			<br><bean:message key="label.teacher.insertGroupProperties.MinimumCapacityDescription"/>
			<br>
         	</td>
    	
			<td class="listClasses" align="left">
			<logic:empty name="groupProperties" property="minimumCapacity">
			<br>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="minimumCapacity">
			<bean:write name="groupProperties" property="minimumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
		
			<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesIdealCapacity"/>:
			<br><bean:message key="label.teacher.insertGroupProperties.IdealCapacityDescription"/>
			<br>
         	</td>
		
			<td class="listClasses" align="left">
			<logic:empty name="groupProperties" property="idealCapacity">
			<br>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="idealCapacity">
			<bean:write name="groupProperties" property="idealCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
			<td class="listClasses-header" align="left">
			<bean:message key="message.groupPropertiesGroupMaximumNumber"/>:
         	</td>
		
			
			<td class="listClasses" align="left">
			<logic:empty name="groupProperties" property="groupMaximumNumber">
			<br>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="groupMaximumNumber">
			<bean:write name="groupProperties" property="groupMaximumNumber" />
			</logic:notEmpty>
			</td>
			
		</tr>
		
  </tbody>
</table>
<br />
<br />

<table>
<tr>

	<html:form action="/acceptNewProjectProposal" method="get">
	<td>
		<html:submit styleClass="inputbutton"><bean:message key="button.accept"/>                    		         	
		</html:submit>
	</td>
		<html:hidden property="method" value="acceptNewProjectProposal"/>
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

	<html:form action="/rejectNewProjectProposal" method="get">
	<td>
		<html:submit styleClass="inputbutton"><bean:message key="button.reject"/>                    		         	
		</html:submit>
	</td>
		<html:hidden property="method" value="rejectNewProjectProposal"/>
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

	<html:form action="/viewNewProjectProposals" method="get">
	<td>
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	</td>
		<html:hidden property="method" value="viewNewProjectProposals"/>
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	</html:form>

</tr>
</table>

</logic:present>



<logic:notPresent name="siteView" property="component"> 
<h2>
<bean:message key="message.infoGroupProperties.not.available" />
</h2>
</logic:notPresent>