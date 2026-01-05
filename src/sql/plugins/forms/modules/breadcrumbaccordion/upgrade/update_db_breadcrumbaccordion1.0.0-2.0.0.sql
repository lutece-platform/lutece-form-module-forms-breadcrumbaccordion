--liquibase formatted sql
--changeset forms-breadcrumbaccordion:update_db_breadcrumbaccordion1.0.0-2.0.0.sql
--preconditions onFail:MARK_RAN onError:WARN
UPDATE core_admin_right SET icon_url='ti ti-line-dashed' WHERE id_right='BREADCRUMBACCORDION_MANAGEMENT';