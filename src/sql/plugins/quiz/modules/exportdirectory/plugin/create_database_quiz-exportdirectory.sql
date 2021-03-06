DROP TABLE IF EXISTS quiz_exportdirectory_parameters;
CREATE TABLE quiz_exportdirectory_parameters (
  id_parameter INT NOT NULL,
  id_quiz INT NOT NULL,
  parameter_name varchar(255) NOT NULL,
  id_entry INT default NULL,
  PRIMARY KEY  (id_parameter)
);

DROP TABLE IF EXISTS quiz_exportdirectory_associations;
CREATE TABLE quiz_exportdirectory_associations (
  id_quiz INT NOT NULL,
  id_question INT NULL,
  id_entry INT NOT NULL
);