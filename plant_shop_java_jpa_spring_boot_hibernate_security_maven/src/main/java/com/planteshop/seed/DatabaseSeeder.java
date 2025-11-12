package com.planteshop.seed;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.planteshop.model.entity.CustomerOrder;
import com.planteshop.model.entity.OrderItem;
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
    private static final int NUMBER_OF_PLANTS = 50; // 🌱 Nombre de plantes à créer
    private static final int NUMBER_OF_ADMINS = 3; // 👤 Nombre d'administrateurs à créer
    private static final int NUMBER_OF_USERS = 20; // 👥 Nombre d'utilisateurs à créer
    private static final int MAX_ORDERS_PER_USER = 7; // 🛒 Nombre max de commandes par utilisateur
    private static final int ITEMS_PER_ORDER = 2; // 📦 Articles par commande
    private static final int MAX_QTY_PER_ITEM = 5;
    private static final List<String> ORDER_STATUSES = Arrays.asList("confirmed", "pending", "shipped", "delivered");

    private final Random random = new Random();

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
        System.out.println("🔧 Lecture de la configuration .env...");

        System.out.println("🧹 Nettoyage de la base de données…");
        resetDatabaseData();
        System.out.println("✅ Base vidée.");

        System.out.println("👑 Création des administrateurs…");
        int adminCount = createFixedAdminUsers();
        System.out.println("✅ " + adminCount + " admins.");

        System.out.println("👥 Création des utilisateurs…");
        int userCount = createStandardUsers();
        System.out.println("✅ " + userCount + " utilisateurs.");

        System.out.println("🌱 Création des plantes…");
        int plantCount = createPlantsList();
        System.out.println("✅ " + plantCount + " plantes.");

        System.out.println("🛒 Création des commandes…");
        int ordersCount = createCustomerOrders();
        System.out.println("✅ " + ordersCount + " commandes.");

        int linesWritten = generateUserCredentialsFile();
        System.out.println("✍️ Fichier users.txt généré (" + linesWritten + " lignes).");
        System.out.println("🎉 Seed terminée !");
    }

	// # Suppression des données existantes dans le bon ordre (respect contraintes
	// FK)
    private void resetDatabaseData() {
        orderRepository.deleteAll();
        plantRepository.deleteAll();
        userRepository.deleteAll();
    }

	// # Création des administrateurs fixes (adminX@planteshop.com / password)
    private int createFixedAdminUsers() {
        for (int adminIndex = 1; adminIndex <= NUMBER_OF_ADMINS; adminIndex++) {
            String adminUsernameForEmail = "admin" + adminIndex;
            String adminUsername = faker.name().fullName();
            User adminUser = new User();
            adminUser.setName(adminUsername);
            adminUser.setEmail(adminUsernameForEmail + "@planteshop.com");
            adminUser.setPassword(passwordEncoder.encode("password"));
            adminUser.setRole(RoleType.ADMIN);
            userRepository.save(adminUser);
            userCredentialsList.add(new Credential(adminUser.getEmail(), "password", true));
        }
        return NUMBER_OF_ADMINS;
    }

	// # Création des utilisateurs classiques
    private int createStandardUsers() {
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
        }
        return NUMBER_OF_USERS;
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
    private int createPlantsList() {
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
        return plantsToInsertList.size();
    }

    private int createCustomerOrders() {
        List<User> users = userRepository.findAll();
        List<Plant> plants = plantRepository.findAll();
        if (plants.isEmpty()) {
            return 0;
        }

        List<PlantStock> plantStocks = new ArrayList<>();
        for (Plant plant : plants) {
            plantStocks.add(new PlantStock(plant));
        }

        int totalOrders = 0;
        for (User user : users) {
            if (user.getRole() != RoleType.USER) {
                continue;
            }

            int ordersForUser = random.nextInt(MAX_ORDERS_PER_USER + 1);
            for (int orderIndex = 0; orderIndex < ordersForUser; orderIndex++) {
                if (availableStockCount(plantStocks) == 0) {
                    break;
                }

                CustomerOrder order = new CustomerOrder();
                order.setUser(user);
                order.setStatus(randomStatus());
                order.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));

                double totalPrice = 0;
                int itemsCreated = 0;
                while (itemsCreated < ITEMS_PER_ORDER) {
                    PlantStock selectedStock = pickPlantWithStock(plantStocks);
                    if (selectedStock == null) {
                        break;
                    }

                    int maxQty = Math.min(MAX_QTY_PER_ITEM, selectedStock.remainingStock);
                    if (maxQty <= 0) {
                        continue;
                    }
                    int quantity = 1 + random.nextInt(maxQty);

                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setPlant(selectedStock.plant);
                    item.setQuantity(quantity);
                    item.setUnitPrice(selectedStock.plant.getPrice());

                    order.getItems().add(item);
                    totalPrice += quantity * selectedStock.plant.getPrice();

                    selectedStock.consume(quantity);
                    itemsCreated++;
                }

                if (order.getItems().isEmpty()) {
                    continue;
                }

                order.setTotalPrice(totalPrice);
                orderRepository.save(order);
                totalOrders++;
            }
        }

        // Persiste les stocks mis à jour
        plantRepository.saveAll(plants);
        return totalOrders;
    }

    private int availableStockCount(List<PlantStock> stocks) {
        int total = 0;
        for (PlantStock stock : stocks) {
            if (stock.remainingStock > 0) {
                total++;
            }
        }
        return total;
    }

    private PlantStock pickPlantWithStock(List<PlantStock> stocks) {
        List<PlantStock> withStock = new ArrayList<>();
        for (PlantStock stock : stocks) {
            if (stock.remainingStock > 0) {
                withStock.add(stock);
            }
        }
        if (withStock.isEmpty()) {
            return null;
        }
        return withStock.get(random.nextInt(withStock.size()));
    }

    private String randomStatus() {
        return ORDER_STATUSES.get(random.nextInt(ORDER_STATUSES.size()));
    }

    // # Génération du fichier des credentials (users.txt)
    private int generateUserCredentialsFile() throws IOException {
        int linesWritten = 0;
        try (FileWriter fileWriter = new FileWriter("users.txt")) {
            fileWriter.write("=== ADMINS ===\n");
            linesWritten++;
            for (Credential adminCredential : userCredentialsList) {
                if (adminCredential.isAdmin()) {
                    writeCredential(fileWriter, adminCredential);
                    linesWritten++;
                }
            }

            fileWriter.write("\n");
            linesWritten++;
            fileWriter.write("=== USERS ===\n");
            linesWritten++;
            for (Credential userCredential : userCredentialsList) {
                if (!userCredential.isAdmin()) {
                    writeCredential(fileWriter, userCredential);
                    linesWritten++;
                }
            }
        }
        return linesWritten;
    }

    private static class PlantStock {
        private final Plant plant;
        private int remainingStock;

        PlantStock(Plant plant) {
            this.plant = plant;
            this.remainingStock = plant.getStock() == null ? 0 : plant.getStock();
        }

        void consume(int quantity) {
            remainingStock = Math.max(0, remainingStock - quantity);
            plant.setStock(remainingStock);
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
