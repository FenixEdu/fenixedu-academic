<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>

<html:form action="/insertNewPublication.do" >
	<html:hidden property="method"/>
	<html:hidden property="idInternal" />
	<html:hidden property="selectedAuthor" />

	<html:hidden property="creatorId" />
	<html:hidden property="title" />
	<html:hidden property="description" />
	<html:hidden property="infoPublicationTypeId" />
	<html:hidden property="subtype" />
	<html:hidden property="journalName" />
	<html:hidden property="volume" />
	<html:hidden property="firstPage" />
	<html:hidden property="lastPage" />
	<html:hidden property="language" />
	<html:hidden property="format" />
	<html:hidden property="observation" />
	<html:hidden property="number" />
	<html:hidden property="month" />
	<html:hidden property="year" />
	<html:hidden property="month_end" />
	<html:hidden property="year_end" />
	<html:hidden property="editor" />
	<html:hidden property="country" />
	<html:hidden property="issn" />
	<html:hidden property="scope" />
	<html:hidden property="url" />
	<html:hidden property="editorCity" />
	<html:hidden property="numberPages" />
	<html:hidden property="edition" />
	<html:hidden property="fascicle" />
	<html:hidden property="serie" />
	<html:hidden property="isbn" />
	<html:hidden property="local" />
	<html:hidden property="conference" />
	<html:hidden property="instituition" />
	<html:hidden property="originalLanguage" />
	<html:hidden property="translatedAuthor" />
	<html:hidden property="criticizedAuthor" />
	<html:hidden property="publicationType" />
	<html:hidden property="university" />

	<html:hidden property="index"/>


	<bean:define id="authorsNameList" name="insertPublicationForm" property="authorsName" type="String[]" />

	<logic:iterate id="authorId" name="insertPublicationForm" property="authorsId" indexId="authorsIndex">
		<html:hidden property="authorsId" value="<%= authorId.toString() %>" />
		<html:hidden property="authorsName" value="<%= authorsNameList[authorsIndex.intValue()] %>" />		
	</logic:iterate>

	<span class="error"><html:errors /></span >

	<p>
		<bean:message key="message.publication.stringNomeSearch"/>: 
		<html:text property="searchedAuthorName" />
		<html:submit styleClass="inputbutton" onclick='<%= "this.form.method.value='searchAuthor';" %>' >Procurar</html:submit>
	</p>

	<logic:present name="searchedAuthorsList">
		<table>
			<logic:iterate id="author" name="searchedAuthorsList">
				<bean:define id="authorId" name="author" property="idInternal" type="Integer"/>
				<tr>
					<td>
						<html:submit styleClass="inputbutton" onclick='<%= "this.form.selectedAuthor.value="+ authorId.toString() +";this.form.method.value='selectAuthor';" %>' >Escolher</html:submit>
					</td>
					<td>
						<bean:write name="author" property="author" />
					</td>
					<td>
						<bean:write name="author" property="organization" />
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
					<html:text property="authorName" />
				</td>
			</tr>
			<tr>
				<td>
					Organização
				</td>
				<td>
					<html:text property="authorOrganization" />
				</td>
			</tr>
			<tr>
				<td>
					<html:submit styleClass="inputbutton" onclick='<%= "this.form.method.value='externalAuthor';" %>' >
						Introduzir
					</html:submit>
				</td>
			</tr>
		</table>
	</logic:present>


</html:form>
