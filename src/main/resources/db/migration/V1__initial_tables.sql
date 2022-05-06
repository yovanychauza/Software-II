CREATE TABLE "student" (
                         st_id serial NOT NULL,
                         st_name character varying(250) NOT NULL,
                         st_last_name character varying(250) NOT NULL,
                         st_age integer NOT NULL,
                         st_state boolean NOT NULL,
                         PRIMARY KEY (st_id)
                        );

ALTER TABLE "student" OWNER to postgres;
