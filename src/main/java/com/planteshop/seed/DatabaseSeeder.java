package com.planteshop.seed;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.planteshop.model.entity.Plant;
import com.planteshop.model.entity.User;
import com.planteshop.model.enums.RoleType;
import com.planteshop.repository.PlantRepository;
import com.planteshop.repository.UserRepository;

import net.datafaker.Faker;

@Component
@Profile({ "seed", "init" })
public class DatabaseSeeder implements CommandLineRunner {

	private final PlantRepository plantRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Faker faker = new Faker();

	private final List<Credential> credentials = new ArrayList<>();

	private static final List<String> PLANT_NAMES = Arrays.asList(
			"Rose", "Tulipe", "Lavande", "Orchidée", "Basilic",
			"Menthe", "Pivoine", "Tournesol", "Cactus", "Bambou");
	private static final List<String> PLANT_CATEGORIES = Arrays.asList("intérieur", "extérieur");

	private static final int NB_PLANTS = 30;
	private static final int NB_ADMINS = 3;
	private static final int NB_USERS = 15;

	public DatabaseSeeder(PlantRepository plantRepository,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.plantRepository = plantRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		// Suppression explicite des données existantes (ordre important à respecter)
		plantRepository.deleteAll();
		userRepository.deleteAll();

		List<User> users = createUsers();
		users.addAll(createFixedAdmins());
		createPlants();
		generateUsersFile();
		System.out.println("Database seeded successfully!");
	}

	private List<User> createUsers() {
		List<User> users = new ArrayList<>();

		for (int i = 0; i < NB_ADMINS; i++) {
			users.add(createUser(true));
		}

		for (int i = 0; i < NB_USERS; i++) {
			users.add(createUser(false));
		}

		return userRepository.saveAll(users);
	}

	private User createUser(boolean isAdmin) {
		String name = faker.name().fullName();
		String email = generateEmailFromName(name);
		String rawPassword = faker.color().name().replaceAll("\\s+", "").toLowerCase()
				+ faker.animal().name().replaceAll("\\s+", "").toLowerCase()
				+ faker.number().numberBetween(10, 99);

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setRole(isAdmin ? RoleType.ADMIN : RoleType.USER);

		credentials.add(new Credential(email, rawPassword, isAdmin));
		System.err.println("DEBUG : 📝 Créé : " + email + " | " + rawPassword + " | role=" + (isAdmin ? "ADMIN" : "USER"));

		return user;
	}

	private User createFixedAdmin(String username, String rawPassword) {
		User user = new User();
		user.setName(username);
		user.setEmail(username + "@planteshop.com");
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setRole(RoleType.ADMIN);

		credentials.add(new Credential(user.getEmail(), rawPassword, true));
		System.err.println("DEBUG : 📝 Admin fixe : " + user.getEmail() + " | " + rawPassword);

		return user;
	}

	private List<User> createFixedAdmins() {
		List<User> fixedAdmins = new ArrayList<>();

		fixedAdmins.add(createFixedAdmin("admin_exemple_1", "password"));
		fixedAdmins.add(createFixedAdmin("admin_exemple_2", "password"));
		fixedAdmins.add(createFixedAdmin("admin_exemple_3", "password"));

		return userRepository.saveAll(fixedAdmins);
	}

	private String generateEmailFromName(String name) {
		String slug = name.trim().toLowerCase().replaceAll("[^a-z ]", "").replaceAll("\\s+", ".");
		return slug + "@planteshop.com";
	}

	private void createPlants() {
		List<Plant> plants = new ArrayList<>();

		for (int i = 0; i < NB_PLANTS; i++) {
			Plant plant = new Plant();
			plant.setName(PLANT_NAMES.get(i % PLANT_NAMES.size()) + " " + (i + 1));
			plant.setDescription("Magnifique " + PLANT_NAMES.get(i % PLANT_NAMES.size()).toLowerCase());
			plant.setPrice(5 + (i * 2.5));
			plant.setCategory(PLANT_CATEGORIES.get(i % PLANT_CATEGORIES.size()));
			plant.setStock(10 + i);
			plants.add(plant);
		}

		plantRepository.saveAll(plants);
	}

	private void generateUsersFile() throws IOException {
		try (FileWriter writer = new FileWriter("users.txt")) {
			writer.write("=== ADMINS ===\n");
			credentials.stream()
					.filter(Credential::isAdmin)
					.forEach(c -> writeCredential(writer, c));

			writer.write("\n=== USERS ===\n");
			credentials.stream()
					.filter(c -> !c.isAdmin())
					.forEach(c -> writeCredential(writer, c));
		}
	}

	private void writeCredential(FileWriter writer, Credential c) {
		try {
			writer.write(c.email + " " + c.password + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
