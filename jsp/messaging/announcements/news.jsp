<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em>Portal de Comunicação</em>
<h2>Novidades</h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<h3>Últimos Anúncios dos Favoritos e Canais do IST</h3>

<html:form action="/announcements/announcementsStartPageHandler.do" method="get">
	<html:hidden property="method" value="news"/>
	<table class="tstyle5 mvert05">
		<tr>
			<td>Visualizar:</td>
			<td>
		    <html:select property="howManyAnnouncementsToShow" onchange="this.form.submit();">
		        <html:option value="6">6 anúncios (default) </html:option>
	   	        <html:option value="12">12 anúncios</html:option>
		        <html:option value="24">24 anúncios</html:option>
		    </html:select>
			</td>
		</tr>
	</table>
</html:form>

<jsp:include page="/messaging/announcements/listAnnouncements.jsp" flush="true"/>

<h3 class="mtop2 mbottom05">Últimas Boards Criadas</h3>
<html:form action="/announcements/announcementsStartPageHandler.do" method="get">
	<html:hidden property="method" value="news"/>
	<html:hidden property="howManyAnnouncementsToShow"/>
	<e:labelValues id="values" bundle="ENUMERATION_RESOURCES" enumeration="net.sourceforge.fenixedu.presentationTier.Action.messaging.RecentBoardsTimeSpanSelection" /> 
	<table class="tstyle5 mvert05">
		<tr>
			<td>Mostrar canais criados nos últimos:</td>
			<td>
			    <html:select property="recentBoardsTimeSpanSelection" onchange="this.form.submit();">
	        		<html:options collection="values" property="value" labelProperty="label" />
			    </html:select>
		    </td>
	    </tr>
    </table>
</html:form>
<jsp:include page="/messaging/announcements/listAnnouncementBoards.jsp" flush="true"/>
