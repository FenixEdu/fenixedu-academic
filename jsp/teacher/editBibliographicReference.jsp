<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:form action="/bibliographicReferenceManager">
    <fieldset style="width: 500; height: 315; padding: 2">
    <legend>
    	<b><u>
    	<font size="4">
    		<bean:message key="message.insertBibliographyData"/>
    	</font>
    	</u></b>
    </legend>
    
    <p><bean:message key="message.bibliographicReferenceTitle"/>
	<b><html:errors property="title"/></b></p>
    <p><html:textarea rows="4" cols="56" property="title"/></p>
    <p><bean:message key="message.bibliographicReferenceAuthors"/></p>
    <p><html:textarea rows="4" cols="56" property="authors"/></p>
    <p><bean:message key="message.bibliographicReferenceReference"/></p>
    <p><html:textarea rows="2" cols="56" property="reference"/></p>
    <p><bean:message key="message.bibliographicReferenceYear"/>
    <b><html:errors property="year"/></b></p>
    <p><html:text property="year"/></p>
    <p><bean:message key="message.bibliographicReferenceOptional"/><b></p>
    <p><html:checkbox property="optional" value="yes"/></p>
    <p><center>
    
    <logic:equal name="edit" scope="session" value="Inserir">
   	 	<html:submit property="method">
    	   	<bean:message key="button.confirmInsert"/>                    		         	
    	</html:submit>       
    </logic:equal>
    
    <logic:equal name="edit" scope="session" value="Editar">
   	 	<html:submit property="method">
    	   	<bean:message key="button.confirmEdit"/>                    		         	
    	</html:submit>       
    </logic:equal>	
    	
    </center></p>
    </fieldset>
</html:form>
