package com.planteshop;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.io.IOException;
import java.net.ServerSocket;

@SpringBootApplication
public class PlantShopApplication {

	private static final int DEFAULT_PORT = 8080;

	/**
	 * Point d'entrée principal de l'application Plant Shop.
	 * Configure le port dynamiquement et lance le serveur Spring Boot.
	 *
	 * @param args String[] les arguments de ligne de commande
	 */
	public static void main(String[] args) {
		int port = resolvePort();

		if (!isPortAvailable(port)) {
			System.err.println("❌ Port " + port + " déjà utilisé, impossible de lancer Plant Shop.");
			return;
		}

		System.setProperty("server.port", String.valueOf(port));

		SpringApplication app = new SpringApplication(PlantShopApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.addListeners((ApplicationListener<ApplicationReadyEvent>) event ->
			System.out.println("✅ Serveur Spring prêt sur http://localhost:" + port)
		);

		System.out.println("🚀 Lancement du serveur Plant Shop sur http://localhost:" + port);
		app.run(args);
	}

	/**
	 * Résout le port du serveur en vérifiant les variables d'environnement.
	 * Ordre de priorité : SERVER_ADDRESS, SERVER_ADRRESS, SERVER_PORT, server.port.
	 *
	 * @return int le port résolu ou le port par défaut 8080
	 */
	private static int resolvePort() {
		Integer envAddress = parsePort(System.getenv("SERVER_ADDRESS"));
		Integer envAdrressTypo = parsePort(System.getenv("SERVER_ADRRESS"));
		Integer envPort = parsePort(System.getenv("SERVER_PORT"));
		Integer systemPort = parsePort(System.getProperty("server.port"));

		if (envAddress != null) return envAddress;
		if (envAdrressTypo != null) return envAdrressTypo;
		if (envPort != null) return envPort;
		if (systemPort != null) return systemPort;
		return DEFAULT_PORT;
	}

	/**
	 * Parse une chaîne pour en extraire un numéro de port valide.
	 * Gère les formats avec ou sans préfixe d'adresse (ex: localhost:8080).
	 *
	 * @param rawValue String la valeur brute à parser
	 * @return Integer le port extrait ou null si invalide
	 */
	private static Integer parsePort(String rawValue) {
		if (rawValue == null || rawValue.isBlank()) {
			return null;
		}
		String candidate = rawValue.contains(":")
			? rawValue.substring(rawValue.lastIndexOf(':') + 1)
			: rawValue;
		try {
			int port = Integer.parseInt(candidate.trim());
			if (port > 0 && port < 65536) {
				return port;
			}
		} catch (NumberFormatException ignored) {
			System.err.println("⚠️  Valeur de port invalide (" + rawValue + "), utilisation du port par défaut.");
		}
		return null;
	}

	/**
	 * Vérifie si un port TCP est disponible pour une écoute.
	 *
	 * @param port int le numéro de port à vérifier
	 * @return boolean true si le port est libre, false sinon
	 */
	private static boolean isPortAvailable(int port) {
		try (ServerSocket socket = new ServerSocket(port)) {
			socket.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Configure le filtre HTTP pour supporter les méthodes PUT/DELETE via _method.
	 *
	 * @return HiddenHttpMethodFilter le filtre configuré
	 */
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
