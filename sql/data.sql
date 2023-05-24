
-- TABLE: USER
CREATE TABLE `user`
(
    `id` VARCHAR(45) PRIMARY KEY COMMENT '아이디',
    `password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `email` VARCHAR(45) NOT NULL UNIQUE COMMENT '이메일',
    `name` VARCHAR(45) NOT NULL COMMENT '이름',
    `phone` VARCHAR(45) NOT NULL UNIQUE COMMENT '전화번호',
    `address` VARCHAR(45) NOT NULL COMMENT '주소',
    `level` VARCHAR(45) NOT NULL COMMENT '회원 구분',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at` TIMESTAMP COMMENT '수정일시',
    INDEX `userId` (`id`)
) COMMENT '회원정보';


-- TABLE: STORE
CREATE TABLE `store`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '가게번호',
    `name` VARCHAR(45) NOT NULL COMMENT '가게이름',
    `phone` VARCHAR(45) NOT NULL COMMENT '가게전화번호',
    `address` VARCHAR(45) NOT NULL COMMENT '가게주소',
    `owner_id` VARCHAR(45) NOT NULL COMMENT '가게사장님아이디',
    `open_status` VARCHAR(45) NOT NULL DEFAULT 'closed' COMMENT '가게오픈정보',
    `introduction` VARCHAR(45) NOT NULL COMMENT '가게소개멘트',
    `category_id` BIGINT NOT NULL COMMENT '가게카테고리번호',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at` TIMESTAMP COMMENT '수정일시',
    INDEX `store` (owner_id)
) COMMENT '가게정보';


-- TABLE: MENU
CREATE TABLE `menu`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '메뉴번호',
    `name` VARCHAR(45) NOT NULL COMMENT '메뉴이름',
    `price` BIGINT NOT NULL COMMENT '메뉴가격',
    `photo` TEXT COMMENT '메뉴사진',
    `description` VARCHAR(45) COMMENT '메뉴정보',
    `menu_group_id` INT COMMENT '메뉴그룹번호',
    `store_id` BIGINT NOT NULL COMMENT '가게번호',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at` TIMESTAMP COMMENT '수정일시',
    INDEX `menu` (store_id)
) COMMENT '메뉴';


-- TABLE: STORE_CATEGORY
CREATE TABLE `store_category`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '가게카테고리번호',
    `name` VARCHAR(45) NOT NULL COMMENT '가게카테고리이름'
) COMMENT '가게카테고리';

INSERT INTO STORE_CATEGORY (name) VALUES ('1인분');
INSERT INTO STORE_CATEGORY (name) VALUES ('족발·보쌈');
INSERT INTO STORE_CATEGORY (name) VALUES ('찜·탕·찌개');
INSERT INTO STORE_CATEGORY (name) VALUES ('돈까스·회·일식');
INSERT INTO STORE_CATEGORY (name) VALUES ('피자');
INSERT INTO STORE_CATEGORY (name) VALUES ('고기·구이');
INSERT INTO STORE_CATEGORY (name) VALUES ('야식');
INSERT INTO STORE_CATEGORY (name) VALUES ('양식');
INSERT INTO STORE_CATEGORY (name) VALUES ('치킨');
INSERT INTO STORE_CATEGORY (name) VALUES ('중식');
INSERT INTO STORE_CATEGORY (name) VALUES ('아시안');
INSERT INTO STORE_CATEGORY (name) VALUES ('백반·죽·국수');
INSERT INTO STORE_CATEGORY (name) VALUES ('도시락');
INSERT INTO STORE_CATEGORY (name) VALUES ('분식');
INSERT INTO STORE_CATEGORY (name) VALUES ('카페·디저트');
INSERT INTO STORE_CATEGORY (name) VALUES ('패스트푸드');


-- TABLE: MENU_OPTION
CREATE TABLE `menu_option`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '메뉴옵션번호',
    `name` VARCHAR(45) NOT NULL COMMENT '메뉴옵션이름',
    `price` BIGINT NOT NULL COMMENT '메뉴옵션가격',
    `menu_id` BIGINT NOT NULL COMMENT '해당메뉴번호'
) COMMENT '메뉴옵션';


-- TABLE: ORDER
CREATE TABLE `order`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '주문번호',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `order_status` VARCHAR(45) NOT NULL DEFAULT 'BEFORE_ORDER' COMMENT '주문현황',
    `address` VARCHAR(45) NOT NULL COMMENT '배달주소',
    `user_id` VARCHAR(45) NOT NULL COMMENT '회원아이디',
    `store_id` BIGINT NOT NULL COMMENT '가게번호',
    `total_price` BIGINT NOT NULL DEFAULT 0 COMMENT '총주문금액'
) COMMENT '주문';


-- TABLE: order_menu
CREATE TABLE `order_menu`
(
    `order_id` BIGINT COMMENT '주문번호',
    `menu_id` BIGINT COMMENT '메뉴번호',
    `count` BIGINT NOT NULL COMMENT '수량'
) COMMENT '주문메뉴';


-- TABLE: order_menu_option
CREATE TABLE `order_menu_option`
(
    `order_id` BIGINT COMMENT '주문번호',
    `menu_id` BIGINT COMMENT '메뉴번호',
    `option_id` BIGINT COMMENT '메뉴옵션번호'
) COMMENT '주문메뉴옵션';


-- TABLE: pay
CREATE TABLE `pay`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '결제번호',
    `pay_type` VARCHAR(45) NOT NULL COMMENT '결제방식',
    `price` BIGINT NOT NULL COMMENT '결제금액',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `order_id` BIGINT NOT NULL COMMENT '주문번호',
    `status` VARCHAR(45) NOT NULL COMMENT '결제현황'
) COMMENT '결제';



ALTER TABLE `store` ADD CONSTRAINT `store_user_fk` FOREIGN KEY (owner_id) REFERENCES `user`(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `store` ADD CONSTRAINT `store_store_category_fk` FOREIGN KEY (category_id) REFERENCES `store_category`(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `menu` ADD CONSTRAINT `menu_store_fk` FOREIGN KEY (store_id) REFERENCES `store`(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `menu_option` ADD CONSTRAINT `menu_option_menu_fk` FOREIGN KEY (menu_id) REFERENCES `menu`(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `order` ADD CONSTRAINT `order_user_fk` FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `order` ADD CONSTRAINT `order_store_fk` FOREIGN KEY (store_id) REFERENCES `store`(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `order_menu` ADD CONSTRAINT `order_menu_order_fk` FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `order_menu` ADD CONSTRAINT `order_menu_menu_fk` FOREIGN KEY (menu_id) REFERENCES `menu`(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `order_menu_option` ADD CONSTRAINT `order_menu_option_order_fk` FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `order_menu_option` ADD CONSTRAINT `order_menu_option_menu_fk` FOREIGN KEY (menu_id) REFERENCES `menu`(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `order_menu_option` ADD CONSTRAINT `order_menu_option_option_fk` FOREIGN KEY (option_id) REFERENCES `menu_option`(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `pay` ADD CONSTRAINT `pay_order_fk` FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE ON UPDATE CASCADE;




# drop table `pay`;
# drop table `order_menu_option`;
# drop table `order_menu`;
# drop table `order`;
# drop table `menu_option`;
# drop table `menu`;
# drop table `store`;
# drop table `store_category`;
# drop table `user`;
