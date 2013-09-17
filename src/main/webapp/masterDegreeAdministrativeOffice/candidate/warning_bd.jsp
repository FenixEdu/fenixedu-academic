<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
    <html:form action="/displayListToSelectCandidates.do?method=next">
        <h2>AVISO:<br />
        O número de candidatos aceites excede o limite de vagas.
        Deseja confirmar esta selecção?</h2>
        <br />
    	<logic:iterate id="candidate" name="candidatesID" indexId="indexCandidate">
	        <html:hidden alt='<%= "candidatesID[" + indexCandidate + "]" %>' property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
	        <html:hidden alt='<%= "situations[" + indexCandidate + "]" %>' property='<%= "situations[" + indexCandidate + "]" %>' />					
	        <html:hidden alt='<%= "remarks[" + indexCandidate + "]" %>' property='<%= "remarks[" + indexCandidate + "]" %>' />					
		</logic:iterate>    
  	    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Confirmar" styleClass="inputbutton" property="OK"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.notOK" value="Cancelar" styleClass="inputbutton" property="notOK"/>
    </html:form>	
	