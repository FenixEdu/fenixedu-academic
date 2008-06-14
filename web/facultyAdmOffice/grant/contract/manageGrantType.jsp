<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.type.information"/></h2>

<div class="infoop2">
	<p><bean:message key="info.grant.manage.granttype.information"/></p>
	<p><bean:message key="info.grant.manage.granttype.edit"/>"<bean:message key="link.edit"/>".</p>
	<p><bean:message key="info.grant.manage.granttype.create"/>"<bean:message key="link.create.grant.type"/>".</p>
</div>

<%-- Presenting errors --%>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<logic:present name="infoGrantTypeList">

	<%-- Create a new Grant Type --%>
	<p>
		<bean:message key="message.grant.type.creation"/>:&nbsp;
		<html:link page="/editGrantType.do?method=prepareEditGrantTypeForm">
			<bean:message key="link.create.grant.type"/>
		</html:link>
	</p>

    <table class="tstyle4">
    <%-- Table with grant type description rows --%>
    <tr>
        <th>
            <bean:message key="label.grant.type.sigla"/>
        </th>
        <th>
            <bean:message key="label.grant.type.name"/>
        </th>
        <th>
            <bean:message key="label.grant.type.minPeriodDays"/>
        </th>
        <th>
            <bean:message key="label.grant.type.maxPeriodDays"/>
        </th>
        <th>
            <bean:message key="label.grant.type.source"/>
        </th>
        <th>
            <bean:message key="label.grant.type.state"/>
        </th>
        <th>&nbsp;</th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantType" name="infoGrantTypeList">
        <tr>
            <td>&nbsp;
	        	<logic:present name="infoGrantType" property="sigla">
		            <bean:write name="infoGrantType" property="sigla"/>
	            </logic:present>&nbsp;
            </td>
            <td>&nbsp;
	        	<logic:present name="infoGrantType" property="name">
		            <bean:write name="infoGrantType" property="name"/>
	            </logic:present>&nbsp;
            </td>
            <td class="acenter">&nbsp;
	        	<logic:present name="infoGrantType" property="minPeriodDays">
		            <bean:write name="infoGrantType" property="minPeriodDays"/>
	            </logic:present>&nbsp;
            </td>
            <td class="acenter">&nbsp;
	        	<logic:present name="infoGrantType" property="maxPeriodDays">
		            <bean:write name="infoGrantType" property="maxPeriodDays"/>
	            </logic:present>&nbsp;
            </td>
            <td class="acenter">&nbsp;
	        	<logic:present name="infoGrantType" property="source">
		            <bean:write name="infoGrantType" property="source"/>
	            </logic:present>&nbsp;
            </td>
            <td class="acenter">
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
            <td>
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


<%-- Create a new Grant Type --%>
<p>
	<bean:message key="message.grant.type.creation"/>:&nbsp;
	<html:link page="/editGrantType.do?method=prepareEditGrantTypeForm">
		<bean:message key="link.create.grant.type"/>
	</html:link>
</p>

</logic:messagesNotPresent>