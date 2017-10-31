-- Test data

USE CBA_Code_Check;

INSERT INTO role (name) VALUES ('STUDENT');

INSERT INTO user (first_name, last_name, mobile_phone, email, passwd_hash) VALUES
(
  'Tobias',
  'Grundtvig',
  '27571087',
  'tgrundtvig@gmail.com',
  '12345678'
);

INSERT INTO assignment (name, description) VALUES
(
  'Simple add',
  'Write a program that reads two integers from <i>stdin</i> and writes their sum to <i>stdout</i>.'
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '5 7',
  '12',
  FALSE,
  1
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '52 73',
  '125',
  FALSE,
  2
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '-45 50',
  '5',
  FALSE,
  3
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '-42 -7',
  '-49',
  FALSE,
  4
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '34 -78',
  '-44',
  FALSE,
  5
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '42 42',
  '84',
  TRUE,
  6
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '13 13',
  '26',
  TRUE,
  7
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '89 112',
  '201',
  TRUE,
  8
);

INSERT INTO assignment_test (assignment_id, input, expected, hidden, test_order) VALUES
(
  (SELECT id FROM assignment WHERE name='Simple add'),
  '-234 435',
  '201',
  TRUE,
  9
);

INSERT INTO task (user_id, assignment_id, end, max_handins) VALUES
(
  (SELECT id FROM user WHERE email='tgrundtvig@gmail.com'),
  (SELECT id FROM assignment WHERE name='Simple add'),
  ADDDATE(CURRENT_TIMESTAMP, 28),
  5
);