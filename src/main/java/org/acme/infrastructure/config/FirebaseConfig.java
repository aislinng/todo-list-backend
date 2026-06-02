package org.acme.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

@Startup
@ApplicationScoped
public class FirebaseConfig {

    @ConfigProperty(name = "firebase.credentials")
    Optional<String> credentialsPath;

    void onStart(@Observes StartupEvent ev) {
        if (credentialsPath.isEmpty() || credentialsPath.get().isBlank()) {
            Log.warn("firebase.credentials no configurado — Firebase Admin SDK no inicializado");
            return;
        }
        if (!FirebaseApp.getApps().isEmpty()) return;

        String path = credentialsPath.get();
        try (InputStream serviceAccount = new FileInputStream(path)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            Log.info("Firebase Admin SDK inicializado correctamente");
        } catch (Exception e) {
            Log.errorf(e, "Error al inicializar Firebase Admin SDK (path=%s)", path);
            throw new RuntimeException("No se pudo inicializar Firebase Admin SDK", e);
        }
    }
}

// PRUEBA DE CAMBIO EN RAMA DE PRUEBA, NO SE DEBE ACEPTAR ESTE CAMBIO, SOLO ES PARA PROBAR EL FUNCIONAMIENTO DE LAS RAMAS
