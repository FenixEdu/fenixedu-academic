<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="infoGroupPropertiesList">
	
	<logic:empty name="infoGroupPropertiesList">
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	</logic:empty>
	<logic:notEmpty name="infoGroupPropertiesList">
<table border="0" style="text-align: left;">
        <tbody>
            <logic:iterate id="infoGroupProperties" name="infoGroupPropertiesList">
                <tr>
                    <td>
                        <br>
                        
                        <h2><li>             
                        <html:link page="/viewProjectStudentGroups.do" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="idInternal">
							<bean:write name="infoGroupProperties" property="name"/>
						</html:link>
						</li></h2>
							
                    </td>
                </tr>

            </logic:iterate>
        </tbody>
</table>
</logic:notEmpty>
	
</logic:present>

<logic:notPresent name="infoGroupPropertiesList">
<h4>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h4>
</logic:notPresent>