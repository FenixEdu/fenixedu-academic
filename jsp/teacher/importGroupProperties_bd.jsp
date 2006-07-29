<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>




<logic:present name="siteView" property="component"> 

<h2><bean:message key="title.importGroupProperties"/></h2>

<bean:define id="infoSiteGroupProperties" name="siteView" property="component"/>
<bean:define id="infoGrouping" name="infoSiteGroupProperties" property="infoGrouping"/>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.importGroupProperties.description" />
		</td>
	</tr>
</table>
<br/>

<h2><span class="error"><!-- Error messages go here --><html:errors /></span></h2>
<br/>

<table width="100%" border="0" style="text-align: left;">
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

	<html:form action="/acceptNewProjectProposal" method="get">
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.accept"/>                    		         	
		</html:submit>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="acceptNewProjectProposal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

	<html:form action="/rejectNewProjectProposal" method="get">
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.reject"/>                    		         	
		</html:submit>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="rejectNewProjectProposal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

	<html:form action="/viewNewProjectProposals" method="get">
	<td>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewNewProjectProposals"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	</html:form>

</tr>
</table>

</logic:present>



<logic:notPresent name="siteView" property="component"> 
<h2>
<bean:message key="message.infoGroupProperties.not.available" />
</h2>
</logic:notPresent>