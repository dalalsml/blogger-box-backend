package com.dauphine.blogger.config;

import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.CategoryRepository;
import com.dauphine.blogger.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public DataSeeder(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, Category> categoriesByKey = categoryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        category -> normalize(category.getName()),
                        category -> category,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        List<String> categoryNames = List.of(
                "Technology",
                "Science",
                "Health",
                "Travel",
                "Food",
                "Sports",
                "Cinema",
                "Music",
                "Finance",
                "History",
                "Education",
                "Environment"
        );

        for (String categoryName : categoryNames) {
            String categoryKey = normalize(categoryName);
            if (!categoriesByKey.containsKey(categoryKey)) {
                Category savedCategory = categoryRepository.save(new Category(categoryName));
                categoriesByKey.put(categoryKey, savedCategory);
            }
        }

        Set<String> existingPostKeys = postRepository.findAll()
                .stream()
                .map(post -> postKey(post.getTitle(), post.getCategory().getName()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<PostSeed> seeds = List.of(
                new PostSeed("How AI is changing daily work", "Practical examples of AI assistants in writing, coding, and planning.", "Technology"),
                new PostSeed("Cybersecurity basics for beginners", "Simple habits to protect accounts, devices, and personal data online.", "Technology"),
                new PostSeed("The science behind sleep quality", "Why consistent sleep schedules improve focus, memory, and mood.", "Science"),
                new PostSeed("Space telescopes and new discoveries", "How modern telescopes help researchers understand distant galaxies.", "Science"),
                new PostSeed("Healthy meal prep in 30 minutes", "A weekly method to build balanced meals with minimal effort.", "Health"),
                new PostSeed("Cardio vs strength training", "When to prioritize endurance, muscle building, or a mix of both.", "Health"),
                new PostSeed("Weekend trip ideas in Europe", "Short itineraries for affordable city breaks and scenic escapes.", "Travel"),
                new PostSeed("How to pack light for 7 days", "A compact packing list that works for most climates and activities.", "Travel"),
                new PostSeed("Easy homemade pasta guide", "A beginner workflow for dough, shaping, and quick sauces.", "Food"),
                new PostSeed("Street food traditions around the world", "Popular local dishes and the culture behind each one.", "Food"),
                new PostSeed("Training plan for a first 10K", "A progressive schedule for new runners with recovery tips.", "Sports"),
                new PostSeed("Football tactics explained simply", "How pressing, transitions, and spacing influence match outcomes.", "Sports"),
                new PostSeed("Best films to discover this year", "A curated list across drama, thriller, animation, and indie cinema.", "Cinema"),
                new PostSeed("How directors use color storytelling", "Examples of mood and symbolism through palette choices.", "Cinema"),
                new PostSeed("Building a focused practice routine", "How musicians structure sessions to improve technique steadily.", "Music"),
                new PostSeed("Genres that shaped modern pop", "From funk to electronic, key influences behind current hits.", "Music"),
                new PostSeed("Budgeting method for students", "A simple monthly framework to track spending and save money.", "Finance"),
                new PostSeed("Understanding inflation in plain words", "What inflation means for rent, food, salaries, and savings.", "Finance"),
                new PostSeed("Ancient cities that changed trade", "How geography and infrastructure built powerful exchange hubs.", "History"),
                new PostSeed("Major inventions of the 19th century", "Technologies that transformed communication, transport, and industry.", "History"),
                new PostSeed("Study techniques that actually work", "Evidence based methods for revision, recall, and exam prep.", "Education"),
                new PostSeed("How to learn faster with projects", "Turning theory into practical exercises to retain knowledge.", "Education"),
                new PostSeed("Urban actions to reduce waste", "Community habits that lower landfill pressure and energy usage.", "Environment"),
                new PostSeed("Renewable energy trends in 2026", "Solar, wind, and storage improvements driving cleaner grids.", "Environment")
        );

        List<Post> postsToCreate = new ArrayList<>();
        for (PostSeed seed : seeds) {
            Category category = categoriesByKey.get(normalize(seed.categoryName()));
            if (category == null) {
                continue;
            }

            String key = postKey(seed.title(), category.getName());
            if (!existingPostKeys.contains(key)) {
                postsToCreate.add(new Post(seed.title(), seed.content(), category));
                existingPostKeys.add(key);
            }
        }

        if (!postsToCreate.isEmpty()) {
            postRepository.saveAll(postsToCreate);
        }
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static String postKey(String title, String categoryName) {
        return normalize(categoryName) + "::" + normalize(title);
    }

    private record PostSeed(String title, String content, String categoryName) {
    }
}
