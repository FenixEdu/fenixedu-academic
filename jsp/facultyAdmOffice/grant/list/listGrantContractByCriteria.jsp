<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>
<html:form action="/listGrantContractByCriteria" style="display:inline">

<html:hidden property="method" value="prepareListGrantOwnerByCriteria"/>
<html:hidden property="page" value="1"/>

<%-- span attributes --%>
<html:hidden property="orderBy"/>
<html:hidden property="totalElements"/>
<html:hidden property="numberOfElementsInSpan"/>
<html:hidden property="totalElements"/>
<html:hidden property="filterType"/>
<html:hidden property="beginContract"/>
<html:hidden property="endContract"/>

<bean:define id="orderBy" name="listGrantOwnerByCriteriaForm" property="orderBy"/>
<bean:define id="totalElements" name="listGrantOwnerByCriteriaForm" property="totalElements"/>
<bean:define id="numberOfElementsInSpan" name="listGrantOwnerByCriteriaForm" property="numberOfElementsInSpan"/>
<bean:define id="totalElements" name="listGrantOwnerByCriteriaForm" property="totalElements"/>
<bean:define id="filterType" name="listGrantOwnerByCriteriaForm" property="filterType"/>
<bean:define id="beginContract" name="listGrantOwnerByCriteriaForm" property="beginContract"/>
<bean:define id="endContract" name="listGrantOwnerByCriteriaForm" property="endContract"/>

	<table align="center">
	<tr>
		<logic:present name="beforeSpan">
		<td>
		<html:link page='<%= "/listGrantOwnerByCriteria.do?method=prepareListGrantOwnerByCriteria&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;numberOfElementsInSpan=" + numberOfElementsInSpan + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract %>' > 
			   	<bean:message key="link.grant.owner.list.before.page"/>
			</html:link>
		</td>
		</logic:present>
		<td>&nbsp;<html:text property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>&nbsp;</td>
		<logic:present name="afterSpan">
		<td>
	        <html:link page='<%= "/listGrantOwnerByCriteria.do?method=prepareListGrantOwnerByCriteria&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;numberOfElementsInSpan=" + numberOfElementsInSpan + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract %>' > 
			   	<bean:message key="link.grant.owner.list.after.page"/>
			</html:link>
		</td>
		</logic:present>
	</tr>
	</table>
	<br/>
</html:form>
	
<logic:present name="listGrantOwner">

    <table border="0" cellspacing="1" cellpadding="1" align="center">
    <%-- Table with list grant owner description rows --%>
    <tr>
        <td class="listClasses-header">
	    	<bean:message key="label.list.grant.owner.number"/>
		</td>
        <td class="listClasses-header">
            <bean:message key="label.list.grant.owner.first.name"/>
		</td>
        <td class="listClasses-header">
            <bean:message key="label.list.grant.owner.last.name"/>
        </td>
        <td class="listClasses-header">
        	<bean:message key="label.list.grant.owner.contract.type"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.list.byCriteria.grant.owner.dateBegin"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.list.byCriteria.grant.owner.dateEnd"/>
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
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoListGrantOwnerByOrder" property="grantType">
		            <bean:write name="infoListGrantOwnerByOrder" property="grantType"/>
	            </logic:present>&nbsp;
        	</td>
			<td class="listClasses">&nbsp;
		    	<logic:present name="infoListGrantOwnerByOrder" property="beginContract">
					<dt:format pattern="dd-MM-yyyy">
		            <bean:write name="infoListGrantOwnerByOrder" property="beginContract.time"/>
					</dt:format>
		        </logic:present>&nbsp;
    		</td>
			<td class="listClasses">&nbsp;
				<logic:present name="infoListGrantOwnerByOrder" property="endContract">
                	<dt:format pattern="dd-MM-yyyy">
			        <bean:write name="infoListGrantOwnerByOrder" property="endContract.time"/>
					</dt:format>
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

<html:form action="/listGrantContractByCriteria" style="display:inline">

<html:hidden property="method" value="prepareListGrantOwnerByCriteria"/>
<html:hidden property="page" value="1"/>

<%-- span attributes --%>
<html:hidden property="orderBy"/>
<html:hidden property="totalElements"/>
<html:hidden property="numberOfElementsInSpan"/>
<html:hidden property="totalElements"/>
<html:hidden property="filterType"/>
<html:hidden property="beginContract"/>
<html:hidden property="endContract"/>

<bean:define id="orderBy" name="listGrantOwnerByCriteriaForm" property="orderBy"/>
<bean:define id="totalElements" name="listGrantOwnerByCriteriaForm" property="totalElements"/>
<bean:define id="numberOfElementsInSpan" name="listGrantOwnerByCriteriaForm" property="numberOfElementsInSpan"/>
<bean:define id="totalElements" name="listGrantOwnerByCriteriaForm" property="totalElements"/>
<bean:define id="filterType" name="listGrantOwnerByCriteriaForm" property="filterType"/>
<bean:define id="beginContract" name="listGrantOwnerByCriteriaForm" property="beginContract"/>
<bean:define id="endContract" name="listGrantOwnerByCriteriaForm" property="endContract"/>

	<table align="center">
	<tr>
		<logic:present name="beforeSpan">
		<td>
		<html:link page='<%= "/listGrantOwnerByCriteria.do?method=prepareListGrantOwnerByCriteria&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;numberOfElementsInSpan=" + numberOfElementsInSpan + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract %>' > 
			   	<bean:message key="link.grant.owner.list.before.page"/>
			</html:link>
		</td>
		</logic:present>
		<td>&nbsp;<html:text property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>&nbsp;</td>
		<logic:present name="afterSpan">
		<td>
	        <html:link page='<%= "/listGrantOwnerByCriteria.do?method=prepareListGrantOwnerByCriteria&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;numberOfElementsInSpan=" + numberOfElementsInSpan + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract %>' > 
			   	<bean:message key="link.grant.owner.list.after.page"/>
			</html:link>
		</td>
		</logic:present>
	</tr>
	</table>
</html:form>
</logic:messagesNotPresent>