-- insert_data.sql
-- Seed data for csce548_project1 (5 users, 5 categories, 50+ tasks)
USE csce548_project1;

-- USERS (5 rows)
INSERT INTO users (name) VALUES
('Alice'),
('Bob'),
('Charlie'),
('Dana'),
('Evan');

-- CATEGORIES (5 rows)
INSERT INTO categories (name) VALUES
('School'),
('Work'),
('Personal'),
('Health'),
('Errands');

-- TASKS (50 rows)
INSERT INTO tasks (user_id, category_id, description, completed) VALUES
(1, 1, 'Study for midterm', FALSE),
(1, 2, 'Finish project report', FALSE),
(1, 3, 'Clean room', TRUE),
(1, 4, 'Go for a run', FALSE),
(1, 5, 'Buy groceries', TRUE),

(2, 1, 'Read chapter 5', TRUE),
(2, 2, 'Prepare slides', FALSE),
(2, 3, 'Call mom', TRUE),
(2, 4, 'Doctor appointment', FALSE),
(2, 5, 'Pick up laundry', TRUE),

(3, 1, 'Submit homework', TRUE),
(3, 2, 'Team meeting', FALSE),
(3, 3, 'Watch a movie', TRUE),
(3, 4, 'Gym workout', FALSE),
(3, 5, 'Post office visit', TRUE),

(4, 1, 'Group study', FALSE),
(4, 2, 'Update resume', TRUE),
(4, 3, 'Organize desk', FALSE),
(4, 4, 'Yoga session', TRUE),
(4, 5, 'Car wash', FALSE),

(5, 1, 'Practice coding', TRUE),
(5, 2, 'Client email', FALSE),
(5, 3, 'Read a book', TRUE),
(5, 4, 'Meditation', FALSE),
(5, 5, 'Buy pet food', TRUE),

(1, 1, 'Review notes', TRUE),
(2, 2, 'Fix bugs', FALSE),
(3, 3, 'Meal prep', TRUE),
(4, 4, 'Stretching', TRUE),
(5, 5, 'Pay bills', FALSE),

(1, 2, 'Code cleanup', TRUE),
(2, 3, 'Journal writing', FALSE),
(3, 4, 'Cardio session', TRUE),
(4, 5, 'Pick up package', FALSE),
(5, 1, 'Exam prep', TRUE),

(1, 3, 'Laundry', TRUE),
(2, 4, 'Physical therapy', FALSE),
(3, 5, 'Return item', TRUE),
(4, 1, 'Quiz review', FALSE),
(5, 2, 'Weekly report', TRUE),

(1, 4, 'Walk outside', TRUE),
(2, 5, 'Buy supplies', FALSE),
(3, 1, 'Lecture review', TRUE),
(4, 2, 'Meeting notes', FALSE),
(5, 3, 'Relax time', TRUE),

(1, 5, 'Organize photos', FALSE),
(2, 1, 'Study group session', TRUE),
(3, 2, 'Deploy demo', FALSE),
(4, 3, 'Plan weekend', TRUE),
(5, 4, 'Swim laps', FALSE);

-- Confirm count is >= 50 (run after importing)
-- SELECT
--   (SELECT COUNT(*) FROM users) AS users,
--   (SELECT COUNT(*) FROM categories) AS categories,
--   (SELECT COUNT(*) FROM tasks) AS tasks;