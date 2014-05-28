<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
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
					<bean:define id="objectCode" name="item" property="externalId"/>
					<bean:define id="objectCode2" name="infoWebSiteSection" property="externalId"/>
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