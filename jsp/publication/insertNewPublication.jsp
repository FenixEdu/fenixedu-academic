<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>

<html:form action="/insertNewPublication.do" >
	<html:hidden property="page" value="1"/>
	<html:hidden property="method"/>
	<html:hidden property="index" />

	<html:hidden property="creatorId" />
	<bean:define id="creatorId" name="insertPublicationForm" property="creatorId" type="Integer" />

	<bean:define id="authorsNameList" name="insertPublicationForm" property="authorsName" type="String[]" />
	<bean:define id="authorsId" name="insertPublicationForm" property="authorsId" type="Integer[]" />

	<logic:iterate id="authorId" name="insertPublicationForm" property="authorsId" indexId="authorsIndex">
		<html:hidden property="authorsId" value="<%= authorId.toString() %>" />
		<html:hidden property="authorsName" value="<%= authorsNameList[authorsIndex.intValue()] %>" />		
	</logic:iterate>

	<span class="error"><html:errors /></span > <br />

	<b>Autores:</b><br />
	<table>

		<bean:define id="singleAuthor" type="String" value="<%= ""+ (authorsNameList.length == 1) %>"/>

		<logic:iterate id="authorN" name="insertPublicationForm" property="authorsName" indexId="authorsIndex">
			<tr>
				<td>
					<logic:notEqual name="authorsIndex" value="0">
						<html:submit styleClass="inputbutton" style="width: 30px" onclick='<%= "this.form.method.value='up';this.form.index.value=" + authorsIndex + ";" %>' >				
							/\
						</html:submit>
					</logic:notEqual>
				</td>
				<td>
					<logic:notEqual name="authorsIndex" value="<%=""+ (authorsNameList.length - 1)%>">
						<html:submit styleClass="inputbutton" style="width: 30px" onclick='<%= "this.form.method.value='down';this.form.index.value=" + authorsIndex + ";" %>' >				
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
							<html:submit styleClass="inputbutton" onclick='<%= "this.form.method.value='remove';this.form.index.value=" + authorsIndex + ";" %>' >				
								Remover
							</html:submit>
						</logic:notEqual>
					</td>
				</logic:equal>
			</tr>
			
		</logic:iterate>	

	</table>
	
	<p><html:submit styleClass="inputbutton" onclick='<%= "this.form.method.value='prepareSearchAuthor';" %>' >Inserir autor</html:submit></p>

	<table>
		<tr>
			<td>
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.type" />
			</td>
			<td>
				<html:select property="infoPublicationTypeId" onchange="this.form.method.value='prepare';this.form.submit();">
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

	</table>
	<br/>
	<table>
		<tr>
			<td>
				<html:submit styleClass="inputbutton" onclick='<%= "this.form.method.value='insert'" %>' >				
					Criar
				</html:submit>
			</td>
			<td>
				<html:submit styleClass="inputbutton" onclick='<%= "this.form.method.value='cancel'" %>' >				
					Cancelar
				</html:submit>
			</td>
		</tr>
	</table>

</html:form>
