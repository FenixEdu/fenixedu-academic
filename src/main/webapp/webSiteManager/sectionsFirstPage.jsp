<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
   <tiles:put name="navGeral" value="/webSiteManager/commonNavGeral.jsp" />  
  <tiles:put name="body" value="/webSiteManager/sectionsFirstPage_bd.jsp" />
  <tiles:put name="navLocal" value="/webSiteManager/sectionsNavbar.jsp" type="page"/>
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>