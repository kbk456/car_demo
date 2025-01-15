INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (1,'현대','코나',2024,'AVAILABLE');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (2,'현대','아이오닉',2024,'AVAILABLE');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (3,'현대','스타리아',2022,'REPAIR');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (4,'현대','포터',2024,'AVAILABLE');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (5,'현대','투싼',2023,'AVAILABLE');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (6,'기아','카니발',2021,'AVAILABLE');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (7,'기아','레이',2022,'REPAIR');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (8,'기아','봉고3',2023,'AVAILABLE');
INSERT INTO car(id,manufacturer,model,production_year,status) VALUES (9,'기아','쏘렌토',2024,'AVAILABLE');

ALTER TABLE car ALTER COLUMN id RESTART WITH 10;

INSERT INTO category(id,name) VALUES (1,'미니SUV');
INSERT INTO category(id,name) VALUES (2,'준중형SUV');
INSERT INTO category(id,name) VALUES (3,'소형RV');
INSERT INTO category(id,name) VALUES (4,'중형트럭');
INSERT INTO category(id,name) VALUES (5,'대형SUV');
INSERT INTO category(id,name) VALUES (6,'대형RV');
INSERT INTO category(id,name) VALUES (7,'경형RV');
INSERT INTO category(id,name) VALUES (8,'중형RV');
INSERT INTO category(id,name) VALUES (9,'중형SUV');
INSERT INTO category(id,name) VALUES (10,'경유차');
INSERT INTO category(id,name) VALUES (11,'휘발유차');
INSERT INTO category(id,name) VALUES (12,'전기차');

ALTER TABLE category ALTER COLUMN id RESTART WITH 13;

INSERT INTO car_category(car_id,category_id) VALUES (1,1);
INSERT INTO car_category(car_id,category_id) VALUES (1,10);
INSERT INTO car_category(car_id,category_id) VALUES (2,2);
INSERT INTO car_category(car_id,category_id) VALUES (2,11);
INSERT INTO car_category(car_id,category_id) VALUES (3,3);
INSERT INTO car_category(car_id,category_id) VALUES (3,12);
INSERT INTO car_category(car_id,category_id) VALUES (4,4);
INSERT INTO car_category(car_id,category_id) VALUES (4,10);
INSERT INTO car_category(car_id,category_id) VALUES (5,5);
INSERT INTO car_category(car_id,category_id) VALUES (5,11);
INSERT INTO car_category(car_id,category_id) VALUES (6,6);
INSERT INTO car_category(car_id,category_id) VALUES (6,12);
INSERT INTO car_category(car_id,category_id) VALUES (7,7);
INSERT INTO car_category(car_id,category_id) VALUES (7,10);
INSERT INTO car_category(car_id,category_id) VALUES (8,8);
INSERT INTO car_category(car_id,category_id) VALUES (8,11);
INSERT INTO car_category(car_id,category_id) VALUES (9,9);
INSERT INTO car_category(car_id,category_id) VALUES (9,12);
