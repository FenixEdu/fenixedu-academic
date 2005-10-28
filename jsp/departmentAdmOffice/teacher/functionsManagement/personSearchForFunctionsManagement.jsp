<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	
	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
		
		<h:inputHidden binding="#{functionsManagementBackingBean.linkHidden}"/>
		 
		<h:outputText value="#{bundle['label.site.orientation1']}" escape="false" 
			rendered="#{!empty functionsManagementBackingBean.link == chooseUnit}"/>
			
		<h:outputText value="#{bundle['label.site.orientation5']}" escape="false" 
			rendered="#{empty functionsManagementBackingBean.link}"/>		
			
		<h:outputText value="<br/><br/>" escape="false" />
			
		<h:outputText value="<H2>#{bundle['label.search.person']}</H2>" escape="false" 
			rendered="#{!empty functionsManagementBackingBean.link}"/>
			
		<h:outputText value="<H2>#{bundle['label.search.person1']}</H2>" escape="false" 
			rendered="#{functionsManagementBackingBean.link != chooseUnit}"/>	
		
		<h:panelGrid columns="3" styleClass="infoop">
			<h:outputText value="#{bundle['label.find.person']}" escape="false"/>
			<h:inputText id="name" required="true" size="30" value="#{functionsManagementBackingBean.personName}"/>
			<h:commandButton action="#{functionsManagementBackingBean.searchPerson}" styleClass="inputbutton" value="#{bundle['button.searchPerson']}"/>
		</h:panelGrid>
		
		<h:message for="name" styleClass="error"/>
		
		<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
				value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
				
		<h:outputText value="<br/><br/><b>#{functionsManagementBackingBean.personsNumber}</b> #{bundle['label.number.of.persons.finded']}" 
			escape="false"/>			
				
		<h:outputText value="<br/><br/>" escape="false" />							
		
		<c:if test="${functionsManagementBackingBean.personsNumber > 0}">
			<c:if test="${functionsManagementBackingBean.numberOfPages != 1}">
				<c:out value="${bundle['label.page']}"/>
				<c:forEach begin="1" end="${functionsManagementBackingBean.numberOfPages}" var="pageIndex">
					<c:url var="url" value="/departmentAdmOffice/teacher/functionsManagement/personSearchForFunctionsManagement.faces?name=${functionsManagementBackingBean.personName}&page=${pageIndex}&link=${functionsManagementBackingBean.link}" />
					<c:choose>
						<c:when test="${pageIndex == functionsManagementBackingBean.numberOfPages}">						
							<c:choose>
								<c:when test="${functionsManagementBackingBean.page == pageIndex}">
									<c:out value="${pageIndex}"/>		
								</c:when>
								<c:otherwise>
									<a href="<c:out value="${url}"/> ">
										<c:out value="${pageIndex}" />
									</a>	
								</c:otherwise>
							</c:choose>
					    </c:when>
					    <c:otherwise>					    
							<c:choose>
								<c:when test="${functionsManagementBackingBean.page == pageIndex}">
									<c:out value="${pageIndex},"/>		
								</c:when>
								<c:otherwise>
									<a href="<c:out value="${url}"/> ">
										<c:out value="${pageIndex}," />
									</a>	
								</c:otherwise>
							</c:choose>		    
					    </c:otherwise>					
					</c:choose>							
				</c:forEach>							
				<c:out value="<br/><br/>" escapeXml="false" />
			</c:if>
		</c:if>
		
		<%-- 
		<h:panelGroup rendered="#{!empty functionsManagementBackingBean.personsList}">
			<h:outputText value="<ul>" escape="false" />
			<h:dataTable value="#{functionsManagementBackingBean.personsList}" var="person">		
				<h:column>
					<h:outputText value="<li>" escape="false" />				
						<h:outputLink value="#{functionsManagementBackingBean.contextPath}/departmentAdmOffice/teacher/functionsManagement/unitChoose.faces?personID=#{person.idInternal}">
							<h:outputText value="#{person.nome}"/>
						</h:outputLink>		
					<h:outputText value="</li>" escape="false"/>
				</h:column>		
			</h:dataTable>
			<h:outputText value="</ul>" escape="false" />
		</h:panelGroup>		
		--%>
		
		<c:if test="${functionsManagementBackingBean.personsNumber > 0}">
			<c:out value="<ul>" escapeXml="false"/>	
			<c:forEach items="${functionsManagementBackingBean.personsList}" var="person">
				<c:url var="url" value="/departmentAdmOffice/teacher/functionsManagement/${functionsManagementBackingBean.link}.faces?personID=${person.idInternal}&link=${functionsManagementBackingBean.link}" />
				<c:out value="<li>" escapeXml="false"/>
				<a href=" <c:out value="${url}"/> ">
					<c:out value="${person.nome}" />
				</a>
				<c:out value="</li>" escapeXml="false"/>										
			</c:forEach>
			<c:out value="</ul>" escapeXml="false"/>
		</c:if>
		
	</h:form>		
</ft:tilesView>