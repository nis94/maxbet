package com.maxback.container;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.GenericContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParentTestContainer {

    private static final Map<GenericContainer, Function<GenericContainer, Map<String, String>>>
        CONTAINER_TO_FUNCTION_MAP = new HashMap<>();
    private static Map<String, String> CONNECTIVITY_PROPERTIES = new HashMap<>();
    private static Map<String, GenericContainer> CONTAINER_NAME_TO_CONTAINER_MAP = new HashMap<>();

    protected static void addContainer(GenericContainer container,
                                       Function<GenericContainer, Map<String, String>> function) {
        CONTAINER_TO_FUNCTION_MAP.put(container, function);
        if (container.getLabels() != null) {
            Map<String, String> labels = container.getLabels();
            CONTAINER_NAME_TO_CONTAINER_MAP.put(labels.get("containerName"), container);
        }
    }

    protected static Map<String, String> getConnectivityProperties() {
        DateTime startedTime = DateTime.now();

        while (startedTime.plusSeconds(250).isAfter(DateTime.now())) {
            if (!allContainersAlreadyStarted()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return CONNECTIVITY_PROPERTIES;
            }
        }
        return CONNECTIVITY_PROPERTIES;
    }

    protected static boolean isNotPopulated() {
        return CONNECTIVITY_PROPERTIES.isEmpty();
    }

    @NotNull
    protected static Map<String, String> setupConnectivityProperties() {
        Map<String, String> result = new HashMap<>();

        getCompletableFuturesOfRunningContainers();

        CONTAINER_TO_FUNCTION_MAP.forEach((container, function) -> {
            Map<String, String> resultMapForContainer = function.apply(container);
            result.putAll(resultMapForContainer);
            System.err.println("Map is ready: " + resultMapForContainer);
        });

        CONNECTIVITY_PROPERTIES = result;

        return result;
    }

    @NotNull
    private static List<? extends CompletableFuture<GenericContainer>> getCompletableFuturesOfRunningContainers() {
        List<? extends CompletableFuture<GenericContainer>> futures =
            CONTAINER_TO_FUNCTION_MAP.keySet()
                .stream().map(genericContainerMapFunction -> CompletableFuture.supplyAsync(() -> {

                    genericContainerMapFunction.start();
                    System.err.println(
                        "Container " + genericContainerMapFunction.getClass().getSimpleName() + " has been started");
                    return genericContainerMapFunction;
                })).toList();

        CompletableFuture<Void> allFeatures = CompletableFuture.allOf(
            futures.toArray(CompletableFuture[]::new));

        try {
            allFeatures.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.err.println("All containers has been started");
        return futures;
    }

    private static boolean allContainersAlreadyStarted() {
        Set<Boolean> allRunningValues = CONTAINER_TO_FUNCTION_MAP.keySet().stream()
            .map(ContainerState::isRunning).collect(Collectors.toSet());
        System.err.println(
            "Status of started containers: " + CONTAINER_TO_FUNCTION_MAP.keySet().stream()
                .map(ContainerState::isRunning).toList());
        return allRunningValues.contains(Boolean.TRUE) && allRunningValues.size() == 1;
    }
}
