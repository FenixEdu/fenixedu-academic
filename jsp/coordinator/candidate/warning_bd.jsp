<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<span class="error"><html:errors/></span>
    <html:form action="/displayListToSelectCandidates.do?method=next">
        <h2>AVISO:<br />
        O número de candidatos aceites excede o limite de vagas.
        Deseja confirmar esta selecção?</h2>
        <br />
    	<logic:iterate id="candidate" name="candidatesID" indexId="indexCandidate">
	        <html:hidden property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
	        <html:hidden property='<%= "situations[" + indexCandidate + "]" %>' />					
	        <html:hidden property='<%= "remarks[" + indexCandidate + "]" %>' />					
		</logic:iterate>    
  	    <html:submit value="Confirmar" styleClass="inputbutton" property="OK"/>
		<html:submit value="Cancelar" styleClass="inputbutton" property="notOK"/>
    </html:form>	
	