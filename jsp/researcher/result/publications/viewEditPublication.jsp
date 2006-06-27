<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="publicationTypeString" name="publication" property="publicationTypeString" type="java.lang.String"/>
	<bean:define id="publicationId" name="publication" property="idInternal" />
	<bean:define id="newAuthorshipsSchema" value="result.authorships" type="java.lang.String"/>
	<logic:present name="authorshipsSchema">
		<bean:define id="newAuthorshipsSchema" name="authorshipsSchema" type="java.lang.String"/>
	</logic:present>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails"/></h2>

  	<br/>
	<%-- Authorships --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultAuthorships"/></h3>
	<fr:view name="publication" property="resultAuthorships" schema="<%=newAuthorshipsSchema%>" layout="tabular">
		<fr:layout>
			<fr:property name="sortBy" value="authorOrder"/>
		</fr:layout>
	</fr:view>
	<html:link page="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorship&resultId=" + publicationId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editAuthorships" />
	</html:link>
	<br/><br/>
	
	<%-- Data --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+publicationTypeString.toLowerCase()%>"/>)</h3>
	<fr:view name="publication" layout="tabular" schema="<%="result.publication.details."+publicationTypeString.toLowerCase() %>">
 		<fr:layout name="tabular">
    	   	<fr:property name="classes" value="style1"/>
	       	<fr:property name="columnClasses" value="listClasses,,"/>
		   </fr:layout>
	</fr:view>
	<html:link page="<%="/publications/publicationsManagement.do?method=prepareEditPublicationData&publicationId="+ publicationId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editResultData" />
	</html:link>
	
	<br/><br/>
	<html:link page="/publications/publicationsManagement.do?method=listPublications">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.backToPublications" />
	</html:link>
</logic:present>
<br/>