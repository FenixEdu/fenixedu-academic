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
               <logic:iterate id="groupNumber" name="infoSiteGroupsByShift" property="groupsNumberList" >
        		<tr>
          		<td>
             	<br>
               	<li><html:link page="<%= "/viewSite.do" + "?method=viewStudentGroupInformationAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties") %>" paramId="groupNumber" paramName="groupNumber">
									<bean:message key="label.groupWord"/>
                					<bean:write name="groupNumber"/>	</h2>
							</html:link></li>
               
                
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