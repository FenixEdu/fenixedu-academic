<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<logic:present name="infoGrantTypeList">
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with grant type description rows --%>
    <tr>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.name"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.sigla"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.source"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.state"/>
        </td>
        <td class="listClasses-header">&nbsp;</td>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantType" name="infoGrantTypeList">
        <tr>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantType" property="name">
		            <bean:write name="infoGrantType" property="name"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantType" property="sigla">
		            <bean:write name="infoGrantType" property="sigla"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantType" property="source">
		            <bean:write name="infoGrantType" property="source"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">
	        	<logic:present name="infoGrantType" property="state">
		            <bean:message key="label.grant.type.state.active"/>
	            </logic:present>
	            <logic:Notpresent name="infoGrantType" property="state">
		            <bean:message key="label.grant.type.state.nonActive"/>
	            </logic:notPresent>	            
            </td>
            <td class="listClasses">
                    <bean:define id="idGrantType" name="infoGrantType" property="idInternal"/>
                    <html:link page='<%= "/editGrantType.do?method=prepareEditGrantTypeForm&amp;idGrantType=" + idGrantType.toString() %>' > 
                        <bean:message key="label.grant.type.edit" />
                    </html:link>        
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>
    
<%-- If there are no grant types --%>
<logic:notPresent name="infoGrantTypeList">
    <p align="center"><bean:message key="message.grant.type.nonExistentGrantTypes" /></p>
</logic:notPresent>
    
<br/>

<bean:message key="message.grant.type.creation"/>:&nbsp;
<html:link page="/editGrantType.do?method=prepareEditGrantContractForm">
	<bean:message key="label.grant.type.create"/>
</html:link>