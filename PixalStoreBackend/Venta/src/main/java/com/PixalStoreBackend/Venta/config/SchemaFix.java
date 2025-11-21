package com.PixalStoreBackend.Venta.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaFix {

    @PersistenceContext
    private EntityManager em;

    @Bean
    CommandLineRunner dropUniqueIndexIfExists() {
        return args -> {
            
            String tableExistsSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'venta'";
            Number tableExists = ((Number) em.createNativeQuery(tableExistsSql).getSingleResult());
            if (tableExists.intValue() == 0) {
                return; // Nothing to do
            }

            
            String indexName = "UKesugfh42xi6p0dv2ieg68a8ax";
            String indexExistsSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'venta' AND INDEX_NAME = '" + indexName + "'";
            Number idxExists = ((Number) em.createNativeQuery(indexExistsSql).getSingleResult());
            if (idxExists.intValue() > 0) {
                em.createNativeQuery("ALTER TABLE `venta` DROP INDEX `" + indexName + "`").executeUpdate();
            } else {
                
                var names = em.createNativeQuery(
                        "SELECT DISTINCT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'venta' AND COLUMN_NAME = 'nombreUsuario' AND NON_UNIQUE = 0")
                        .getResultList();
                for (Object n : names) {
                    if (n != null) {
                        String idx = n.toString();
                        em.createNativeQuery("ALTER TABLE `venta` DROP INDEX `" + idx + "`").executeUpdate();
                    }
                }
            }
        };
    }
}
