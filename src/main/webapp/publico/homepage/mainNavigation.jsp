<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<ul class="treemenu">

<logic:present name="actual$site">
	<fr:view name="actual$site" layout="side-menu">
            <fr:layout>
                <fr:property name="sectionUrl" value="/viewHomepage.do?method=section"/>
            </fr:layout>
	</fr:view>
</logic:present>

</ul>
