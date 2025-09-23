INSERT INTO students (name, kana_name, nickname, email, area, age, sex, remark, isDeleted) VALUES
('佐藤 太郎', 'サトウ タロウ', 'タロちゃん', 'taro.sato@example.com', '東京', 18, '男性', '真面目な学生', 0),
('鈴木 花子', 'スズキ ハナコ', 'ハナちゃん', 'hanako.suzuki@example.com', '大阪', 20, '女性', '活発で明るい', 0),
('田中 一郎', 'タナカ イチロウ', 'イチくん', 'ichiro.tanaka@example.com', '秋田', 20, '男性', '成績優秀', 0),
('高橋 美咲', 'タカハシ ミサキ', 'ミサミサ', 'misaki.takahashi@example.com', '福岡', 21, '女性', '出席率が高い', 0),
('山本 大輔', 'ヤマモト ダイスケ', 'ダイスケ', 'daisuke.yamamoto@example.com', '滋賀', 23, '男性', 'ITに強い', 0);

INSERT INTO student_courses (student_id, course_name, start_date, end_date) VALUES
(1, 'Java入門', '2024-04-01', '2024-06-30'),
(2, 'Java入門', '2024-05-01', '2024-07-31'),
(3, 'データベース基礎', '2024-04-15', '2024-06-15'),
(4, 'データベース基礎', '2024-05-10', '2024-07-10'),
(5, 'ネットワーク基礎', '2025-08-08', '2026-08-08'),
(1, 'Spring Boot実践', '2024-07-01', '2024-09-30'),
(3, 'Python超入門', '2024-06-20', '2024-08-20'),
(5, 'Java応用', '2025-08-08', '2026-08-08');

INSERT INTO course_status (course_id, student_id, status) VALUES
(1, 1, '仮申込'),
(2, 2, '本申込'),
(3, 3, '受講中'),
(4, 4, '受講終了'),
(5, 5, '仮申込'),
(6, 1, '本申込'),
(7, 3, '受講中'),
(8, 5, '受講終了');