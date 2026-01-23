package com.mockra.api.events;

import com.mockra.api.config.MockraConfig;

public record ConfigLoadedEvent(MockraConfig config, boolean hotReload) {}
