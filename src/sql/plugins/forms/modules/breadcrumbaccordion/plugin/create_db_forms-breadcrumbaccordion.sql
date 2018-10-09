--
-- Structure for table forms_form
--

DROP TABLE IF EXISTS forms_breadcrumbaccordion_config_item;
CREATE TABLE forms_breadcrumbaccordion_config_item
(
	id_form INT DEFAULT 0 NOT NULL,
	id_step INT DEFAULT 0 NOT NULL,
	position INT DEFAULT 0 NOT NULL,
	
	PRIMARY KEY (id_form, id_step)
);