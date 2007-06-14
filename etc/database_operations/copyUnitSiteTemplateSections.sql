--
-- Create Side Section if needed
--

SELECT concat(
	"INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, SECTION_ORDER, KEY_SITE) VALUES (",
	concat_ws(",",
		"'net.sourceforge.fenixedu.domain.Section'",
		1,
		"'en4:Sidept7:Lateral'",
		1,
		(
			SELECT COALESCE(MAX(A2.SECTION_ORDER), -1) + 1
			FROM ACCESSIBLE_ITEM A2
			WHERE A2.KEY_SITE = S.ID_INTERNAL
			  AND A2.KEY_SUPERIOR_SECTION IS NULL
		),
		S.ID_INTERNAL
	),
	");"
) AS "-- Create Section 'Side/Lateral' in unit sites that don't have one"
FROM SITE S
WHERE S.OJB_CONCRETE_CLASS IN ('net.sourceforge.fenixedu.domain.DepartmentSite', 'net.sourceforge.fenixedu.domain.ResearchUnitSite')
  AND NOT EXISTS (
	SELECT * FROM ACCESSIBLE_ITEM A
	WHERE A.KEY_SITE = S.ID_INTERNAL
	  AND A.KEY_SUPERIOR_SECTION IS NULL
	  AND A.NAME LIKE '%pt7:Lateral%'
  );

--- 
--- Copy Department Sections
---

-- Teachers
SELECT concat(
	"INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, KEY_SUPERIOR_SECTION, SECTION_ORDER, KEY_SITE, KEY_FUNCTIONALITY) VALUES (",
	concat_ws(",",
		concat("'", STS.OJB_CONCRETE_CLASS, "'"),
		1,
		coalesce(concat("'", STS.NAME, "'"), "NULL"),
		coalesce(STS.VISIBLE, 1),
		A.ID_INTERNAL,
		STS.SECTION_ORDER - 3, -- because of the removal of presentation, announcements, and events sections
		S.ID_INTERNAL,
		coalesce(STS.KEY_FUNCTIONALITY, "NULL")
	),
	");"
) AS "-- Department: Copy section teachers"
FROM ACCESSIBLE_ITEM A,   -- side section
     SITE S,              -- site
     SITE ST,             -- template site
     ACCESSIBLE_ITEM STS, -- template section
	 ACCESSIBLE_ITEM F    -- functionality
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.DepartmentSite'
  AND A.KEY_SITE = S.ID_INTERNAL
  AND A.KEY_SUPERIOR_SECTION IS NULL
  AND A.NAME LIKE '%pt7:Lateral%'
  AND ST.SITE_TYPE = S.OJB_CONCRETE_CLASS
  AND STS.KEY_SITE = ST.ID_INTERNAL
  AND STS.KEY_FUNCTIONALITY = F.ID_INTERNAL
  AND F.UUID = '173e00ae-66ff-408b-a8ce-d4a4011fe3f5'
  AND EXISTS (
  	SELECT * FROM SITE S1 
  	WHERE S1.ID_INTERNAL = S.ID_INTERNAL 
  	  AND S.SHOW_SECTION_TEACHERS = 1
  );

-- Employees
SELECT concat(
	"INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, KEY_SUPERIOR_SECTION, SECTION_ORDER, KEY_SITE, KEY_FUNCTIONALITY) VALUES (",
	concat_ws(",",
		concat("'", STS.OJB_CONCRETE_CLASS, "'"),
		1,
		coalesce(concat("'", STS.NAME, "'"), "NULL"),
		coalesce(STS.VISIBLE, 1),
		A.ID_INTERNAL,
		STS.SECTION_ORDER - (1 - S.SHOW_SECTION_TEACHERS) - 3, -- because of the removal of presentation, announcements, and events sections
		S.ID_INTERNAL,
		coalesce(STS.KEY_FUNCTIONALITY, "NULL")
	),
	");"
) AS "-- Department: Copy section employees"
FROM ACCESSIBLE_ITEM A,   -- side section
     SITE S,              -- site
     SITE ST,             -- template site
     ACCESSIBLE_ITEM STS, -- template section
	 ACCESSIBLE_ITEM F    -- functionality
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.DepartmentSite'
  AND A.KEY_SITE = S.ID_INTERNAL
  AND A.KEY_SUPERIOR_SECTION IS NULL
  AND A.NAME LIKE '%pt7:Lateral%'
  AND ST.SITE_TYPE = S.OJB_CONCRETE_CLASS
  AND STS.KEY_SITE = ST.ID_INTERNAL
  AND STS.KEY_FUNCTIONALITY = F.ID_INTERNAL
  AND F.UUID = '3bd0759b-edca-4ff7-87d6-6e03b6be1b3c'
  AND EXISTS (
  	SELECT * FROM SITE S1 
  	WHERE S1.ID_INTERNAL = S.ID_INTERNAL 
  	  AND S.SHOW_SECTION_EMPLOYEES = 1
  );

-- Degrees
SELECT concat(
	"INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, KEY_SUPERIOR_SECTION, SECTION_ORDER, KEY_SITE, KEY_FUNCTIONALITY) VALUES (",
	concat_ws(",",
		concat("'", STS.OJB_CONCRETE_CLASS, "'"),
		1,
		coalesce(concat("'", STS.NAME, "'"), "NULL"),
		coalesce(STS.VISIBLE, 1),
		A.ID_INTERNAL,
		STS.SECTION_ORDER - (1 - S.SHOW_SECTION_TEACHERS) - (1 - S.SHOW_SECTION_EMPLOYEES) - 3, -- because of the removal of presentation, announcements, and events sections
		S.ID_INTERNAL,
		coalesce(STS.KEY_FUNCTIONALITY, "NULL")
	),
	");"
) AS "-- Department: Copy section degrees"
FROM ACCESSIBLE_ITEM A,   -- side section
     SITE S,              -- site
     SITE ST,             -- template site
     ACCESSIBLE_ITEM STS, -- template section
	 ACCESSIBLE_ITEM F    -- functionality
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.DepartmentSite'
  AND A.KEY_SITE = S.ID_INTERNAL
  AND A.KEY_SUPERIOR_SECTION IS NULL
  AND A.NAME LIKE '%pt7:Lateral%'
  AND ST.SITE_TYPE = S.OJB_CONCRETE_CLASS
  AND STS.KEY_SITE = ST.ID_INTERNAL
  AND STS.KEY_FUNCTIONALITY = F.ID_INTERNAL
  AND F.UUID = '95b0bf15-1f4d-4430-815a-dfc1fb373ea6'
  AND EXISTS (
  	SELECT * FROM SITE S1 
  	WHERE S1.ID_INTERNAL = S.ID_INTERNAL 
  	  AND S.SHOW_SECTION_DEGREES = 1
  );

-- Courses
SELECT concat(
	"INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, KEY_SUPERIOR_SECTION, SECTION_ORDER, KEY_SITE, KEY_FUNCTIONALITY) VALUES (",
	concat_ws(",",
		concat("'", STS.OJB_CONCRETE_CLASS, "'"),
		1,
		coalesce(concat("'", STS.NAME, "'"), "NULL"),
		coalesce(STS.VISIBLE, 1),
		A.ID_INTERNAL,
		STS.SECTION_ORDER - (1 - S.SHOW_SECTION_TEACHERS) - (1 - S.SHOW_SECTION_EMPLOYEES) - (1 - S.SHOW_SECTION_DEGREES) - 3, -- because of the removal of presentation, announcements, and events sections
		S.ID_INTERNAL,
		coalesce(STS.KEY_FUNCTIONALITY, "NULL")
	),
	");"
) AS "-- Department: Copy section courses"
FROM ACCESSIBLE_ITEM A,   -- side section
     SITE S,              -- site
     SITE ST,             -- template site
     ACCESSIBLE_ITEM STS, -- template section
	 ACCESSIBLE_ITEM F    -- functionality
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.DepartmentSite'
  AND A.KEY_SITE = S.ID_INTERNAL
  AND A.KEY_SUPERIOR_SECTION IS NULL
  AND A.NAME LIKE '%pt7:Lateral%'
  AND ST.SITE_TYPE = S.OJB_CONCRETE_CLASS
  AND STS.KEY_SITE = ST.ID_INTERNAL
  AND STS.KEY_FUNCTIONALITY = F.ID_INTERNAL
  AND F.UUID = '85372e2e-ffe0-48ff-a758-1017a11c797f'
  AND EXISTS (
  	SELECT * FROM SITE S1 
  	WHERE S1.ID_INTERNAL = S.ID_INTERNAL 
  	  AND S.SHOW_SECTION_COURSES = 1
  );

--- 
--- Copy ResearchUnit Sections
---

-- Members
SELECT concat(
	"INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, KEY_SUPERIOR_SECTION, SECTION_ORDER, KEY_SITE, KEY_FUNCTIONALITY) VALUES (",
	concat_ws(",",
		concat("'", STS.OJB_CONCRETE_CLASS, "'"),
		1,
		coalesce(concat("'", STS.NAME, "'"), "NULL"),
		coalesce(STS.VISIBLE, 1),
		A.ID_INTERNAL,
		STS.SECTION_ORDER - 3, -- because of the removal of presentation, announcements, and events sections
		S.ID_INTERNAL,
		coalesce(STS.KEY_FUNCTIONALITY, "NULL")
	),
	");"
) AS "-- ResearchUnit: Copy section Members"
FROM ACCESSIBLE_ITEM A,   -- side section
     SITE S,              -- site
     SITE ST,             -- template site
     ACCESSIBLE_ITEM STS, -- template section
	 ACCESSIBLE_ITEM F    -- functionality
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ResearchUnitSite'
  AND A.KEY_SITE = S.ID_INTERNAL
  AND A.KEY_SUPERIOR_SECTION IS NULL
  AND A.NAME LIKE '%pt7:Lateral%'
  AND ST.SITE_TYPE = S.OJB_CONCRETE_CLASS
  AND STS.KEY_SITE = ST.ID_INTERNAL
  AND STS.KEY_FUNCTIONALITY = F.ID_INTERNAL
  AND F.UUID = '2a487bf2-9986-40cf-923d-c571c211491f'
  AND EXISTS (
  	SELECT * FROM SITE S1 
  	WHERE S1.ID_INTERNAL = S.ID_INTERNAL 
  	  AND S.SHOW_RESEARCH_MEMBERS = 1
  );

-- Publications
SELECT concat(
	"INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, KEY_SUPERIOR_SECTION, SECTION_ORDER, KEY_SITE, KEY_FUNCTIONALITY) VALUES (",
	concat_ws(",",
		concat("'", STS.OJB_CONCRETE_CLASS, "'"),
		1,
		coalesce(concat("'", STS.NAME, "'"), "NULL"),
		coalesce(STS.VISIBLE, 1),
		A.ID_INTERNAL,
		STS.SECTION_ORDER - (1 - S.SHOW_RESEARCH_MEMBERS) - 3, -- because of the removal of presentation, announcements, and events sections
		S.ID_INTERNAL,
		coalesce(STS.KEY_FUNCTIONALITY, "NULL")
	),
	");"
) AS "-- Department: Copy section publications"
FROM ACCESSIBLE_ITEM A,   -- side section
     SITE S,              -- site
     SITE ST,             -- template site
     ACCESSIBLE_ITEM STS, -- template section
	 ACCESSIBLE_ITEM F    -- functionality
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ResearchUnitSite'
  AND A.KEY_SITE = S.ID_INTERNAL
  AND A.KEY_SUPERIOR_SECTION IS NULL
  AND A.NAME LIKE '%pt7:Lateral%'
  AND ST.SITE_TYPE = S.OJB_CONCRETE_CLASS
  AND STS.KEY_SITE = ST.ID_INTERNAL
  AND STS.KEY_FUNCTIONALITY = F.ID_INTERNAL
  AND F.UUID = '5a35c10e-2108-412d-94f5-8ba12f051305'
  AND EXISTS (
  	SELECT * FROM SITE S1 
  	WHERE S1.ID_INTERNAL = S.ID_INTERNAL 
  	  AND S.SHOW_PUBLICATIONS = 1
  );
