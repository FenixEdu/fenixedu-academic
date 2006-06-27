<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<bean:define id="publicationTypeString" name="publication" property="publicationTypeString" type="java.lang.String"/>
	<bean:define id="newAuthorshipsSchema" value="result.authorships" type="java.lang.String"/>
	<logic:present name="authorshipsSchema">
		<bean:define id="newAuthorshipsSchema" name="authorshipsSchema" type="java.lang.String"/>
	</logic:present>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.deletePublication"/></h2>
	
	<%-- Authorships --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultAuthorships"/></h3>
	<fr:view name="publication" property="resultAuthorships" schema="<%=newAuthorshipsSchema%>" layout="tabular">
		<fr:layout>
			<fr:property name="sortBy" value="authorOrder"/>
		</fr:layout>
	</fr:view>
	<br/>
	
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+publicationTypeString.toLowerCase()%>"/>)</h3>
 	<fr:view name="publication" layout="tabular" schema="<%="result.publication.details."+publicationTypeString.toLowerCase() %>">
 		<fr:layout name="tabular">
        	<fr:property name="classes" value="style1"/>
	       	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	   </fr:view>
	   <br /><bean:message key="researcher.result.publication.deletePublicationQuestion"/>
	
	<html:form action="/publications/publicationsManagement">
		<bean:define id="resultId" name="publication" property="idInternal"/>
		<html:hidden property="resultId" value="<%= resultId.toString() %>" />
		<html:hidden property="method" />
		
		<html:submit onclick='<%= "this.form.method.value='deletePublication' " %>'>
			<bean:message key="button.delete"/>
		</html:submit>

		<html:submit onclick='<%= "this.form.method.value='listPublications' " %>'>
			<bean:message key="button.cancel"/>
		</html:submit>

	</html:form>
</logic:present>
