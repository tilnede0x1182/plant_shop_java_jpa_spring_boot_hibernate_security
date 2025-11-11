package com.planteshop.seed;

import com.planteshop.PlantShopApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

public final class SeedRunner {

	private SeedRunner() {
	}

	public static void main(String[] args) {
		System.out.println("🌱 Initialisation de la base Plant Shop (profil seed)...");
		SpringApplication app = new SpringApplication(PlantShopApplication.class);
		app.setAdditionalProfiles("seed");
		app.setBannerMode(Banner.Mode.OFF);
		app.setWebApplicationType(WebApplicationType.NONE);

		ConfigurableApplicationContext context = app.run(args);
		System.out.println("✅ Seed exécutée avec succès, arrêt du contexte Spring.");
		SpringApplication.exit(context, () -> 0);
	}
}
