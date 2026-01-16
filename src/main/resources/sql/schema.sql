CREATE TYPE CategoryEnum AS ENUM (
  'VEGETABLE','ANIMAL','MARINE','DAIRY','OTHER'
);
CREATE TYPE DishTypeEnum AS ENUM (
    'STAR','MAIN','DESSERT'
);

create table Ingredient (
    id SERIAL primary key,
    name VARCAHR(255),
    price NUMERIC,
    category CategoryEnum not null ,
    id_dish int not null ,
    CONSTRAINT fk_dish foreign key (id_dish) references Dish (id)

)
create table Dish (
      id  SERIAL primary key,
      name varchar (255),
      dish_type DishTypeEnum not null
)