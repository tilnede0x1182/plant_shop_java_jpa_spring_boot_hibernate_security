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
	 * Point d'entree principal de l'application Spring Boot.
	 * Configure le port, verifie sa disponibilite et demarre le serveur.
	 *
	 * @param args String[] arguments de ligne de commande
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
	 * Resout le port a utiliser en verifiant les variables d'environnement et proprietes systeme.
	 * Ordre de priorite : SERVER_ADDRESS, SERVER_ADRRESS (typo), SERVER_PORT, server.port, defaut.
	 *
	 * @return int le port resolu
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
	 * Parse une valeur brute pour en extraire un numero de port valide.
	 * Gere les formats "port" ou "host:port".
	 *
	 * @param rawValue String valeur brute potentiellement contenant un port
	 * @return Integer le port parse ou null si invalide
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
	 * Verifie si un port est disponible pour l'ecoute.
	 *
	 * @param port int le port a verifier
	 * @return boolean true si le port est disponible, false sinon
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
	 * Configure le filtre pour supporter les methodes HTTP cachees (PUT, DELETE via POST).
	 *
	 * @return HiddenHttpMethodFilter le filtre configure
	 */
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
