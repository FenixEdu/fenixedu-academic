<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>
<logic:present name="infoWebSiteSection">
	<table width="100%" align="center">
		<logic:iterate id="item" name="infoWebSiteSection" property="infoItemsList">
			<tr>
				<td align="left">
					<bean:write name="item" property="title"/>
				</td>	   
			</tr> 	
			<logic:notEmpty name="item" property="itemBeginDay">
				<tr>
					<td align="left">
						<bean:message key="label.from"/>&nbsp;<bean:write name="item" property="itemBeginDay"/>
						&nbsp;<bean:message key="label.until"/>&nbsp;<bean:write name="item" property="itemEndDay"/>
					</td>	   
				</tr> 	
			</logic:notEmpty>
			<tr>
				<td align="left">
					<bean:define id="objectCode" name="item" property="idInternal"/>
					<bean:write name="item" property="excerpt"/>
					(<html:link page="<%="/viewWebSiteSection.do?method=viewAllItemsFromSection&amp;objectCode=" + objectCode %>">
						<bean:message key="link.more"/>
					</html:link>)
					<br/><br/>
				</td>	   
			</tr> 	
		</logic:iterate>
	</table>    
</logic:present>   