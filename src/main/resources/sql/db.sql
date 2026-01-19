create database "mini_dish_db";

create user "mini_dish_db_manager" with password '123456';

GRANT USAGE ON SCHEMA public TO mini_dish_db_manager;
GRANT CREATE ON SCHEMA public TO mini_dish_db_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA PUBLIC
      GRANT SELECT , INSERT , UPDATE , DELETE ON TABLES TO mini_dish_db_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA PUBLIC
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO mini_dish_db_manager;