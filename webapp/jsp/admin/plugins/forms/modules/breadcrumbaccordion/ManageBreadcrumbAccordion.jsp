<jsp:useBean id="breadcrumbAccordion" scope="session" class="fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.web.BreadcrumbAccordionJspBean" />
<% String strContent = breadcrumbAccordion.processController ( request, response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
