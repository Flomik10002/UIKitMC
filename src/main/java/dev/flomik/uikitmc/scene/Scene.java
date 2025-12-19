package dev.flomik.uikitmc.scene;

import dev.flomik.uikitmc.scene.node.UiNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Scene {
    private final List<UiNode> nodes = new ArrayList<>();

    public List<UiNode> nodes() {
        return Collections.unmodifiableList(nodes);
    }

    public void add(UiNode node) {
        nodes.add(node);
    }

    public void remove(UiNode node) {
        nodes.remove(node);
    }
}