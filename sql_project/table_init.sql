# orderdb
drop table IF EXISTS  test_db.t_order;
CREATE TABLE IF NOT EXISTS test_db.t_order (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
 	order_number varchar(50) not null COMMENT '订单号',
	description VARCHAR(256) NOT NULL DEFAULT '' COMMENT '描述',
	create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	index id_create_time(create_time),
  	index id_order_number(order_number)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_bin COMMENT ='订单';


# test_user
drop table IF EXISTS  test_db.test_user;
CREATE TABLE IF NOT EXISTS test_db.test_user (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
 	name varchar(50) not null DEFAULT '' COMMENT '姓名',
	salary decimal(10,2) NOT NULL DEFAULT 0 COMMENT '薪水',
	age int NOT NULL DEFAULT 0 COMMENT '年龄',
	group_id BIGINT   COMMENT '用户所属组织',
	create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	index id_create_time(create_time),
  	index id_test_use_name(name)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_bin COMMENT ='用户';


# orderdb
drop table IF EXISTS  test_db.test_group;
CREATE TABLE IF NOT EXISTS test_db.test_group (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
 	group_name varchar(50) not null DEFAULT '' COMMENT '组织名称',
  	index id_test_use_group_name(group_name)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_bin COMMENT ='组织';




INSERT INTO test_db.test_group (id,group_name)
	VALUES (1,'group1');