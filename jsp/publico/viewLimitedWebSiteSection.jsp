<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="net.sourceforge.fenixedu.util.Mes" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="infoWebSiteSection">
	<table width="100%" align="center">
		<logic:iterate id="item" name="infoWebSiteSection" property="infoItemsList">
			<tr>
				<td align="left">
					<bean:write name="item" property="title"/>
				</td>	   
			</tr> 	
			<logic:notEmpty name="item" property="itemBeginDayCalendar">
				<tr>
					<td align="left">
						<bean:define id="itemBeginDayCalendar" name="item" property="itemBeginDayCalendar" type="java.util.Calendar"/>
						<bean:define id="itemEndDayCalendar" name="item" property="itemEndDayCalendar" type="java.util.Calendar"/>
						<bean:message key="label.from"/>&nbsp;
						<%=String.valueOf(itemBeginDayCalendar.get(Calendar.DAY_OF_MONTH))%>					
						<bean:define id="beginMonthToCompare"><%=String.valueOf(itemBeginDayCalendar.get(Calendar.MONTH))%></bean:define>
						<bean:define id="endMonthToCompare"><%=String.valueOf(itemEndDayCalendar.get(Calendar.MONTH))%></bean:define>
						<logic:notEqual name="beginMonthToCompare" value="<%=endMonthToCompare%>">
							<%= new Mes(itemBeginDayCalendar.get(Calendar.MONTH) + 1)%>&nbsp;
						</logic:notEqual>
						<bean:define id="beginYearToCompare"><%=String.valueOf(itemBeginDayCalendar.get(Calendar.YEAR))%></bean:define>
						<bean:define id="endYearToCompare"><%=String.valueOf(itemEndDayCalendar.get(Calendar.YEAR))%></bean:define>
						<logic:notEqual name="beginYearToCompare" value="<%=endYearToCompare%>">
							<%=String.valueOf(itemBeginDayCalendar.get(Calendar.YEAR))%>&nbsp;
						</logic:notEqual>						
						<bean:message key="label.a"/>&nbsp;
						<%=String.valueOf(itemEndDayCalendar.get(Calendar.DAY_OF_MONTH))%>&nbsp;
						<%= (new Mes(itemEndDayCalendar.get(Calendar.MONTH) + 1)).toString()%>&nbsp;
						<%=String.valueOf(itemEndDayCalendar.get(Calendar.YEAR))%>
					</td>	   
				</tr> 	
			</logic:notEmpty>
			<tr>
				<td align="left">
					<bean:define id="objectCode" name="item" property="idInternal"/>
					<bean:define id="objectCode2" name="infoWebSiteSection" property="idInternal"/>
					<bean:write name="item" property="excerpt"/>
					(<html:link page="<%="/viewWebSiteSection.do?method=viewAllPublishedItemsFromSection&amp;objectCode=" + objectCode + "&amp;objectCode2=" + objectCode2 %>">
						<bean:message key="link.more"/>
					</html:link>)
					<br/><br/>
				</td>	   
			</tr> 	
		</logic:iterate>
	</table>    
</logic:present>   