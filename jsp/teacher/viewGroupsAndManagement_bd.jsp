<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoSiteGroupsByShiftList">
	<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
	<tbody>
     <logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
     <tr>
      <td>
        <br>
          <h2>
           <bean:define id="infoShift" name="infoSiteGroupsByShift" property="infoShift"/>
			
			<bean:write name="infoShift" property="nome"/></h2>
				<html:link page="<%= "/insertStudentGroup.do?method=insertStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
					<b><bean:message key="link.insertGroup"/></b>
				</html:link>
               <logic:iterate id="infoStudentGroup" name="infoSiteGroupsByShift" property="infoStudentGroupsList" >
        		<tr>
          		<td>
             	<br>
               	<li><html:link page="<%= "/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="studentGroup" paramName="infoStudentGroup" paramProperty="idInternal">
						<bean:message key="label.groupWord"/>
                		<bean:write name="infoStudentGroup" property="groupNumber"/>
					</html:link></li>
               		&nbsp
               		<html:link page="<%= "/editStudentGroup.do?method=editStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="studentGroup" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.editGroup"/></b>
 					</html:link>
                    &nbsp                
                	<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="studentGroup" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.deleteGroup"/></b>
 					</html:link>
             	</td>
                </tr>

            </logic:iterate>
        </tbody>
                
                
                
                    </td>
                </tr>

            </logic:iterate>
        </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoSiteGroupsByShiftList.not.available" />
</h4>
</logic:notPresent>