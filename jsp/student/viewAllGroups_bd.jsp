<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="infoSiteGroupsByShiftList">

	<html:link page="<%="/groupEnrolment.do?method=prepareEnrolment&groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>">
               				<b><bean:message key="link.insertGroup"/></b></html:link>

	<logic:empty name="infoSiteGroupsByShiftList">
	<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available" /></h2>
	</logic:empty>
	
	<h2><span class="error"><html:errors/></span></h2>
	
<table border="0" style="text-align: left;">
	<tbody>
	
	<logic:iterate id="infoSiteGroupsByShift" name="infoSiteGroupsByShiftList" >
     <tr>
     	<td>
		<br>
        <h2>
        	<bean:define id="infoShift" name="infoSiteGroupsByShift" property="infoShift"/>
			<bean:write name="infoShift" property="nome"/></h2>
			<br>
             
			<table width="500" cellpadding="0" border="0" style="text-align: left;">
          	<tbody>
            	<logic:iterate id="infoStudentGroup" name="infoSiteGroupsByShift" property="infoStudentGroupsList" >
        		<tr>
          		<td>
             		<br>
             		<td class="listClasses">
               		<li><html:link page="/viewStudentGroupInformation.do" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
						<bean:message key="label.groupWord"/>
                		<bean:write name="infoStudentGroup" property="groupNumber"/>
						</html:link></li>
					</td>
               		
	           		<bean:define id="infoGroupProperties" name="infoStudentGroup" property="infoGroupProperties"/>
               		<bean:define id="idInternal" name="infoGroupProperties" property="idInternal"/>
					
               		<td class="listClasses">
               	
               			<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.enrolment"/></b></html:link>
                	</td>
               	
               		<td class="listClasses">
               	
                   	<html:link page="<%="/removeGroupEnrolment.do?method=prepareRemove&groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               			<b><bean:message key="link.removeEnrolment"/></b></html:link>
                	</td>
                
             	</td>
                </tr>

            	</logic:iterate>
            </tbody>
			</table>
            </td>
            </tr>
            </logic:iterate>
            <br>
        </tbody>
</table>

	<html:form action="/viewProjectStudentGroups" method="get">
	
	<html:submit styleClass="inputbutton"><bean:message key="button.refresh"/>                    		         	
	</html:submit>
	
	<html:hidden property="method" value="execute"/>
	<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
	</html:form>


</logic:present>

<logic:notPresent name="infoSiteGroupsByShiftList">
<h4>
<bean:message key="message.infoSiteGroupsByShiftList.not.available" />
</h4>
</logic:notPresent>

