DROP TABLE IF EXISTS SEMINARY;
CREATE TABLE SEMINARY(
id_internal INT (11) NOT NULL,
ACKOPTLOCK int(11),
name VARCHAR (255) NOT NULL,
description TEXT,
allowed_candidacies_per_student INT(11),
PRIMARY KEY (id_internal),
UNIQUE (name)
)type=InnoDB;

DROP TABLE IF EXISTS SEMINARY_THEME;
CREATE TABLE SEMINARY_THEME
(
id_internal INT(11) NOT NULL,
ACKOPTLOCK int(11),
name VARCHAR(255) NOT NULL, /*it is necessary to specify a length to the text variables that are used in a key (e.g., unique)*/
short_name TEXT,
description TEXT,
UNIQUE(name),
PRIMARY KEY (id_internal)
)type=InnoDB;

DROP TABLE IF EXISTS SEMINARY_MODALITY;
CREATE TABLE SEMINARY_MODALITY
(
id_internal INT(11) NOT NULL,
ACKOPTLOCK int(11),
name VARCHAR(255) NOT NULL, /*it is necessary to specify a length to the text variables that are used in a key (e.g., unique)*/
description TEXT,
PRIMARY KEY (id_internal),
UNIQUE (name)
)type=InnoDB;

DROP TABLE IF EXISTS SEMINARY_CASESTUDY;
CREATE TABLE SEMINARY_CASESTUDY
(
id_internal INT(11) NOT NULL,
ACKOPTLOCK int(11),
name VARCHAR(255) NOT NULL, /*it is necessary to specify a length to the text variables that are used in a key (e.g., unique)*/
description TEXT,
code VARCHAR(32) NOT NULL,
seminary_theme_Id_Internal INT(11),
PRIMARY KEY (id_internal),
UNIQUE (name,seminary_theme_Id_Internal),
UNIQUE (code,seminary_theme_Id_Internal)
)type=InnoDB;

DROP TABLE IF EXISTS SEMINARY_CANDIDACY;
CREATE TABLE SEMINARY_CANDIDACY
(
id_internal INT(11) NOT NULL,
ACKOPTLOCK int(11),
student_id_internal INT(11) NOT NULL,
curricular_course_id_internal INT(11) NOT NULL,
seminary_id_internal INT(11) NOT NULL,
seminary_theme_id_internal INT(11), /*can be null if all themes are choosen (due to "Completa" modality)*/
seminary_modality_id_internal INT(11) NOT NULL,
motivation TEXT NOT NULL,
PRIMARY KEY (id_internal)
)type=InnoDB;

DROP TABLE IF EXISTS SEMINARYCANDIDACY_SEMINARYCASESTUDY;
CREATE TABLE SEMINARYCANDIDACY_SEMINARYCASESTUDY
(
ACKOPTLOCK int(11),
candidacy_id_internal INT(11) NOT NULL,
casestudy_id_internal INT(11) NOT NULL,
preference_order INT(3) NOT NULL,
PRIMARY KEY (candidacy_id_internal,casestudy_id_internal),
UNIQUE (candidacy_id_internal,preference_order)
)type=InnoDB;

DROP TABLE IF EXISTS SEMINARY_CURRICULARCOURSE;
CREATE TABLE SEMINARY_CURRICULARCOURSE
(
id_internal INT(11) NOT NULL,
ACKOPTLOCK int(11),
seminary_id_internal INT(11) NOT NULL,
curricular_course_id_internal INT(11) NOT NULL,
modality_id_internal INT(11) NOT NULL,
PRIMARY KEY (id_internal),
UNIQUE (seminary_id_internal,curricular_course_id_internal,modality_id_internal)
)type=InnoDB;

DROP TABLE IF EXISTS EQUIVALENCY_THEME;
CREATE TABLE EQUIVALENCY_THEME
(
theme_id INT(11) NOT NULL,
ACKOPTLOCK int(11),
equivalency_id INT(11) NOT NULL,
PRIMARY KEY (equivalency_id,theme_id)
)type=InnoDB;