package visitor.domain;

import java.util.Locale;

public class MaintenanceCostVisitor implements MapNodeVisitor {
    private double totalBudget = 0.0;

    @Override
    public void visitBuilding(ResidentialBuilding building) {
        totalBudget += building.computeTotalArea() * 1.5;
    }

    @Override
    public void visitRoad(RoadSegment road) {
        totalBudget += road.computeLaneKm() * 320.0;
    }

    @Override
    public String getResult() {
        return String.format(Locale.US, "COSTO_TOTAL=%.1f", totalBudget);
    }
}
