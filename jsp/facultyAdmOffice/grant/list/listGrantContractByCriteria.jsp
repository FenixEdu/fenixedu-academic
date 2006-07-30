<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>
<logic:present name="listGrantContract">

<html:form action="/listGrantContractByCriteria" style="display:inline">

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareListGrantContractByCriteria"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<%-- span attributes --%>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orderBy" property="orderBy"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.totalElements" property="totalElements"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.totalElements" property="totalElements"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.filterType" property="filterType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginContract" property="beginContract"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endContract" property="endContract"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantTypeId" property="grantTypeId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.validToTheDate" property="validToTheDate"/>

<bean:define id="spanNumber" name="listGrantContractByCriteriaForm" property="spanNumber"/>
<bean:define id="orderBy" name="listGrantContractByCriteriaForm" property="orderBy"/>
<bean:define id="totalElements" name="listGrantContractByCriteriaForm" property="totalElements"/>
<bean:define id="totalElements" name="listGrantContractByCriteriaForm" property="totalElements"/>
<bean:define id="filterType" name="listGrantContractByCriteriaForm" property="filterType"/>
<bean:define id="beginContract" name="listGrantContractByCriteriaForm" property="beginContract"/>
<bean:define id="endContract" name="listGrantContractByCriteriaForm" property="endContract"/>
<bean:define id="grantTypeId" name="listGrantContractByCriteriaForm" property="grantTypeId"/>
<bean:define id="validToTheDate" name="listGrantContractByCriteriaForm" property="validToTheDate"/>


	<table align="center">
	<tr>
		<logic:present name="beforeSpan">
		<td>
		<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId + "&amp;validToTheDate=" + validToTheDate %>' > 
			   	<bean:message key="link.grant.owner.list.before.page"/>
			</html:link>
		</td>
		</logic:present>
		<td>&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.spanNumber" property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>&nbsp;</td>
		<logic:present name="afterSpan">
		<td>
	        <html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' > 
			   	<bean:message key="link.grant.owner.list.after.page"/>
			</html:link>
		</td>
		</logic:present>
		
	</tr>
	</table>
	<table align="center">
	<tr>
	<td>&nbsp;Total&nbsp;<bean:write name="totalElements"/></td>
	</tr>
	</table>
	<br/>


    <table border="0" cellspacing="1" cellpadding="1" align="center">
    <%-- Table with list grant owner description rows --%>
    <tr>
        <th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByGrantOwnerNumber&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
	    	<bean:message key="label.list.grant.owner.number"/>
	    	</html:link>
		</th>
        <th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByFirstName&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
            <bean:message key="label.list.grant.owner.first.name"/>
            </html:link>
		</th>
        <th class="listClasses-header">
            <bean:message key="label.list.grant.owner.last.name"/>
        </th>
        <th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByGrantContractNumber&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
	    	<bean:message key="label.grant.contract.contractnumber"/>
	    	</html:link>
		</th>
        <th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByGrantType&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
        	<bean:message key="label.list.grant.owner.contract.type"/>
        	</html:link>
		</th>
		<th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByDateBeginContract&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
			<bean:message key="label.list.byCriteria.grant.owner.dateBegin"/>
			</html:link>
		</th>
		<th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByDateEndContract&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
			<bean:message key="label.list.byCriteria.grant.owner.dateEnd"/>
			</html:link>
        </th>
        <th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByDateEndContract&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
			<bean:message key="label.list.byCriteria.grant.owner.numberCostCenter"/>
			</html:link>
        </th>
        <th class="listClasses-header">
			<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + spanNumber + "&amp;orderBy=orderByDateEndContract&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' >
			<bean:message key="label.list.byCriteria.grant.owner.designation"/>
			</html:link>
        </th>
        <th class="listClasses-header">&nbsp;</th>
    </tr>   

</html:form>

    <%-- Table with result of search --%>
    <logic:iterate id="infoListGrantOwnerByOrder" name="listGrantContract">
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
	        	<logic:present name="infoListGrantOwnerByOrder" property="contractNumber">
		            <bean:write name="infoListGrantOwnerByOrder" property="contractNumber"/>
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
			<td class="listClasses">&nbsp;
				<logic:present name="infoListGrantOwnerByOrder" property="numberPaymentEntity">  	
			        <bean:write name="infoListGrantOwnerByOrder" property="numberPaymentEntity"/>	
			    </logic:present>&nbsp;
			</td>
			<td class="listClasses">&nbsp;
				<logic:present name="infoListGrantOwnerByOrder" property="designation">
			        <bean:write name="infoListGrantOwnerByOrder" property="designation"/>
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
<br/>

<html:form action="/listGrantContractByCriteria" style="display:inline">

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareListGrantContractByCriteria"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<%-- span attributes --%>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orderBy" property="orderBy"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.totalElements" property="totalElements"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.totalElements" property="totalElements"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.filterType" property="filterType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginContract" property="beginContract"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endContract" property="endContract"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantTypeId" property="grantTypeId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.validToTheDate" property="validToTheDate"/>

<bean:define id="orderBy" name="listGrantContractByCriteriaForm" property="orderBy"/>
<bean:define id="totalElements" name="listGrantContractByCriteriaForm" property="totalElements"/>
<bean:define id="totalElements" name="listGrantContractByCriteriaForm" property="totalElements"/>
<bean:define id="filterType" name="listGrantContractByCriteriaForm" property="filterType"/>
<bean:define id="beginContract" name="listGrantContractByCriteriaForm" property="beginContract"/>
<bean:define id="endContract" name="listGrantContractByCriteriaForm" property="endContract"/>
<bean:define id="grantTypeId" name="listGrantContractByCriteriaForm" property="grantTypeId"/>
<bean:define id="validToTheDate" name="listGrantContractByCriteriaForm" property="validToTheDate"/>

	<table align="center">
	<tr>
		<logic:present name="beforeSpan">
		<td>
		<html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate %>' > 
			   	<bean:message key="link.grant.owner.list.before.page"/>
			</html:link>
		</td>
		</logic:present>
		<td>&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.spanNumber" property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>&nbsp;</td>
		<logic:present name="afterSpan">
		<td>
	        <html:link page='<%= "/listGrantContractByCriteria.do?method=prepareListGrantContractByCriteria&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;orderBy=" + orderBy + "&amp;totalElements=" + totalElements + "&amp;argsInRequest=1&amp;filterType=" + filterType + "&amp;beginContract=" + beginContract + "&amp;endContract=" + endContract + "&amp;grantTypeId=" + grantTypeId  + "&amp;validToTheDate=" + validToTheDate  %>' > 
			   	<bean:message key="link.grant.owner.list.after.page"/>
			</html:link>
		</td>
		</logic:present>
	</tr>
	</table>
</html:form>
</logic:present>
</logic:messagesNotPresent>