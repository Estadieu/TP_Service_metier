package comptoirs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import comptoirs.service.CommandeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import comptoirs.entity.Client;
import comptoirs.entity.Commande;
import comptoirs.entity.Ligne;
import comptoirs.entity.Produit;
import lombok.extern.log4j.Log4j2;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2 // Génère le 'logger' pour afficher les messages de trace
@DataJpaTest
class CommandeRepositoryTest {
	
	@Autowired 
	private CommandeRepository daoCommande;

	@Autowired 
	private ClientRepository daoClient;

	@Autowired 
	private ProduitRepository daoProduit;
	
	@Autowired
	private LigneRepository daoLigne;

	@Test
	@Sql("small_data.sql")		
	void onPeutCreerUneCommandeEtSesLignes() {
		log.info("Création d'une commande avec ses lignes");
		// On cherche les infos nécessaires dans le jeu d'essai
		Produit p1 = daoProduit.findById(98).orElseThrow();
		Produit p2 = daoProduit.findById(99).orElseThrow();
		Client c1  = daoClient.findById("0COM").orElseThrow();

		// On crée une commande
		Commande nouvelle = new Commande();
		// On définit au moins les propriétés non NULL
		nouvelle.setClient(c1);
		nouvelle.setSaisiele(LocalDate.now());
		nouvelle.setRemise(BigDecimal.ZERO);
				
		// On crée deux lignes pour la nouvelle commande
		Ligne l1 = new Ligne(nouvelle, p1, 4);
	
		Ligne l2 = new Ligne(nouvelle, p2, 99);
		
		ArrayList<Ligne> lignes = new ArrayList<>();
		lignes.add(l1); lignes.add(l2);

		// On ajoute les deux lignes à la commande
		nouvelle.setLignes(lignes);

		// On enregistre la commande (provoque l'enregistrement des lignes)
		daoCommande.save(nouvelle);
				
		// On regarde si ça s'est bien passé
		assertEquals(5, daoLigne.count(),   "Il doit y avoir 5 lignes en tout");
		assertEquals(3, p1.getLignes().size(), "Il doit y avoir 3 lignes pour le produit p1");
		assertEquals(2, p2.getLignes().size(), "Il doit y avoir 2 lignes pour le produit p2");
		assertTrue(p2.getLignes().contains(l2), "La nouvelle ligne doit avoir été ajoutée au produit p2");
		assertTrue(p1.getLignes().contains(l1), "La nouvelle ligne doit avoir été ajoutée au produit p1");		
	}
	
	@Test
	@Sql("small_data.sql")		
	void pasDeuxFoisLeMemeProduitDansUneCommande() {
		log.info("Tentative de création d'une commande avec doublon");	
		// On cherche les infos nécessaires dans le jeu d'essai
		Produit p1 = daoProduit.findById(99).get();
		Client c1  = daoClient.findById("0COM").get();

		// On crée une commande
		Commande nouvelle = new Commande();
		// On définit au moins les propriétés non NULL
		nouvelle.setClient(c1);
				
		// On crée deux lignes pour la nouvelle commande avec le même produit
		Ligne l1 = new Ligne(nouvelle, p1, 4);
		Ligne l2 = new Ligne(nouvelle, p1, 10);

		// On ajoute les deux lignes à la commande
		nouvelle.getLignes().add(l1);
		nouvelle.getLignes().add(l2);

		try { // La création de la commande doit produire une erreur
			daoCommande.save(nouvelle);
			fail("La commande ne doit pas être sauvegardée");
		} catch (DataIntegrityViolationException e) {
			log.info("La création a échoué : {}", e.getMessage());
		}
	}

	@Test
	@Sql("small_data.sql")		
	// La liste des lignes d'une commandes est annotée par "orphanRemoval=true"
	void onPeutSupprimerDesLignesDansUneCommande() {
		long nombreDeLignes = daoLigne.count(); // Combien de lignes en tout ?
		log.info("Supression de lignes dans une commande");
		Commande c = daoCommande.findById(99999).get(); // Cette commande a 2 lignes
		c.getLignes().remove(1); // On supprime la dernière ligne
		daoCommande.save(c); // On l'enregistre (provoque la suppression de la ligne)
		assertEquals(nombreDeLignes - 1, daoLigne.count(), "On doit avoir supprimé une ligne");
	}

	@Test
	@Sql("small_data.sql")
	void onPeutModifierDesLignesDansUneCommande() {
		log.info("Modification des lignes d'une commande");
		Commande c = daoCommande.findById(99999).get(); // Cette commande a 2 lignes
		Ligne l = c.getLignes().get(1); // On prend la deuxième
		l.setQuantite(99);; // On la modifie
		daoCommande.save(c); // On enregistre la commande (provoque la modification de la ligne)
		assertEquals(3, daoLigne.count(), "Il doit rester 3 lignes en tout");
	}

}
