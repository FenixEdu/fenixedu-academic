<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">

	<bean:define id="publications" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.researchResultPublications"/>
	<bean:define id="personId" name="person" property="idInternal"/>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></h2>

	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></strong></p>
		</html:messages>
	</logic:messagesPresent>

    <logic:notPresent name="preferredSetting">
		<ul class="list5 mbottom15">
			<li>
				<html:link page="/resultPublications/prepareCreate.do">
					<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.ResultPublication.insert.link"/>
				</html:link>
			</li>
			<li>
				<html:link page="/publications/bibtexManagement.do?method=prepareOpenBibtexFile">
					<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.result.publication.importBibtex"/>
				</html:link>
			</li>
			<li>
				<html:link target="_blank" page="<%="/publications/bibtexManagement.do?method=exportAllPublicationsToBibtex&amp;personOID=" + personId %>">
						<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportAllToBibTeX" />
				</html:link>
			</li>
	        <li>
	            <html:link page="/publications/management.do?method=prepareSetPreferredPublications">
	                <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.setPreferredPublications"/>
	            </html:link>
	        </li>
		</ul>
    </logic:notPresent>

    <logic:present name="preferredSetting">
        <h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.preferredPublications.title"/></h3>
		<div class="infoop2">
    		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.preferredPublications.help"/>
		</div>
    </logic:present>

	<logic:empty name="publications">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.emptyList"/></em></p>
	</logic:empty>
	
	<logic:notEmpty name="publications">
	
	<logic:present name="preferredSetting">
	   <fr:form action="/publications/management.do?method=setPreferredPublications">
	       <jsp:include page="publicationsCategories.jsp" />
	       <p>
		       <html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.save" /></html:submit>
		       <html:cancel><bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel" /></html:cancel>
	       </p>
        </fr:form>
    </logic:present>

    <logic:notPresent name="preferredSetting">
	    <jsp:include page="publicationsCategories.jsp" />
    </logic:notPresent>

	</logic:notEmpty>
</logic:present>