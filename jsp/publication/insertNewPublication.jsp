<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>

<html:form action="/insertNewPublication.do" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.index" property="index" />

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.creatorId" property="creatorId" />
	<bean:define id="creatorId" name="insertPublicationForm" property="creatorId" type="Integer" />

	<bean:define id="authorsNameList" name="insertPublicationForm" property="authorsName" type="String[]" />
	<bean:define id="authorsId" name="insertPublicationForm" property="authorsId" type="Integer[]" />

	<logic:iterate id="authorId" name="insertPublicationForm" property="authorsId" indexId="authorsIndex">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsId" property="authorsId" value="<%= authorId.toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsName" property="authorsName" value="<%= authorsNameList[authorsIndex.intValue()] %>" />		
	</logic:iterate>

	<span class="error"><html:errors /></span > <br />

	<b>Autores:</b><br />
	<table>

		<bean:define id="singleAuthor" type="String" value="<%= ""+ (authorsNameList.length == 1) %>"/>

		<logic:iterate id="authorN" name="insertPublicationForm" property="authorsName" indexId="authorsIndex">
			<tr>
				<td>
					<logic:notEqual name="authorsIndex" value="0">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="width: 30px" onclick='<%= "this.form.method.value='up';this.form.index.value=" + authorsIndex + ";" %>' >				
							/\
						</html:submit>
					</logic:notEqual>
				</td>
				<td>
					<logic:notEqual name="authorsIndex" value="<%=""+ (authorsNameList.length - 1)%>">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="width: 30px" onclick='<%= "this.form.method.value='down';this.form.index.value=" + authorsIndex + ";" %>' >				
							\/
						</html:submit>
					</logic:notEqual>
				</td>
				<td>
					<%= authorN.toString().replaceAll("'.'","      -      ")+(authorN.toString().contains("'.'")?"        (Pessoa externa)":"") %>
				</td>
				
				<logic:equal name="singleAuthor" value="false">
					<td>
						<%-- the creator cannot remove himself when inserting --%>
						<logic:notEqual name="creatorId" value="<%= ""+authorsId[authorsIndex.intValue()] %>">
							<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='remove';this.form.index.value=" + authorsIndex + ";" %>' >				
								Remover
							</html:submit>
						</logic:notEqual>
					</td>
				</logic:equal>
			</tr>
			
		</logic:iterate>	

	</table>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='prepareSearchAuthor';" %>' >Inserir autor</html:submit></p>

	<table>
		<tr>
			<td>
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.type" />
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.infoPublicationTypeId" property="infoPublicationTypeId" onchange="this.form.method.value='prepare';this.form.submit();">
					<html:options collection="publicationTypesList" property="idInternal" labelProperty="publicationType"/>
				</html:select>
			</td>
		</tr>
		
		<hr/>
		

	
	    <%-- MAJOR ABSTRACTION BREACH under Prof. Rito's permission
	    		these values are idInternal's from the PublicationType table
	    		will be redone on the new architecture --%>

		<bean:define id="type" name="insertPublicationForm" property="infoPublicationTypeId" />
	
		<logic:equal name="type" value="1">
				<jsp:include page="publicationAttributesJournal.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="2">
				<jsp:include page="publicationAttributesBook.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="3">
				<jsp:include page="publicationAttributesThesis.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="4">
				<jsp:include page="publicationAttributesConference.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="5">
				<jsp:include page="publicationAttributesTechnicalReport.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="6">
				<jsp:include page="publicationAttributesPatent.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="7">
				<jsp:include page="publicationAttributesTranslation.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="8">
				<jsp:include page="publicationAttributesCritique.jsp" />
		</logic:equal>
	
		<logic:equal name="type" value="9">
				<jsp:include page="publicationAttributesOther.jsp" />
		</logic:equal>

		<logic:equal name="type" value="11">
				<jsp:include page="publicationAttributesUnstructured.jsp" />
		</logic:equal>


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

	</table>
	<br/>
	<table>
		<tr>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='insert'" %>' >				
					Criar
				</html:submit>
			</td>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='cancel'" %>' >				
					Cancelar
				</html:submit>
			</td>
		</tr>
	</table>

</html:form>
