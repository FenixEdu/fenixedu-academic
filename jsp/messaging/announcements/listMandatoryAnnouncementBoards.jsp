<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="mandatoryAnnouncementBoards">	

	<logic:notEmpty name="mandatoryAnnouncementBoards">
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="mandatoryAnnouncementBoards" name="mandatoryAnnouncementBoards" type="java.util.Collection<net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard>"/>
		<bean:define id="extraParameters" name="extraParameters" />
		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

		<%							
			int indexOfLastSlash = contextPrefix.lastIndexOf("/");
			int indexOfDot = contextPrefix.lastIndexOf(".");
			String prefix = contextPrefix.substring(0,indexOfLastSlash+1);
			String suffix = contextPrefix.substring(indexOfDot,contextPrefix.length());
		%>
	
		<%
			boolean canManageAtLeastOneBoard = false;
			boolean atLeastOneBoardIsPublic = false;
			boolean canWriteAtLeastOneBoard = false;
			
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: mandatoryAnnouncementBoards) {
				if (announcementBoard.hasWriter(person)) {
					canWriteAtLeastOneBoard = true;
					break;
				}
			}
			
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: mandatoryAnnouncementBoards) {
				if (announcementBoard.getReaders() == null) {
						atLeastOneBoardIsPublic = true;
						break;
				}
			}
			
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: mandatoryAnnouncementBoards) {
				if (announcementBoard.hasManager(person)) {
					canManageAtLeastOneBoard = true;
					break;
				}
			}		
		%>
	
		<table class="tstyle2 tdcenter mtop05">	
			<tr>
				<th>
					Nome
				</th>
				<th>
					<bean:message bundle="MESSAGING_RESOURCES" key="label.board.unit" />
				</th>
				<th>
					Tipo
				</th>

				<%
				if (canManageAtLeastOneBoard)
				{
				%>
				<th>
					Permissões
				</th>
				<%
				}
				%>
				<%
				if (atLeastOneBoardIsPublic)
				{
				%>
					<th>
						RSS
					</th>				
				<%
				}
				%>				
			</tr>
			<logic:iterate name="mandatoryAnnouncementBoards" id="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard">
				<%
					boolean ableToRead = announcementBoard.hasReader(person);
					boolean ableToWrite = announcementBoard.hasWriter(person);
				%>
				<tr>
					<td style="text-align: left;">
					<%
						if (ableToRead)
						{
						%>
							<html:link title="<%= announcementBoard.getQualifiedName()%>" page="<%= contextPrefix + "method=viewAnnouncements" +"&" +extraParameters +"&announcementBoardId="+announcementBoard.getIdInternal()%>">
								<bean:write name="announcementBoard" property="name"/>
							</html:link>
						<%
						}					
						%>
						<%
						if (!ableToRead)
						{
						%>
							<bean:write name="announcementBoard" property="name"/>
						<%
						}
						%>						
					</td>
					<td class="smalltxt2 lowlight1" style="text-align: left;">
						<bean:write name="announcementBoard" property="fullName"/>
					</td>
					<td>
						<logic:empty name="announcementBoard" property="readers">
							Público
						</logic:empty>
						<logic:notEmpty name="announcementBoard" property="readers">
							Privado
						</logic:notEmpty>					
					</td>

						<%
						if (announcementBoard.getManagers() == null || announcementBoard.getManagers().isMember(person))
						{
						%>
							<td class="acenter">
								<html:link page="<%= prefix +"manage" + announcementBoard.getClass().getSimpleName() + suffix + "method=prepareEditAnnouncementBoard" + "&" + extraParameters +"&announcementBoardId="+announcementBoard.getIdInternal() + "&returnAction="+request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")+"&tabularVersion=true"%>">
									Gerir
								</html:link>
							</td>
						<%
						}
						else if (canManageAtLeastOneBoard)
						{
						%>
							<td>
								&nbsp;
							</td>
						<%
						}
						java.util.Map parameters = new java.util.HashMap();
						parameters.put("method","simple");
						parameters.put("announcementBoardId",announcementBoard.getIdInternal());
						request.setAttribute("parameters",parameters);
						%>
						<%
						if (announcementBoard.getReaders() == null)
						{
						%>
						<td class="acenter">
							<html:link module="" action="/external/announcementsRSS" name="parameters">
									<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
							</html:link>				
						</td>						
						<%
						}
						else if (atLeastOneBoardIsPublic)
						{					
						%>					
							<td>
							</td>
						<%
						}
						%>
				</tr>
			</logic:iterate>
		</table>		
	</logic:notEmpty>
	<logic:empty name="mandatoryAnnouncementBoards">
		<em><bean:message key="view.announcementBoards.noBoardsToCurrentSelection" bundle="MESSAGING_RESOURCES"/></em>
	</logic:empty>
</logic:present>