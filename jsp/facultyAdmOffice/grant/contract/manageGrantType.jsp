<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<p><b><bean:message key="label.grant.type.information"/></b></p><br/>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<logic:present name="infoGrantTypeList">
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with grant type description rows --%>
    <tr>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.sigla"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.name"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.minPeriodDays"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.type.maxPeriodDays"/>
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
	        	<logic:present name="infoGrantType" property="sigla">
		            <bean:write name="infoGrantType" property="sigla"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantType" property="name">
		            <bean:write name="infoGrantType" property="name"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantType" property="minPeriodDays">
		            <bean:write name="infoGrantType" property="minPeriodDays"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantType" property="maxPeriodDays">
		            <bean:write name="infoGrantType" property="maxPeriodDays"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantType" property="source">
		            <bean:write name="infoGrantType" property="source"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">
	        	<logic:present name="infoGrantType" property="state">
		            <bean:message key="label.grant.type.state.nonActive"/>
		            (<dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantType" property="state.time"/>
                    </dt:format>)
	            </logic:present>
	            <logic:notPresent name="infoGrantType" property="state">
   		            <bean:message key="label.grant.type.state.active"/>
	            </logic:notPresent>	            
            </td>
            <td class="listClasses">
		            <%-- Edit a Grant Type --%>
                    <bean:define id="idGrantType" name="infoGrantType" property="idInternal"/>
                    <html:link page='<%= "/editGrantType.do?method=prepareEditGrantTypeForm&amp;idGrantType=" + idGrantType.toString() %>' > 
                        <bean:message key="link.edit" />
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
    
<br/><br/>

<%-- Create a new Grant Type --%>
<bean:message key="message.grant.type.creation"/>:&nbsp;
<html:link page="/editGrantType.do?method=prepareEditGrantTypeForm">
	<bean:message key="link.create.grant.type"/>
</html:link>

</logic:messagesNotPresent>