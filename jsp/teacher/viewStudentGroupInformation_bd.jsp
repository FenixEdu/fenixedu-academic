<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoSiteStudentInformationList">
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
        <tbody>
            <logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList" >
                <tr>
                    <td>
                        <br>
                        
							<li>
								<b><bean:message key="label.numberWord"/></b>
								<bean:write name="infoSiteStudentInformation" property="number"/>
								&nbsp;
								<b><bean:message key="label.nameWord"/></b>
								<bean:write name="infoSiteStudentInformation" property="name"/>
								&nbsp;
								<b><bean:message key="label.emailWord"/></b>
								<bean:write name="infoSiteStudentInformation" property="email"/>
							
							</li>
						
                    </td>
                </tr>

            </logic:iterate>
        </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h4>
</logic:notPresent>