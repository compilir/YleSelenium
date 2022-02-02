create table kysymysehdokkaalle
(
    kysymys text not null
        constraint kysymysehdokkaalle_pk
            primary key
);

alter table kysymysehdokkaalle
    owner to postgres;


create table ehdokkaat
(
    url text not null
        constraint ehdokkaat_pk
            primary key
);

alter table ehdokkaat
    owner to postgres;

create unique index ehdokkaat_url_uindex
    on ehdokkaat (url);


create table ehdokaskysymys
(
    kysymys       text
        constraint ehdokaskysymys_kysymysehdokkaalle_kysymys_fk
            references kysymysehdokkaalle,
    vastausnumero integer,
    vastausteksti text,
    puolue        text,
    url           text
        constraint ehdokaskysymys_ehdokkaat_url_fk
            references ehdokkaat
);

alter table ehdokaskysymys
    owner to postgres;

