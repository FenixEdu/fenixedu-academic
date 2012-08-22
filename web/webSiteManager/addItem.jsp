<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
   <tiles:put name="navGeral" value="/webSiteManager/commonNavGeral.jsp" />  
  <tiles:put name="body" value="/webSiteManager/addItem_bd.jsp" />
  <tiles:put name="navLocal" value="/webSiteManager/sectionsNavbar.jsp" type="page"/>
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>