package visitor.domain;

import java.util.ArrayList;
import java.util.List;

public class CityMap {
    private final List<MapNode> nodes = new ArrayList<>();

    public void register(MapNode node) {
        nodes.add(node);
    }

    public void apply(MapNodeVisitor visitor) {
        for (MapNode node : nodes) {
            node.accept(visitor);
        }
    }
}
