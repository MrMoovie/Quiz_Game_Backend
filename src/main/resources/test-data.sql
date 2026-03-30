-- 1. Populate Names
INSERT IGNORE INTO names (name) VALUES
    ('David'),
    ('Sarah'),
    ('Yossi'),
    ('Noa'),
    ('Omer'),
    ('Rachel'),
    ('Avi');

-- 2. Populate Objects
INSERT IGNORE INTO objects (object_name) VALUES
    ('Popsicle'),
    ('Ball'),
    ('Apple'),
    ('Bicycle'),
    ('Book'),
    ('Pencil');

-- 3. Populate Actions
INSERT IGNORE INTO actions (action_name) VALUES
    ('bought'),
    ('ate'),
    ('found'),
    ('gave away'),
    ('lost');

-- 4. Populate Question Templates
-- 4. Populate Question Templates
INSERT IGNORE INTO question_templates (template, max_number, level) VALUES
    ('If {name} has {NUM1} {object}(s) and they {action} {NUM2} {object}(s), how many {object}(s) do they have now?', 10, 'Easy'),
    ('{name} starts with {NUM1} {object}(s). After they {action} {NUM2} {object}(s), what is the final count?', 20, 'Easy'),
    ('Yesterday, {name} had {NUM1} {object}(s). Today, they {action} {NUM2} {object}(s). How many {object}(s) are left with {name}?', 50, 'Medium'),
    ('Suppose {name} collected {NUM1} {object}(s) over the summer. If they {action} {NUM2} of them, how many remain?', 50, 'Medium'),
    ('{name} is organizing their room and counts {NUM1} {object}(s). Later, {name} {action} {NUM2} {object}(s). What is the new total?', 100, 'Hard'),
    ('At the start of the week, {name} recorded {NUM1} {object}(s). By Friday, {name} had {action} {NUM2} {object}(s). Calculate the current number of {object}(s).', 100, 'Hard'),
    ('Imagine {name} holds {NUM1} {object}(s) in a large bag. Once {name} {action} {NUM2} {object}(s), how many {object}(s) are accounted for?', 25, 'Easy'),
    ('During a school project, {name} needed {NUM1} {object}(s). Suddenly, they {action} {NUM2} {object}(s). How many does {name} have at this moment?', 75, 'Medium'),
    ('A magical chest contains {NUM1} {object}(s) belonging to {name}. If {name} {action} {NUM2} {object}(s), how many are inside the chest now?', 200, 'Hard'),
    ('{name} was given {NUM1} {object}(s) for a birthday gift. After a few days, they {action} {NUM2} {object}(s). How many {object}(s) does {name} currently possess?', 15, 'Easy');

-- 5. Populate Students
INSERT IGNORE INTO students (username, password, contact_info, full_name, token) VALUES
    ('student_david', 'pass123', 'david@example.com', 'David Cohen', 'tok_std_001'),
    ('student_sarah', 'pass123', 'sarah@example.com', 'Sarah Levy', 'tok_std_002'),
    ('student_yossi', 'pass123', 'yossi@example.com', 'Yossi Mizrahi', 'tok_std_003'),
    ('student_noa', 'pass123', 'noa@example.com', 'Noa Golan', 'tok_std_004');

-- 6. Populate Teachers
INSERT IGNORE INTO teachers (username, password, contact_info, full_name, token) VALUES
    ('teacher_ruth', 'teachpass', 'ruth.t@school.edu', 'Ruth Ben-David', 'tok_tch_001'),
    ('teacher_amir', 'teachpass', 'amir.k@school.edu', 'Amir Katz', 'tok_tch_002');

-- 7. Populate Races
-- Assuming teacher IDs generated above are 1 and 2
INSERT IGNORE INTO races (teacher_id, entry_code, is_open, maxCapacity, status) VALUES
    (1, 'MATH101', true, 8, 1),
    (1, 'MATH102', true, 12, 0),
    (2, 'ALG200', false, 10, 2);

-- 8. Populate Tracks
-- Linking students (IDs 1-4) to races (IDs 1-3)
INSERT IGNORE INTO tracks (student_id, race_id, score, path, path_chance, power_up, position) VALUES
    (1, 1, 150, 1, 5, 2, 1),
    (2, 1, 120, 2, 5, 0, 2),
    (3, 1, 90, 1, 3, 1, 3),
    (4, 3, 200, 3, 8, 3, 1),
    (1, 3, 180, 2, 4, 1, 2);