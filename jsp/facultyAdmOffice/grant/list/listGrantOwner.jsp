<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<table align="center">
<tr>
	<logic:present name="beforeSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.before.page"/>
		</html:link>
	</td>
	</logic:present>
	<td>
	&nbsp;
<html:form action="/listGrantOwner" style="display:inline">
	<html:hidden property="method" value="prepareListGrantOwner"/>
	<html:hidden property="page" value="1"/>

	<%-- span attributes --%>
	<html:hidden property="orderBy"/>
	<html:hidden property="totalElements"/>
	<html:text property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>
</html:form>
	&nbsp;</td>
	<logic:present name="afterSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.after.page"/>
		</html:link>
	</td>
	</logic:present>
</tr>
</table>
<br/>
<logic:present name="listGrantOwner">

    <table border="0" cellspacing="1" cellpadding="1" align="center">
    <%-- Table with list grant owner description rows --%>
    <tr>
        <td class="listClasses-header">
	        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("spanNumber") +  "&amp;orderBy=orderByNumber&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
	            <bean:message key="label.list.grant.owner.number"/>
			</html:link>
        </td>
        <td class="listClasses-header">
	        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("spanNumber") + "&amp;orderBy=orderByFirstName&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
	            <bean:message key="label.list.grant.owner.first.name"/>
			</html:link>
        </td>
        <td class="listClasses-header">
	        <%-- <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("spanNumber") + "&amp;orderBy=orderByFirstName&amp;totalElements=" + request.getAttribute("totalElements") %>' > --%> 
	            <bean:message key="label.list.grant.owner.last.name"/>
			<%-- </html:link> --%>
        </td>
        <td class="listClasses-header">&nbsp;</td>
    </tr>   
    
    <%-- Table with result of search --%>
    <logic:iterate id="infoListGrantOwnerByOrder" name="listGrantOwner">
        <tr>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoListGrantOwnerByOrder" property="grantOwnerNumber">
		            <bean:write name="infoListGrantOwnerByOrder" property="grantOwnerNumber"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoListGrantOwnerByOrder" property="firstName">
		            <bean:write name="infoListGrantOwnerByOrder" property="firstName"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoListGrantOwnerByOrder" property="lastName">
		            <bean:write name="infoListGrantOwnerByOrder" property="lastName"/>
	            </logic:present>&nbsp;
            </td>
            <td class="listClasses">
	            <%-- Show all the information of a grant owner --%>
                <bean:define id="idGrantOwner" name="infoListGrantOwnerByOrder" property="grantOwnerId"/>
                <html:link page='<%= "/listGrantOwner.do?method=showGrantOwner&amp;grantOwnerId=" + idGrantOwner.toString() %>' > 
                    <bean:message key="link.grant.owner.show" />
                </html:link>        
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>
<br/>
<table align="center">
<tr>
	<logic:present name="beforeSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.before.page"/>
		</html:link>
	</td>
	</logic:present>
	<td>
	&nbsp;
	<html:form action="/listGrantOwner" style="display:inline">
	<html:hidden property="method" value="prepareListGrantOwner"/>
	<html:hidden property="page" value="1"/>

	<%-- span attributes --%>
	<html:hidden property="orderBy"/>
	<html:hidden property="totalElements"/>
	<html:text property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>
	</html:form>
	&nbsp;
	</td>
	<logic:present name="afterSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.after.page"/>
		</html:link>
	</td>
	</logic:present>
</tr>
</table>

</logic:messagesNotPresent>