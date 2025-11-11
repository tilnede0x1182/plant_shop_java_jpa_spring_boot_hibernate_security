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

	private static boolean isPortAvailable(int port) {
		try (ServerSocket socket = new ServerSocket(port)) {
			socket.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
