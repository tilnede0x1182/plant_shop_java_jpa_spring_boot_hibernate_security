package com.planteshop.seed;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.planteshop.model.entity.Plant;
import com.planteshop.model.entity.User;
import com.planteshop.model.enums.RoleType;
import com.planteshop.repository.OrderRepository;
import com.planteshop.repository.PlantRepository;
import com.planteshop.repository.UserRepository;
import net.datafaker.Faker;

@Component
@Profile({ "seed", "init" })
public class DatabaseSeeder implements CommandLineRunner {

	// # Dépendances principales
	private final PlantRepository plantRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final OrderRepository orderRepository;
	private final Faker faker = new Faker();

	// # Liste pour stocker les credentials générés
	private final List<Credential> userCredentialsList = new ArrayList<>();

	// # Variables globales pour la seed
	private static final int NUMBER_OF_PLANTS = 30; // 🌱 Nombre de plantes à créer
	private static final int NUMBER_OF_ADMINS = 3; // 👤 Nombre d'administrateurs à créer
	private static final int NUMBER_OF_USERS = 20; // 👥 Nombre d'utilisateurs à créer

	private static final List<String> PLANT_NAMES_LIST = Arrays.asList(
			"Rose", "Tulipe", "Lavande", "Orchidée", "Basilic", "Menthe", "Pivoine", "Tournesol",
			"Cactus (Echinopsis)", "Bambou", "Camomille (Matricaria recutita)", "Sauge (Salvia officinalis)",
			"Romarin (Rosmarinus officinalis)", "Thym (Thymus vulgaris)", "Laurier-rose (Nerium oleander)",
			"Aloe vera", "Jasmin (Jasminum officinale)", "Hortensia (Hydrangea macrophylla)",
			"Marguerite (Leucanthemum vulgare)", "Géranium (Pelargonium graveolens)", "Fuchsia (Fuchsia magellanica)",
			"Anémone (Anemone coronaria)", "Azalée (Rhododendron simsii)", "Chrysanthème (Chrysanthemum morifolium)",
			"Digitale pourpre (Digitalis purpurea)", "Glaïeul (Gladiolus hortulanus)", "Lys (Lilium candidum)",
			"Violette (Viola odorata)", "Muguet (Convallaria majalis)", "Iris (Iris germanica)",
			"Lavandin (Lavandula intermedia)", "Érable du Japon (Acer palmatum)", "Citronnelle (Cymbopogon citratus)",
			"Pin parasol (Pinus pinea)", "Cyprès (Cupressus sempervirens)", "Olivier (Olea europaea)",
			"Papyrus (Cyperus papyrus)", "Figuier (Ficus carica)", "Eucalyptus (Eucalyptus globulus)",
			"Acacia (Acacia dealbata)", "Bégonia (Begonia semperflorens)", "Calathea (Calathea ornata)",
			"Dieffenbachia (Dieffenbachia seguine)", "Ficus elastica", "Sansevieria (Sansevieria trifasciata)",
			"Philodendron (Philodendron scandens)", "Yucca (Yucca elephantipes)", "Zamioculcas zamiifolia",
			"Monstera deliciosa", "Pothos (Epipremnum aureum)", "Agave (Agave americana)",
			"Cactus raquette (Opuntia ficus-indica)",
			"Palmier-dattier (Phoenix dactylifera)", "Amaryllis (Hippeastrum hybridum)", "Bleuet (Centaurea cyanus)",
			"Cœur-de-Marie (Lamprocapnos spectabilis)", "Croton (Codiaeum variegatum)", "Dracaena (Dracaena marginata)",
			"Hosta (Hosta plantaginea)", "Lierre (Hedera helix)", "Mimosa (Acacia dealbata)");
	private static final List<String> PLANT_CATEGORY_LIST = Arrays.asList("intérieur", "extérieur");

	// # Constructeur
	public DatabaseSeeder(
			PlantRepository plantRepository,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			OrderRepository orderRepository) {
		this.plantRepository = plantRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.orderRepository = orderRepository;
	}

	// # Point d'entrée principal de la seed
	@Override
	public void run(String... args) throws Exception {
		resetDatabaseData();
		createFixedAdminUsers();
		createStandardUsers();
		createPlantsList();
		generateUserCredentialsFile();
		System.out.println("Database seeded successfully!");
	}

	// # Suppression des données existantes dans le bon ordre (respect contraintes
	// FK)
	private void resetDatabaseData() {
		orderRepository.deleteAll();
		plantRepository.deleteAll();
		userRepository.deleteAll();
	}

	// # Création des administrateurs fixes (adminX@planteshop.com / password)
	private void createFixedAdminUsers() {
		for (int adminIndex = 1; adminIndex <= NUMBER_OF_ADMINS; adminIndex++) {
			String adminUsername = "admin" + adminIndex;
			User adminUser = new User();
			adminUser.setName(adminUsername);
			adminUser.setEmail(adminUsername + "@planteshop.com");
			adminUser.setPassword(passwordEncoder.encode("password"));
			adminUser.setRole(RoleType.ADMIN);
			userRepository.save(adminUser);
			userCredentialsList.add(new Credential(adminUser.getEmail(), "password", true));
			System.err.println("DEBUG : 📝 Admin fixe : " + adminUser.getEmail() + " | password");
		}
	}

	// # Création des utilisateurs classiques
	private void createStandardUsers() {
		for (int userIndex = 0; userIndex < NUMBER_OF_USERS; userIndex++) {
			String fullName = faker.name().fullName();
			String generatedEmail = generateEmailFromFullName(fullName);
			String generatedPassword = faker.internet().password(10, 16, true, true);
			User standardUser = new User();
			standardUser.setName(fullName);
			standardUser.setEmail(generatedEmail);
			standardUser.setPassword(passwordEncoder.encode(generatedPassword));
			standardUser.setRole(RoleType.USER);
			userRepository.save(standardUser);
			userCredentialsList.add(new Credential(generatedEmail, generatedPassword, false));
			System.err.println("DEBUG : 📝 User : " + generatedEmail + " | " + generatedPassword);
		}
	}

	// # Retourne un nom de plante (suffixe si > 60)
	private String getPlantNameFromIndex(int plantIndex) {
		int plantNamesCount = PLANT_NAMES_LIST.size();
		if (NUMBER_OF_PLANTS > plantNamesCount) {
			return PLANT_NAMES_LIST.get(plantIndex % plantNamesCount) + " " + (plantIndex / plantNamesCount + 1);
		}
		return PLANT_NAMES_LIST.get(plantIndex % plantNamesCount);
	}

	// # Création des plantes
	private void createPlantsList() {
		List<Plant> plantsToInsertList = new ArrayList<>();
		for (int plantIndex = 0; plantIndex < NUMBER_OF_PLANTS; plantIndex++) {
			Plant plantEntity = new Plant();
			plantEntity.setName(getPlantNameFromIndex(plantIndex));
			plantEntity
					.setDescription("Magnifique " + PLANT_NAMES_LIST.get(plantIndex % PLANT_NAMES_LIST.size()).toLowerCase());
			plantEntity.setPrice(5 + (plantIndex * 2.5));
			plantEntity.setCategory(PLANT_CATEGORY_LIST.get(plantIndex % PLANT_CATEGORY_LIST.size()));
			plantEntity.setStock(10 + plantIndex);
			plantsToInsertList.add(plantEntity);
		}
		plantRepository.saveAll(plantsToInsertList);
	}

	// # Génération du fichier des credentials (users.txt)
	private void generateUserCredentialsFile() throws IOException {
		try (FileWriter fileWriter = new FileWriter("users.txt")) {
			fileWriter.write("=== ADMINS ===\n");
			for (Credential adminCredential : userCredentialsList) {
				if (adminCredential.isAdmin())
					writeCredential(fileWriter, adminCredential);
			}
			fileWriter.write("\n=== USERS ===\n");
			for (Credential userCredential : userCredentialsList) {
				if (!userCredential.isAdmin())
					writeCredential(fileWriter, userCredential);
			}
		}
	}

	// # Écriture d'une ligne credentials
	private void writeCredential(FileWriter fileWriter, Credential credentialEntity) {
		try {
			fileWriter.write(credentialEntity.email + " " + credentialEntity.password + "\n");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	// # Génère un email "slug" à partir du nom
	private String generateEmailFromFullName(String fullName) {
		String slugName = fullName.trim().toLowerCase().replaceAll("[^a-z ]", "").replaceAll("\\s+", ".");
		return slugName + "@planteshop.com";
	}

	// # Classe interne Credential pour stocker les credentials
	private static class Credential {
		String email;
		String password;
		boolean admin;

		public Credential(String email, String password, boolean admin) {
			this.email = email;
			this.password = password;
			this.admin = admin;
		}

		public boolean isAdmin() {
			return admin;
		}
	}
}
