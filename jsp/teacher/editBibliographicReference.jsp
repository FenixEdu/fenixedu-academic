<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:form action="/bibliographicReferenceManager">
   	<html:hidden property="page" value="1"/>
    <fieldset style="width: 500; height: 315; padding: 2">
    <legend>
    	<b><u>
    	<font size="4">
    		<bean:message key="message.insertBibliographyData"/>
    	</font>
    	</u></b>
    </legend>
    
    <bean:message key="message.bibliographicReferenceTitle"/>
	<b><html:errors property="title"/></b>
    <p><html:textarea rows="4" cols="56" property="title"/></p>
    
    <bean:message key="message.bibliographicReferenceAuthors"/>
   	<b><html:errors property="authors"/></b>
    <p><html:textarea rows="4" cols="56" property="authors"/></p>

    <bean:message key="message.bibliographicReferenceReference"/>
	<b><html:errors property="reference"/></b>
    <p><html:textarea rows="2" cols="56" property="reference"/></p>

    <bean:message key="message.bibliographicReferenceYear"/>
    <b><html:errors property="year"/></b>
    <p><html:text property="year"/></p>
    
    <bean:message key="message.bibliographicReferenceOptional"/>
    <p><html:checkbox property="optional" value="yes"/></p>
    <p>
    
    <center>
    
    <logic:notPresent name="edit">
    	<html:hidden property="method" value="createBibliographicReference"/>
   	 	<html:submit>
    	   	<bean:message key="button.confirm"/>                    		         	
    	</html:submit>       
    </logic:notPresent>
    
    <logic:present name="edit">
       	<html:hidden property="method" value="editBibliographicReference"/>
   	 	<html:submit>
    	   	<bean:message key="button.confirm"/>                    		         	
    	</html:submit>       
    </logic:present>	
    	
    </center></p>
    </fieldset>
</html:form>
