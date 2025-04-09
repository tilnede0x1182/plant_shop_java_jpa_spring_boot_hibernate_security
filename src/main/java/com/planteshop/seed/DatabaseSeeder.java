package com.planteshop.seed;

import com.planteshop.model.entity.Plant;
import com.planteshop.model.entity.User;
import com.planteshop.repository.PlantRepository;
import com.planteshop.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();

    // Listes prédéfinies pour des données cohérentes
    private static final List<String> PLANT_NAMES = Arrays.asList(
        "Rose", "Tulipe", "Lavande", "Orchidée", "Basilic",
        "Menthe", "Pivoine", "Tournesol", "Cactus", "Bambou"
    );
    private static final List<String> PLANT_CATEGORIES = Arrays.asList("intérieur", "extérieur");

    // Constantes de configuration
    private static final int NB_PLANTS = 30;
    private static final int NB_ADMINS = 3;
    private static final int NB_USERS = 15;
    private static final String DEFAULT_PASSWORD = "password123";

    public DatabaseSeeder(PlantRepository plantRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (plantRepository.count() == 0 && userRepository.count() == 0) {
            List<User> users = createUsers();
            createPlants();
            generateUsersFile(users);
            System.out.println("Database seeded successfully!");
        }
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();

        // Admins
        for (int i = 0; i < NB_ADMINS; i++) {
            users.add(createUser(true, i));
        }

        // Users normaux
        for (int i = 0; i < NB_USERS; i++) {
            users.add(createUser(false, i));
        }

        return userRepository.saveAll(users);
    }

    private User createUser(boolean isAdmin, int index) {
        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail((isAdmin ? "admin" : "user") + index + "@planteshop.com");
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setAdmin(isAdmin);
        return user;
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

    private void generateUsersFile(List<User> users) throws IOException {
        try (FileWriter writer = new FileWriter("users.txt")) {
            writer.write("=== CREDENTIALS ===\n\n");
            writer.write("Default password for all users: " + DEFAULT_PASSWORD + "\n\n");

            writer.write("=== ADMINS ===\n");
            users.stream()
                .filter(User::isAdmin)
                .forEach(u -> writeUser(writer, u));

            writer.write("\n=== USERS ===\n");
            users.stream()
                .filter(u -> !u.isAdmin())
                .forEach(u -> writeUser(writer, u));
        }
    }

    private void writeUser(FileWriter writer, User user) {
        try {
            writer.write(String.format(
                "Email: %s\nName: %s\nRole: %s\n\n",
                user.getEmail(),
                user.getName(),
                user.isAdmin() ? "ADMIN" : "USER"
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
