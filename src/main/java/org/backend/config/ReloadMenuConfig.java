package org.backend.config;

import jakarta.annotation.PostConstruct;
import org.backend.entity.Menu;
import org.backend.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReloadMenuConfig {

    private final MenuRepository menuRepository;
    private Map<Long, Menu> activeMenuCache; // In-memory cache for active menus

    public ReloadMenuConfig(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // Method to load the menus into memory when the application starts
    @PostConstruct
    public void loadActiveMenusIntoMemory() {
        List<Menu> activeMenus = menuRepository.findAllByActive(true);
        // Convert list to a map for quicker access, or keep as a list based on your use case
        activeMenuCache = activeMenus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));
    }

    // Method to get all active menus from the cache
    public List<Menu> getActiveMenus() {
        return new ArrayList<>(activeMenuCache.values());
    }

    // Optionally, if you need to retrieve a specific menu by ID
    public Menu getMenuById(Long id) {
        return activeMenuCache.get(id);
    }
}
