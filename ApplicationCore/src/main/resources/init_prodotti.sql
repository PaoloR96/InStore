-- Inserisci dati nella tabella RIVENDITORE (che include campi da Utente)
INSERT INTO rivenditore (username, email, password, num_cell, nome_societa, partita_iva, iban, stato_rivenditore) VALUES
                                                                                                                      ('rivenditore1', 'alpha@example.com', 'password1', '1234567890', 'Società Alpha', '12345678901', 'IT60X0542811101000000123456', 'ABILITATO'),
                                                                                                                      ('rivenditore2', 'beta@example.com', 'password2', '0987654321', 'Società Beta', '98765432109', 'IT60Y0542811102000000654321', 'INATTESA'),
                                                                                                                      ('rivenditore3', 'gamma@example.com', 'password3', '098000000', 'Società Gamma', '98765430000', 'IT60Y0542811102000000000000', 'DISABILITATO');

INSERT INTO carta_di_credito(numero_carta, cognome_intestatario, cvc, data_sacdenza, nome_intestatario) VALUES
                                                                                                            ('4087575738998','Pippo', 676, '2026-04-22','Franco');

-- Inserisci il cliente e associa direttamente il carrello
INSERT INTO cliente (email, username, password, num_cell, nome, cognome, stato_cliente, numero_carta_fedelta, sconto, tipo_cliente, numero_carta)
VALUES ('mario.rossi@example.com', 'mrossi', 'password123', '1234567890', 'Mario', 'Rossi', 'ABILITATO', NULL, NULL, 'STANDARD', '4087575738998');


-- Inserisci dati nella tabella PRODOTTO, associandoli ai Rivenditori
INSERT INTO prodotto (nome_prodotto, descrizione, prezzo, taglia, path_immagine, quantita_totale, rivenditore_username) VALUES
                                                                                                                                ('Maglietta', 'Maglietta in cotone 100%', 19.99, 'M', '/images/maglietta.jpg', 50, 'rivenditore1'),
                                                                                                                                ('Pantaloni', 'Pantaloni eleganti in lino', 49.99, 'L', '/images/pantaloni.jpg', 30, 'rivenditore1'),
                                                                                                                                ('Scarpe', 'Scarpe sportive', 59.99, 'S', '/images/scarpe.jpg', 20, 'rivenditore1');


INSERT INTO carrello (username, prezzo_complessivo) VALUES ('mrossi', 0.0);


