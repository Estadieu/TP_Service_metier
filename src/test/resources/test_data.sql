-- Une catégorie avec deux produits
INSERT INTO Categorie(code, libelle, description) VALUES 
    ( 98, '7prods', 'Cette catégorie a 7 produits');

-- Une catégorie sans produits

INSERT INTO Categorie(code, libelle, description) VALUES 
    ( 99, '0prod', 'Cette catégorie n''a pas de produit');

INSERT INTO Produit(reference, nom, fournisseur, categorie_code, quantite_par_unite, prix_unitaire, unites_en_stock, unites_commandees, niveau_de_reappro, indisponible ) VALUES
    ( 93, 'Produit 93 jamais commandé , disponible', 1, 98, '', 10.00, 100, 0, 10, FALSE),
    ( 94, 'Produit 94 jamais commandé, disponible', 1, 98, '', 10.00, 100, 0, 10, FALSE),
    ( 95, 'Produit 95 jamais commandé, disponible', 1, 98, '', 10.00, 100, 0, 10, FALSE),
    ( 96, 'Produit 96 jamais commandé, disponible', 1, 98, '', 10.00, 100, 0, 10, FALSE),
    ( 97, 'Produit 97 jamais commandé, indisponible', 1, 98, '', 10.00, 100, 0, 10, TRUE),
    ( 98, 'Produit 98', 1, 98, '24 bouteilles (1 litre)', 95.00, 17, 40, 25, FALSE),
    ( 99, 'Produit 99', 1, 98, '12 bouteilles (550 ml)', 50.00, 13, 70, 25, FALSE);

-- Un client sans commandes
INSERT INTO Client(code, societe, contact, fonction, adresse, ville, region, code_postal, pays, telephone, fax) VALUES 
    ( '0COM', 'Ce client n''a pas de commande', 'Maria Anders', 'Représentant(e)', 'Obere Str. 57', 'Berlin', NULL, '12209', 'Allemagne', '030-0074321', '030-0076545');

-- Un client avec deux commandes (il a commandé plus de 100 articles)
INSERT INTO Client(code, societe, contact, fonction, adresse, ville, region, code_postal, pays, telephone, fax) VALUES 
    ( '2COM', 'Ce client a 2 commandes', 'Laurence Lebihan', 'Propriétaire', '12, rue des Bouchers', 'Marseille', NULL, '13008', 'France', '91.24.45.40', '91.24.45.41');
-- Cette commande a déja été livrée, on ne peut plus ajouter de lignes (règle métier à vérifier)
INSERT INTO Commande(numero, client_code, saisiele, envoyeele, port, destinataire, adresse, ville, region, code_postal, pays, remise) VALUES 
    ( 99999, '2COM', '1994-11-16', '1994-11-21', 50.00, 'Bon app''', '12, rue des Bouchers', 'Marseille', NULL, '13008', 'France', 0.00);
INSERT INTO Ligne(commande_numero, produit_reference, quantite) VALUES ( 99999, 98, 15);
INSERT INTO Ligne(commande_numero, produit_reference, quantite) VALUES ( 99999, 99, 70);
-- Cette commande n'a pas encore été livrée, on peut encore ajouter des lignes
INSERT INTO Commande(numero, client_code, saisiele, envoyeele, port, destinataire, adresse, ville, region, code_postal, pays, remise) VALUES
    ( 99998, '2COM', '1994-11-29', NULL, 831.00, 'Bon app''', '12, rue des Bouchers', 'Marseille', NULL, '13008', 'France', 0.00);
INSERT INTO Ligne(commande_numero, produit_reference, quantite) VALUES ( 99998, 98, 7);
INSERT INTO Ligne(commande_numero, produit_reference, quantite) VALUES ( 99998, 99, 3);
