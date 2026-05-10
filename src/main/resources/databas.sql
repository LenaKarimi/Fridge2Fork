CREATE TABLE IF NOT EXISTS public.profiles
(
    id integer NOT NULL DEFAULT nextval('profiles_id_seq'::regclass),
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password_hash character varying(255) COLLATE pg_catalog."default" NOT NULL,
    name character varying(100) COLLATE pg_catalog."default",
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT profiles_pkey PRIMARY KEY (id),
    CONSTRAINT profiles_email_key UNIQUE (email),
    CONSTRAINT profiles_username_key UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.profiles
    OWNER to aq2327;

CREATE TABLE IF NOT EXISTS public.recipes
(
    meal_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    instructions text COLLATE pg_catalog."default",
    image_url character varying(500) COLLATE pg_catalog."default",
    cuisine character varying(50) COLLATE pg_catalog."default",
    cuisine_group character varying(50) COLLATE pg_catalog."default",
    ingredients jsonb,
    CONSTRAINT recipes_pkey PRIMARY KEY (meal_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.recipes
    OWNER to aq2327;

CREATE TABLE IF NOT EXISTS public.liked_recipes
(
    profile_id integer NOT NULL,
    meal_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT liked_recipes_pkey PRIMARY KEY (profile_id, meal_id),
    CONSTRAINT liked_recipes_meal_id_fkey FOREIGN KEY (meal_id)
        REFERENCES public.recipes (meal_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT liked_recipes_profile_id_fkey FOREIGN KEY (profile_id)
        REFERENCES public.profiles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.liked_recipes
    OWNER to aq2327;

