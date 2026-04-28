package com.curling.service;

import com.curling.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ApiService {
    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient client;
    private final ObjectMapper mapper;
    private static ApiService instance;
    
    private ApiService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }
    
    public static ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }
    
    private HttpRequest.Builder createRequest(String path) {
        String url = BASE_URL + path;
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");
        
        String token = AuthManager.getInstance().getToken();
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        
        return builder;
    }
    
    // ==================== AUTH ====================
    
    public String login(String email, String password) throws Exception {
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
                
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            Map<String, String> map = mapper.readValue(response.body(), new TypeReference<>() {});
            return map.get("token");
        } else if (response.statusCode() == 403) {
            throw new Exception("Credenciales inválidas");
        } else {
            throw new Exception("Error del servidor: " + response.statusCode());
        }
    }
    
    // ==================== USERS ====================
    
    public User getProfile() throws Exception {
        HttpRequest request = createRequest("/api/users/profile").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), User.class);
        }
        throw new Exception("No se pudo obtener el perfil");
    }
    
    public List<User> getAllUsers() throws Exception {
        HttpRequest request = createRequest("/api/users").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<User>>() {});
        }
        throw new Exception("Error al obtener usuarios: " + response.statusCode());
    }
    
    public void updateUser(String userId, User user) throws Exception {
        String json = mapper.writeValueAsString(user);
        HttpRequest request = createRequest("/api/users/" + userId)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
                
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Error al actualizar usuario");
        }
    }
    
    public void deleteUser(String userId) throws Exception {
        HttpRequest request = createRequest("/api/users/" + userId).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new Exception("Error al eliminar usuario");
        }
    }
    
    // ==================== RESERVATIONS ====================
    
    public List<Reservation> getAllReservations() throws Exception {
        HttpRequest request = createRequest("/api/reservations").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<Reservation>>() {});
        }
        throw new Exception("Error al obtener reservas");
    }
    
    public void deleteReservation(String id) throws Exception {
        HttpRequest request = createRequest("/api/reservations/" + id).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new Exception("Error al cancelar reserva");
        }
    }
    
    // ==================== RESULTS ====================
    
    public List<Result> getAllResults() throws Exception {
        HttpRequest request = createRequest("/api/results").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<Result>>() {});
        }
        throw new Exception("Error al obtener resultados");
    }
    
    public void updateResult(String resultId, Result result) throws Exception {
        // Nota: Este endpoint puede no existir en tu API, es una extensión necesaria para admin
        String json = mapper.writeValueAsString(result);
        HttpRequest request = createRequest("/api/results/" + resultId)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
                
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Error al actualizar resultado");
        }
    }
    
    // ==================== STATS (Mock/Simulado si no existe endpoint) ====================
    
    public StatsData getGlobalStats() throws Exception {
        // Este método simula estadísticas agregadas
        // En una implementación real, tu backend debería tener un endpoint /api/stats/global
        
        StatsData stats = new StatsData();
        
        // Obtener datos reales para calcular estadísticas
        List<User> users = getAllUsers();
        List<Reservation> reservations = getAllReservations();
        
        stats.setTotalUsers(users.size());
        stats.setTotalReservations(reservations.size());
        stats.setPendingReservations((int) reservations.stream()
                .filter(r -> "PENDIENTE".equals(r.getStatus())).count());
        
        // Calcular ocupación (simplificado)
        stats.setOccupancyRate(reservations.size() > 0 ? 
                (reservations.size() - stats.getPendingReservations()) * 100.0 / reservations.size() : 0);
        
        return stats;
    }
}