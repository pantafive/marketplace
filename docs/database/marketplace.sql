-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.9.1-beta
-- PostgreSQL version: 10.0
-- Project Site: pgmodeler.com.br
-- Model Author: ---


-- Database creation must be done outside an multicommand file.
-- These commands were put in this file only for convenience.
-- -- object: marketplace | type: DATABASE --
-- -- DROP DATABASE IF EXISTS marketplace;
-- CREATE DATABASE marketplace
-- 	ENCODING = 'UTF8'
-- 	LC_COLLATE = 'en_US.utf8'
-- 	LC_CTYPE = 'en_US.utf8'
-- 	TABLESPACE = pg_default
-- 	OWNER = postgres
-- ;
-- -- ddl-end --
-- 

-- object: public.user_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.user_id_seq CASCADE;
CREATE SEQUENCE public.user_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.user_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public."user" | type: TABLE --
-- DROP TABLE IF EXISTS public."user" CASCADE;
CREATE TABLE public."user"(
	id serial NOT NULL,
	name character varying(50) NOT NULL,
	email character varying(32) NOT NULL,
	password character varying(128) NOT NULL,
	created timestamp NOT NULL,
	updated timestamp NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT username_uq UNIQUE (name),
	CONSTRAINT email_uq UNIQUE (email)

);
-- ddl-end --

-- object: public.order_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.order_id_seq CASCADE;
CREATE SEQUENCE public.order_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.order_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public."order" | type: TABLE --
-- DROP TABLE IF EXISTS public."order" CASCADE;
CREATE TABLE public."order"(
	id serial NOT NULL,
	user_id integer NOT NULL,
	status varchar(32) NOT NULL,
	created timestamp NOT NULL,
	updated timestamp NOT NULL,
	CONSTRAINT order_pk PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public."order" OWNER TO postgres;
-- ddl-end --

-- object: public.goods_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.goods_id_seq CASCADE;
CREATE SEQUENCE public.goods_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.goods_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.license_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.license_id_seq CASCADE;
CREATE SEQUENCE public.license_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.license_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.song_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.song_id_seq CASCADE;
CREATE SEQUENCE public.song_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.song_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.book | type: TABLE --
-- DROP TABLE IF EXISTS public.book CASCADE;
CREATE TABLE public.book(
	id integer NOT NULL,
	author varchar(128) NOT NULL,
	title varchar(128) NOT NULL,
	cover varchar(128),
	published date NOT NULL,
	pdf varchar(128),
	description text NOT NULL,
	created timestamp NOT NULL,
	updated timestamp NOT NULL,
	CONSTRAINT book_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: public.order_item_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.order_item_id_seq CASCADE;
CREATE SEQUENCE public.order_item_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.order_item_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.role | type: TABLE --
-- DROP TABLE IF EXISTS public.role CASCADE;
CREATE TABLE public.role(
	id serial NOT NULL,
	name varchar(150) NOT NULL,
	CONSTRAINT role_pk PRIMARY KEY (id),
	CONSTRAINT name_uq UNIQUE (name)

);
-- ddl-end --

-- object: public.order_item | type: TABLE --
-- DROP TABLE IF EXISTS public.order_item CASCADE;
CREATE TABLE public.order_item(
	id serial NOT NULL,
	order_id integer NOT NULL,
	product_id integer,
	amount numeric(12,2) NOT NULL,
	CONSTRAINT order_item_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: order_fk | type: CONSTRAINT --
-- ALTER TABLE public.order_item DROP CONSTRAINT IF EXISTS order_fk CASCADE;
ALTER TABLE public.order_item ADD CONSTRAINT order_fk FOREIGN KEY (order_id)
REFERENCES public."order" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.download | type: TABLE --
-- DROP TABLE IF EXISTS public.download CASCADE;
CREATE TABLE public.download(
	id serial NOT NULL,
	order_item_id integer NOT NULL,
	token varchar(64),
	created timestamp NOT NULL,
	CONSTRAINT download_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: order_item_fk | type: CONSTRAINT --
-- ALTER TABLE public.download DROP CONSTRAINT IF EXISTS order_item_fk CASCADE;
ALTER TABLE public.download ADD CONSTRAINT order_item_fk FOREIGN KEY (order_item_id)
REFERENCES public.order_item (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.genre | type: TABLE --
-- DROP TABLE IF EXISTS public.genre CASCADE;
CREATE TABLE public.genre(
	id smallserial NOT NULL,
	name varchar(128) NOT NULL,
	CONSTRAINT genre_pk PRIMARY KEY (id),
	CONSTRAINT genere_uq UNIQUE (name)

);
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public."order" DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public."order" ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.product | type: TABLE --
-- DROP TABLE IF EXISTS public.product CASCADE;
CREATE TABLE public.product(
	id serial NOT NULL,
	user_id integer NOT NULL,
	type varchar(16) NOT NULL,
	price numeric(6,2) NOT NULL,
	created timestamp NOT NULL,
	updated timestamp NOT NULL,
	CONSTRAINT product_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.book DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.book ADD CONSTRAINT product_fk FOREIGN KEY (id)
REFERENCES public.product (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: book_uq | type: CONSTRAINT --
-- ALTER TABLE public.book DROP CONSTRAINT IF EXISTS book_uq CASCADE;
ALTER TABLE public.book ADD CONSTRAINT book_uq UNIQUE (id);
-- ddl-end --

-- object: public.book_2_genre | type: TABLE --
-- DROP TABLE IF EXISTS public.book_2_genre CASCADE;
CREATE TABLE public.book_2_genre(
	book_id integer NOT NULL,
	genre_id smallint NOT NULL,
	CONSTRAINT book_2_genre_pk PRIMARY KEY (book_id,genre_id)

);
-- ddl-end --

-- object: book_fk | type: CONSTRAINT --
-- ALTER TABLE public.book_2_genre DROP CONSTRAINT IF EXISTS book_fk CASCADE;
ALTER TABLE public.book_2_genre ADD CONSTRAINT book_fk FOREIGN KEY (book_id)
REFERENCES public.book (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: genre_fk | type: CONSTRAINT --
-- ALTER TABLE public.book_2_genre DROP CONSTRAINT IF EXISTS genre_fk CASCADE;
ALTER TABLE public.book_2_genre ADD CONSTRAINT genre_fk FOREIGN KEY (genre_id)
REFERENCES public.genre (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.order_item DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.order_item ADD CONSTRAINT product_fk FOREIGN KEY (product_id)
REFERENCES public.product (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.product DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.product ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.transaction | type: TABLE --
-- DROP TABLE IF EXISTS public.transaction CASCADE;
CREATE TABLE public.transaction(
	id serial NOT NULL,
	user_id integer NOT NULL,
	order_id integer,
	amount numeric(12,2) NOT NULL,
	type varchar(16) NOT NULL,
	status varchar(16) NOT NULL,
	payload text,
	created timestamp NOT NULL,
	updated timestamp NOT NULL,
	CONSTRAINT payment_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: order_fk | type: CONSTRAINT --
-- ALTER TABLE public.transaction DROP CONSTRAINT IF EXISTS order_fk CASCADE;
ALTER TABLE public.transaction ADD CONSTRAINT order_fk FOREIGN KEY (order_id)
REFERENCES public."order" (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.user_2_role | type: TABLE --
-- DROP TABLE IF EXISTS public.user_2_role CASCADE;
CREATE TABLE public.user_2_role(
	user_id integer NOT NULL,
	role_id integer NOT NULL,
	CONSTRAINT user_2_role_pk PRIMARY KEY (user_id,role_id)

);
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_2_role DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.user_2_role ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: role_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_2_role DROP CONSTRAINT IF EXISTS role_fk CASCADE;
ALTER TABLE public.user_2_role ADD CONSTRAINT role_fk FOREIGN KEY (role_id)
REFERENCES public.role (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.review | type: TABLE --
-- DROP TABLE IF EXISTS public.review CASCADE;
CREATE TABLE public.review(
	id serial NOT NULL,
	order_item_id integer NOT NULL,
	grade smallint NOT NULL,
	comment text,
	created timestamp NOT NULL,
	updated timestamp NOT NULL,
	CONSTRAINT reviews_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: public."like" | type: TABLE --
-- DROP TABLE IF EXISTS public."like" CASCADE;
CREATE TABLE public."like"(
	id serial NOT NULL,
	user_id integer NOT NULL,
	product_id integer NOT NULL,
	created timestamp NOT NULL,
	CONSTRAINT like_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public."like" DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public."like" ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public."like" DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public."like" ADD CONSTRAINT product_fk FOREIGN KEY (product_id)
REFERENCES public.product (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: order_item_fk | type: CONSTRAINT --
-- ALTER TABLE public.review DROP CONSTRAINT IF EXISTS order_item_fk CASCADE;
ALTER TABLE public.review ADD CONSTRAINT order_item_fk FOREIGN KEY (order_item_id)
REFERENCES public.order_item (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: review_uq | type: CONSTRAINT --
-- ALTER TABLE public.review DROP CONSTRAINT IF EXISTS review_uq CASCADE;
ALTER TABLE public.review ADD CONSTRAINT review_uq UNIQUE (order_item_id);
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.transaction DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.transaction ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --


