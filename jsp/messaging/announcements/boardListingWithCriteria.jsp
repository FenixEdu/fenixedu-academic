<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/></em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board.announcements"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<html:form action="/announcements/announcementsStartPageHandler.do" method="get">
	<html:hidden property="method" value="handleBoardListing"/>
	<e:labelValues id="levelValues" bundle="ENUMERATION_RESOURCES" enumeration="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessLevel" /> 
	<e:labelValues id="typeValues" bundle="ENUMERATION_RESOURCES" enumeration="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessType" /> 
	
	<p class="mbottom025"><strong><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board.filtering"/></strong></p>
	<table class="tstyle5 thlight thright mtop0">
	<tr>
		<td>Privilégios:</td>
		<td>
		<html:select property="announcementBoardAccessLevel" onchange="this.form.submit();">
	        <html:options collection="levelValues" property="value" labelProperty="label" />
		</html:select>
		</td>
	</tr>
	<tr>
		<td>Tipo:</td>
		<td>
	    <html:select property="announcementBoardAccessType" onchange="this.form.submit();">
	        <html:options collection="typeValues" property="value" labelProperty="label" />
	    </html:select>
		</td>
	</tr>
	</table>
	
</html:form>

<h3 class="mbottom05">Canais de Unidades</h3>
<logic:present name="unitAnnouncementBoards">	
	<logic:notEmpty name="unitAnnouncementBoards">
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="unitAnnouncementBoards" name="unitAnnouncementBoards" type="java.util.Collection<net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard>"/>
		<bean:define id="extraParameters" name="extraParameters" />
		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

		<%							
			int indexOfLastSlash = contextPrefix.lastIndexOf("/");
			int indexOfDot = contextPrefix.lastIndexOf(".");
			String prefix = contextPrefix.substring(0,indexOfLastSlash+1);
			String suffix = contextPrefix.substring(indexOfDot,contextPrefix.length());
		%>
	
		<%
			boolean canManageAtLeastOneUnitBoard = false;
			boolean atLeastOneUnitBoardIsPublic = false;
			boolean canWriteAtLeastOneUnitBoard = false;
			
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: unitAnnouncementBoards) {
				if (announcementBoard.hasWriter(person)) {
					canWriteAtLeastOneUnitBoard = true;
					break;
				}
			}
			
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: unitAnnouncementBoards) {
				if (announcementBoard.getReaders() == null) {
					atLeastOneUnitBoardIsPublic = true;
					break;
				}
			}	
			
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: unitAnnouncementBoards) {
				if (announcementBoard.hasManager(person)) {
					canManageAtLeastOneUnitBoard = true;
					break;
				}
			}
	
		%>
	Página:
 	  <%  
   	   int lastPageUnitBoards = ((Integer) request.getAttribute("numberOfPagesUnitBoards")).intValue();
       int pageNumberUnitBoards = ((Integer) request.getAttribute("pageNumberUnitBoards")).intValue();
       for (int i = 1; i <= lastPageUnitBoards; i++) {
	       if (i == 1 && i != pageNumberUnitBoards) {
       %>
    	   <bean:define id="url11" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberUnitBoards=<%=Integer.toString(pageNumberUnitBoards - 1) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
       	   <html:link page="<%= url11 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.previous"/></html:link>
       <%
           }
           if (i != pageNumberUnitBoards) {
       %>
           <bean:define id="url12" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberUnitBoards=<%= Integer.toString(i) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
	       <html:link page="<%= url12 %>"><%= Integer.toString(i) %></html:link>
       <%
              } else {
       %>
              	<%= Integer.toString(i) %>
       <%
                  }
              if (i == lastPageUnitBoards && i != pageNumberUnitBoards) {
       %>
           	<bean:define id="url13" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberUnitBoards=<%= Integer.toString(pageNumberUnitBoards + 1) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
                <html:link page="<%= url13 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.next"/></html:link>
       <%     }
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
				<th>
					Favoritos
				</th>					
				<%
				if (canManageAtLeastOneUnitBoard)
				{
				%>
				<th>
					Permissões
				</th>
				<%
				}
				%>
				<%
				if (atLeastOneUnitBoardIsPublic)
				{
				%>
					<th>
						RSS
					</th>				
				<%
				}
				%>				
			</tr>
			<logic:iterate name="unitAnnouncementBoards" id="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard">
					<%
					boolean ableToRead = announcementBoard.getReaders() == null || announcementBoard.getReaders().isMember(person);
					boolean ableToWrite = announcementBoard.getWriters() == null || announcementBoard.getWriters().isMember(person);				
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
						else
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
						<td class="acenter">
							<logic:empty name="announcementBoard" property="readers">
								Público
							</logic:empty>
							<logic:notEmpty name="announcementBoard" property="readers">
								Privado
							</logic:notEmpty>					
						</td>
					<%
					if (!announcementBoard.getBookmarkOwner().contains(person)) {
					%>						
						<td class="acenter">
							Não 
							(<html:link page="<%= contextPrefix + "method=addBookmark" + "&" + extraParameters +"&announcementBoardId="+announcementBoard.getIdInternal() + "&returnAction=" + request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")%>">Adicionar</html:link>)
						</td>									
					<%
					} else {
					%>
						<td class="acenter">
							Sim 
							(<html:link page="<%= contextPrefix + "method=removeBookmark" + "&" + extraParameters +"&announcementBoardId="+announcementBoard.getIdInternal() + "&returnAction=" + request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")%>">Remover</html:link>)
						</td>
					<%
					} 
					%>						
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
					else if (canManageAtLeastOneUnitBoard)
					{
					%>
						<td>
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
						else if (atLeastOneUnitBoardIsPublic)
						{					
						%>					
							<td>
							</td>
						<%
						}
						%>
				</tr>
				</tr>
			</logic:iterate>
		</table>
		Página:		
		 <%  
	   	   lastPageUnitBoards = ((Integer) request.getAttribute("numberOfPagesUnitBoards")).intValue();
	       pageNumberUnitBoards = ((Integer) request.getAttribute("pageNumberUnitBoards")).intValue();
	       for (int i = 1; i <= lastPageUnitBoards; i++) {
		       if (i == 1 && i != pageNumberUnitBoards) {
	       %>
	    	     <bean:define id="url21" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberUnitBoards=<%=Integer.toString(pageNumberUnitBoards - 1) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
	        	 <html:link page="<%= url21 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.previous"/></html:link>
	       <%
	           }
	           if (i != pageNumberUnitBoards) {
	       %>
	             <bean:define id="url22" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberUnitBoards=<%= Integer.toString(i) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
		             <html:link page="<%= url22 %>"><%= Integer.toString(i) %></html:link>
	       <%
	              } else {
	       %>
	              	<%= Integer.toString(i) %>
	       <%
	                  }
	              if (i == lastPageUnitBoards && i != pageNumberUnitBoards) {
	       %>
	             <bean:define id="url23" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberUnitBoards=<%= Integer.toString(pageNumberUnitBoards + 1) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
	                <html:link page="<%= url23 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.next"/></html:link>
	       <%     }
	      	}
	       %>		
	</logic:notEmpty>
	<logic:empty name="unitAnnouncementBoards">
			Não existem canais de unidade<br/>
	</logic:empty>
</logic:present>

<h3 class="mtop2 mbottom05">Canais de Disciplinas</h3>

<logic:present name="executionCourseAnnouncementBoards">	
	<logic:notEmpty name="executionCourseAnnouncementBoards">
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="executionCourseAnnouncementBoards" name="executionCourseAnnouncementBoards" type="java.util.Collection<net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard>"/>
		<bean:define id="extraParameters" name="extraParameters" />
		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

		<%							
			int indexOfLastSlash = contextPrefix.lastIndexOf("/");
			int indexOfDot = contextPrefix.lastIndexOf(".");
			String prefix = contextPrefix.substring(0,indexOfLastSlash+1);
			String suffix = contextPrefix.substring(indexOfDot,contextPrefix.length());
		%>
	
		<%
			boolean canManageAtLeastOneExecutionCourseBoard = false;
			boolean atLeastOneExecutionCourseBoardIsPublic = false;
			boolean canWriteAtLeastOneExecutionCourseBoard = false;
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: executionCourseAnnouncementBoards)
			{
				if (announcementBoard.getWriters() == null || announcementBoard.getWriters().isMember(person))
				{
					canWriteAtLeastOneExecutionCourseBoard = true;
					break;
				}
			}
			
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: executionCourseAnnouncementBoards)
			{
			if (announcementBoard.getReaders() == null)
				{
					atLeastOneExecutionCourseBoardIsPublic = true;
					break;
				}
			}						
			for(net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard announcementBoard: executionCourseAnnouncementBoards)
			{
				if (announcementBoard.getManagers() == null || announcementBoard.getManagers().isMember(person))
				{
					canManageAtLeastOneExecutionCourseBoard = true;
					break;
				}
			}
	
		%>

	Página:
 	  <%  
   	   int lastPageExecutionCourseBoards = ((Integer) request.getAttribute("numberOfPagesExecutionCourseBoards")).intValue();
       int pageNumberExecutionCourseBoards = ((Integer) request.getAttribute("pageNumberExecutionCourseBoards")).intValue();
       for (int i = 1; i <= lastPageExecutionCourseBoards; i++) {
	       if (i == 1 && i != pageNumberExecutionCourseBoards) {
       %>
    	     <bean:define id="url31" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberExecutionCourseBoards=<%=Integer.toString(pageNumberExecutionCourseBoards - 1) %>&method=handleBoardListing<%=extraParameters%></bean:define>
        	 <html:link page="<%= url31 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.previous"/></html:link>
       <%
           }
           if (i != pageNumberExecutionCourseBoards) {
       %>
             <bean:define id="url32" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberExecutionCourseBoards=<%= Integer.toString(i) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
	             <html:link page="<%= url32 %>"><%= Integer.toString(i) %></html:link>
       <%
              } else {
       %>
              	<%= Integer.toString(i) %>
       <%
                  }
              if (i == lastPageExecutionCourseBoards && i != pageNumberExecutionCourseBoards) {
       %>
             <bean:define id="url33" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberExecutionCourseBoards=<%= Integer.toString(pageNumberExecutionCourseBoards + 1) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
                <html:link page="<%= url33 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.next"/></html:link>
       <%     }
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
				<th>
					Favoritos
				</th>
				<%
				if (canManageAtLeastOneExecutionCourseBoard)
				{
				%>
				<th>
					Permissões
				</th>
				<%
				}
				%>
				<%
				if (atLeastOneExecutionCourseBoardIsPublic)
				{
				%>
					<th>
						RSS
					</th>				
				<%
				}
				%>				
			</tr>
			<logic:iterate name="executionCourseAnnouncementBoards" id="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard">
				<tr>
					<td style="text-align: left;">
					<%
					boolean ableToRead = announcementBoard.getReaders() == null || announcementBoard.getReaders().isMember(person);
					boolean ableToWrite = announcementBoard.getWriters() == null || announcementBoard.getWriters().isMember(person);				
					if (ableToRead)
						{
					%>
							<html:link title="<%= announcementBoard.getQualifiedName()%>" page="<%= contextPrefix + "method=viewAnnouncements" +"&" +extraParameters +"&announcementBoardId="+announcementBoard.getIdInternal()%>">
								<bean:write name="announcementBoard" property="name"/>
							</html:link>
						<%
						}
						else
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
					<td class="acenter">
						<logic:empty name="announcementBoard" property="readers">
							Público
						</logic:empty>
						<logic:notEmpty name="announcementBoard" property="readers">
							Privado
						</logic:notEmpty>					
					</td>
					<%
					if (!announcementBoard.getBookmarkOwner().contains(person))
					{
					%>						
					<td class="acenter">
						Não 
						(<html:link page="<%= contextPrefix + "method=addBookmark" + "&" + extraParameters +"&announcementBoardId="+announcementBoard.getIdInternal() + "&returnAction="+request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")%>">Adicionar</html:link>)
					</td>									
					<%
					}
					else
					{
					%>
					<td class="acenter">
						Sim 
						(<html:link page="<%= contextPrefix + "method=removeBookmark" + "&" + extraParameters +"&announcementBoardId="+announcementBoard.getIdInternal() + "&returnAction="+request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")%>">Remover</html:link>)
					</td>										
					<%				
					}
					%>
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
						else if (canManageAtLeastOneExecutionCourseBoard)
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
						else if (atLeastOneExecutionCourseBoardIsPublic)
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
		Página:
			<%  
   	  	 		lastPageExecutionCourseBoards = ((Integer) request.getAttribute("numberOfPagesExecutionCourseBoards")).intValue();
      	 		pageNumberExecutionCourseBoards = ((Integer) request.getAttribute("pageNumberExecutionCourseBoards")).intValue();
	       for (int i = 1; i <= lastPageExecutionCourseBoards; i++) {
		       if (i == 1 && i != pageNumberExecutionCourseBoards) {
	       %>
	    	     <bean:define id="url41" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberExecutionCourseBoards=<%=Integer.toString(pageNumberExecutionCourseBoards - 1) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
	        	 <html:link page="<%= url41 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.previous"/></html:link>
	       <%
	           }
	           if (i != pageNumberExecutionCourseBoards) {
	       %>
	             <bean:define id="url42" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberExecutionCourseBoards=<%= Integer.toString(i) %>&method=handleBoardListing&<%=extraParameters%>&<%=extraParameters%></bean:define>
		             <html:link page="<%= url42 %>"><%= Integer.toString(i) %></html:link>
	       <%
	              } else {
	       %>
	              	<%= Integer.toString(i) %>
	       <%
	                  }
	              if (i == lastPageExecutionCourseBoards && i != pageNumberExecutionCourseBoards) {
	       %>
	             <bean:define id="url43" type="java.lang.String">/announcements/announcementsStartPageHandler.do?amp;page=0&pageNumberExecutionCourseBoards=<%= Integer.toString(pageNumberExecutionCourseBoards + 1) %>&method=handleBoardListing&<%=extraParameters%></bean:define>
	                <html:link page="<%= url43 %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.next"/></html:link>
	       <%     }
	      	}
	       %>
	</logic:notEmpty>
	<logic:empty name="executionCourseAnnouncementBoards">
		<p><em class="warning0">Não existem canais de disciplinas com o critério escolhido</em></p>
	</logic:empty>
</logic:present>