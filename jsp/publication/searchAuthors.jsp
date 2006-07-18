<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>

<html:form action="/insertNewPublication.do" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAuthor" property="selectedAuthor" />

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.creatorId" property="creatorId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.title" property="title" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.description" property="description" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPublicationTypeId" property="infoPublicationTypeId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.subtype" property="subtype" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.journalName" property="journalName" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.volume" property="volume" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.firstPage" property="firstPage" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.lastPage" property="lastPage" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.language" property="language" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.format" property="format" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.observation" property="observation" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.month" property="month" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year" property="year" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.month_end" property="month_end" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year_end" property="year_end" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.editor" property="editor" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.country" property="country" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.issn" property="issn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.scope" property="scope" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.url" property="url" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.editorCity" property="editorCity" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.numberPages" property="numberPages" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.edition" property="edition" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.fascicle" property="fascicle" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.serie" property="serie" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.isbn" property="isbn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.local" property="local" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.conference" property="conference" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.instituition" property="instituition" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originalLanguage" property="originalLanguage" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.translatedAuthor" property="translatedAuthor" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.criticizedAuthor" property="criticizedAuthor" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.publicationType" property="publicationType" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.university" property="university" />

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.index" property="index"/>


	<bean:define id="authorsNameList" name="insertPublicationForm" property="authorsName" type="String[]" />

	<logic:iterate id="authorId" name="insertPublicationForm" property="authorsId" indexId="authorsIndex">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsId" property="authorsId" value="<%= authorId.toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsName" property="authorsName" value="<%= authorsNameList[authorsIndex.intValue()] %>" />		
	</logic:iterate>

	<span class="error"><html:errors /></span >

	<p>
		<bean:message key="message.publication.stringNomeSearch"/>: 
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.searchedAuthorName" property="searchedAuthorName" />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='searchAuthor';" %>' >Procurar</html:submit>
	</p>

	<logic:present name="searchedAuthorsList">
		<table>
			<logic:iterate id="author" name="searchedAuthorsList">
				<bean:define id="authorId" name="author" property="idInternal" type="Integer"/>
				<tr>
					<td>
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.selectedAuthor.value="+ authorId.toString() +";this.form.method.value='selectAuthor';" %>' >Escolher</html:submit>
					</td>
					<td>
						<bean:write name="author" property="author" />
						<logic:notEmpty name="author" property="organization">
							 - <bean:write name="author" property="organization" /> (Pessoa externa)
						</logic:notEmpty>
					</td>
				</tr>
			</logic:iterate>	
		</table>
		<b>Inserir autor externo:</b>
		<table>
			<tr>
				<td>
					Autor
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.authorName" property="authorName" />
				</td>
			</tr>
			<tr>
				<td>
					Organiza��o
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.authorOrganization" property="authorOrganization" />
				</td>
			</tr>
			<tr>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='externalAuthor';" %>' >
						Introduzir
					</html:submit>
				</td>
			</tr>
		</table>
	</logic:present>


</html:form>
