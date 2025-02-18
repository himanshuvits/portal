package com.travel.portal.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import static org.mockito.Mockito.*;

class AppRepositoryConfigTest {
    @Test
    void testConfigureRepositoryRestConfiguration() {
        AppRepositoryConfig config = new AppRepositoryConfig();
        RepositoryRestConfiguration restConfig = mock(RepositoryRestConfiguration.class);
        CorsRegistry corsRegistry = mock(CorsRegistry.class);
        config.configureRepositoryRestConfiguration(restConfig, corsRegistry);
        verify(restConfig).setExposeRepositoryMethodsByDefault(false);
    }

    @Test
    void testConfigureRepositoryRestConfigurationWithNullCors() {
        AppRepositoryConfig config = new AppRepositoryConfig();
        RepositoryRestConfiguration restConfig = mock(RepositoryRestConfiguration.class);
        config.configureRepositoryRestConfiguration(restConfig, null);
        verify(restConfig).setExposeRepositoryMethodsByDefault(false);
    }
}
