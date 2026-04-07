-- 1. Populate Names
INSERT IGNORE INTO names (name) VALUES
    ('David'),
    ('Omer'),
    ('Avi'),
    ('Josh'),
    ('Daniel'),
    ('Ben'),
    ('John'),
    ('Michael'),
    ('Ethan'),
    ('Jacob'),
    ('Liam');

-- 2. Populate Objects
INSERT IGNORE INTO objects (object_name) VALUES
    ('Popsicle'),
    ('Ball'),
    ('Apple'),
    ('Bicycle'),
    ('Book'),
    ('Pencil');

-- 3. Populate Actions
INSERT IGNORE INTO actions (action_name, action_operation) VALUES
  ('found', '+'),
  ('bought', '+'),
  ('sold', '-'),
  ('lost', '-'),
  ('gave away', '-'),
  ('multiplied that amount by', '*'),
  ('divided them into groups of', '/');

-- 4. Populate Question Templates
INSERT IGNORE INTO question_templates (template, max_number, level) VALUES
('If {name} had {NUM1} {object}s and then he {action} {NUM2}, how many does he have now?', 20, 'Easy'),
('If {name} had {NUM1} {object}s and then he {action} {NUM2}, how many does he have now?', 40, 'Easy'),
('If {name} had {NUM1} {object}s and then he {action} {NUM2}, how many does he have now?', 60, 'Medium'),
('If {name} had {NUM1} {object}s and then he {action} {NUM2}, how many does he have now?', 80, 'Medium'),
('If {name} had {NUM1} {object}s and then he {action} {NUM2}, how many does he have now?', 100, 'Hard'),
('If {name} had {NUM1} {object}s and then he {action} {NUM2}, how many does he have now?', 120, 'Hard');
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
-- INSERT IGNORE INTO races (teacher_id, entry_code, maxCapacity, status) VALUES
--    (1,'123456',8,0);
--
-- -- -- 8. Populate Tracks
-- -- -- Linking students (IDs 1-4) to races (IDs 1-3)
-- INSERT IGNORE INTO tracks (student_id, race_id, score, path, path_chance, power_up, position) VALUES
--     (1,1,0,0,0,0,0);