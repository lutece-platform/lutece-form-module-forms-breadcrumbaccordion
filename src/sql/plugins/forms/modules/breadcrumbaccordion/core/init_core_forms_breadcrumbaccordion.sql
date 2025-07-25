--liquibase formatted sql
--changeset forms-breadcrumbaccordion:init_core_forms_breadcrumbaccordion.sql
--preconditions onFail:MARK_RAN onError:WARN
DELETE FROM core_admin_right WHERE id_right = 'BREADCRUMBACCORDION_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('BREADCRUMBACCORDION_MANAGEMENT','module.forms.breadcrumbaccordion.adminFeature.manageBreadcrumbAccordion.name',1,'jsp/admin/plugins/forms/modules/breadcrumbaccordion/ManageBreadcrumbAccordion.jsp','module.forms.breadcrumbaccordion.adminFeature.manageBreadcrumbAccordion.description',0,'forms-breadcrumbaccordion',NULL,NULL,NULL,4);