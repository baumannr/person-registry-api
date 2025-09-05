INSERT INTO Person (FirstName, LastName)
VALUES ('Elek', 'Teszt'),
       ('Angéla', 'Kacagó');

INSERT INTO Address (PersonId, Type, Country, City, Zip, Street)
VALUES (1, 'PERMANENT', 'Magyarország', 'Budapest', '1120', 'Teszt utca 15.'),
       (2, 'PERMANENT', 'Magyarország', 'Budapest', '1061', 'Nap utca 2.');

INSERT INTO ContactInfo (PersonId, Type, ContactValue)
VALUES (1, 'EMAIL', 'elekteszt@gmail.com'),
       (2, 'EMAIL', 'angela.kacago@gmail.com');

INSERT INTO ContactInfo (PersonId, Type, ContactValue)
VALUES (1, 'PHONE', '+36301234567'),
       (2, 'PHONE', '+36203217654');