-- 1. Populate Names
INSERT IGNORE INTO names (name) VALUES
    ('David'),
    ('Sarah'),
    ('Yossi'),
    ('Noa'),
    ('Omer');

-- 2. Populate Objects
INSERT IGNORE INTO objects (object_name) VALUES
    ('Popsicle'),
    ('Ball'),
    ('Apple'),
    ('Bicycle');

-- 3. Populate Question Templates
-- Columns map exactly to your QuestionTemplateEntity
INSERT IGNORE INTO question_templates (template, max_number, level) VALUES
    ('{NUM1} + {NUM2} = ?', 20, 'EASY'),
    ('{NUM1} - {NUM2} = ?', 20, 'EASY'),
    ('{NUM1} * {NUM2} = ?', 10, 'HARD'),
    ('{NAME} had {NUM1} {OBJECT}s. They lost {NUM2} of them. How many are left?', 50, 'HARD');