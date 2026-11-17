package visitor.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeoJsonExportVisitor implements MapNodeVisitor {
    private final List<String> features = new ArrayList<>();

    @Override
    public void visitBuilding(ResidentialBuilding b) {
        features.add(String.format(Locale.US,
                "{\"kind\":\"Building\",\"id\":\"%s\",\"area\":%.1f}",
                b.getIdentifier(), b.computeTotalArea()));
    }

    @Override
    public void visitRoad(RoadSegment r) {
        features.add(String.format(Locale.US,
                "{\"kind\":\"Road\",\"id\":\"%s\",\"laneKm\":%.1f}",
                r.getIdentifier(), r.computeLaneKm()));
    }

    @Override
    public String getResult() {
        return "{\"features\":[" + String.join(",", features) + "]}";
    }
}
