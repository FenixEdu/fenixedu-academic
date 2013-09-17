<%@ page language="java" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<script language="JavaScript">
 window.resizeTo(800,390);
</script>

<logic:present name="bibtex">
<bean:define id="bibtex" name="bibtex" type="java.lang.String"/>
<pre><%= bibtex %></pre>
</logic:present>
	
<logic:notEmpty name="bibtexList">
	<logic:iterate id="bibtex" name="bibtexList" type="java.lang.String">
		<pre><%= bibtex %></pre>
	</logic:iterate>
</logic:notEmpty>



	
	 
	 
	
