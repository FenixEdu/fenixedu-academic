<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoSiteGroupsByShiftList">
	<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available" /></h2>
	</logic:empty>
	
<table border="0" style="text-align: left;">
	<tbody>
	<b><html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupProperties=" + request.getParameter("groupProperties")%>" paramId="objectCode" paramName="objectCode" ><bean:message key="link.editGroupProperties"/></html:link></b>
	
     <logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
     <tr>
      <td>
        <br>
          <h2>
           <bean:define id="infoShift" name="infoSiteGroupsByShift" property="infoShift"/>
			
			<bean:write name="infoShift" property="nome"/></h2>
				<html:link page="<%= "/insertStudentGroup.do?method=prepareCreateStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupProperties=" + request.getParameter("groupProperties")%>" paramId="shiftCode" paramName="infoShift" paramProperty="idInternal">
					<b><bean:message key="link.insertGroup"/></b>
				</html:link>
             <br>
             <br>
             
<table width="500" cellpadding="0" border="0">
          
             <logic:iterate id="infoStudentGroup" name="infoSiteGroupsByShift" property="infoStudentGroupsList" >
        		<tr>
          		<td>
             	<br>
             	<td class="listClasses">
               	<li><html:link page="<%= "/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="studentGroup" paramName="infoStudentGroup" paramProperty="idInternal">
						<bean:message key="label.groupWord"/>
                		<bean:write name="infoStudentGroup" property="groupNumber"/>
					</html:link></li>
				</td>
               		
	           		<bean:define id="infoGroupProperties" name="infoStudentGroup" property="infoGroupProperties"/>
               		<bean:define id="idInternal" name="infoGroupProperties" property="idInternal"/>
        			
					<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
               	<td class="listClasses">
               	
               		<html:link page="<%="/editStudentGroup.do?method=prepareEditStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;shiftCode=" + shiftCode.toString()%>" paramId="studentGroup" paramName="infoStudentGroup" paramProperty="idInternal">
               				
               		
               				<b><bean:message key="link.editGroupMembers"/></b>
 					</html:link>
                </td>
               	<td class="listClasses">
               	
                    <html:link page="<%="/editStudentGroup.do?method=prepareEditStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;shiftCode=" + shiftCode.toString()%>" paramId="studentGroup" paramName="infoStudentGroup" paramProperty="idInternal">
               				
               		
               				<b><bean:message key="link.editGroupShift"/></b>
 					</html:link>
                </td>
                <td class="listClasses">
               	
                	<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupProperties=" + idInternal.toString()%>" paramId="studentGroup" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.deleteGroup"/></b>
 					</html:link>
             	</td>
             	</td>
                </tr>

            </logic:iterate>
</table>   
   
            
        </tbody>
                
                
                
                    </td>
                </tr>

            </logic:iterate>
            <h2></h2><span class="error"><html:errors/></span></h2>
        </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoSiteGroupsByShiftList.not.available" />
</h4>
</logic:notPresent>