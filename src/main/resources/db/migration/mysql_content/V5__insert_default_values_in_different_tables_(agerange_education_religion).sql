
INSERT INTO education_level (`id`, `education_level`) VALUES (1, 'preschool');

INSERT INTO education_level (`id`, `education_level`) VALUES (2, 'elementary school');

INSERT INTO education_level (`id`, `education_level`) VALUES (3, 'middle school');

INSERT INTO education_level (`id`, `education_level`) VALUES (4, 'high school');

INSERT INTO education_level (`id`, `education_level`) VALUES (5, 'college');

INSERT INTO education_level (`id`, `education_level`) VALUES (6, 'university');

INSERT INTO education_level (`id`, `education_level`) VALUES (7, 'master');

INSERT INTO education_level (`id`, `education_level`) VALUES (8, 'doctor');

INSERT INTO education_level (`id`, `education_level`) VALUES (9, 'other');


INSERT INTO education_discipline (`id`, `discipline`) VALUES (1, 'humanities');

INSERT INTO education_discipline (`id`, `discipline`) VALUES (2, 'social sciences');

INSERT INTO education_discipline (`id`, `discipline`) VALUES (3, 'natural sciences');

INSERT INTO education_discipline (`id`, `discipline`) VALUES (4, 'formal sciences');

INSERT INTO education_discipline (`id`, `discipline`) VALUES (5, 'professions and applied sciences');

INSERT INTO education_discipline (`id`, `discipline`) VALUES (6, 'other');


INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (1, 'history', 1);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (2, 'linguistics', 1);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (3, 'literature', 1);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (4, 'performing arts', 1);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (5, 'philosophy', 1);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (6, 'religion', 1);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (7, 'visual arts', 1);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (8, 'other', 1);


INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (9, 'anthropology', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (10, 'archaeology', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (11, 'area studies', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (12, 'cultural and ethnic studies', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (13, 'gender and sexuality studies', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (14, 'political science', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (15, 'psychology', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (16, 'sociology', 2);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (17, 'other', 2);


INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (18, 'space sciences', 3);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (19, 'earth sciences', 3);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (20, 'life sciences', 3);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (21, 'chemistry', 3);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (22, 'physics', 3);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (23, 'other', 3);


INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (24, 'logic', 4);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (25, 'mathematics', 4);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (26, 'statistics', 4);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (27, 'systems science', 4);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (28, 'other', 4);


INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (29, 'agriculture', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (30, 'architecture and design', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (31, 'business', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (32, 'computer sciences', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (33, 'divinity', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (34, 'education', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (35, 'engineering', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (36, 'environmental studies and forestry', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (37, 'family and consumer science', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (38, 'healthcare science', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (39, 'human physical performance and recreation', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (40, 'journalism, media studies and communication', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (41, 'law', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (42, 'library and museum studies', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (43, 'military sciences', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (44, 'public administration', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (45, 'social work', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (46, 'transportation', 5);

INSERT INTO education_sub_discipline (`id`, `subdiscipline`, `education_discipline_id`) VALUES (47, 'other', 5);


INSERT INTO age_range (`id`, `start`, `end`) VALUES (1, 0, 10);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (2, 11, 20);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (3, 21, 30);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (4, 31, 40);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (5, 41, 50);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (6, 51, 60);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (7, 61, 70);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (8, 71, 80);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (9, 81, 90);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (10, 91, 100);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (11, 101, 110);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (12, 111, 120);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (13, 121, 130);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (14, 131, 140);

INSERT INTO age_range (`id`, `start`, `end`) VALUES (15, 141, 150);


INSERT INTO religion (`id`, `name`) VALUES (1, 'christian');

INSERT INTO religion (`id`, `name`) VALUES (2, 'muslim');

INSERT INTO religion (`id`, `name`) VALUES (3, 'budhist');

INSERT INTO religion (`id`, `name`) VALUES (4, 'hindus');

INSERT INTO religion (`id`, `name`) VALUES (5, 'judaist');

INSERT INTO religion (`id`, `name`) VALUES (6, 'bahá''í faith');

INSERT INTO religion (`id`, `name`) VALUES (7, 'sikhist');

INSERT INTO religion (`id`, `name`) VALUES (8, 'taoist');

INSERT INTO religion (`id`, `name`) VALUES (9, 'confucianist');

INSERT INTO religion (`id`, `name`) VALUES (10, 'jainist');

INSERT INTO religion (`id`, `name`) VALUES (11, 'mormonist');

INSERT INTO religion (`id`, `name`) VALUES (12, 'spiritist');

INSERT INTO religion (`id`, `name`) VALUES (13, 'atheist');

INSERT INTO religion (`id`, `name`) VALUES (14, 'other');
