package visitor.domain;

public class ResidentialBuilding implements MapNode {
    private final String identifier;
    private final int floors;
    private final double floorAreaSqm;

    public ResidentialBuilding(String identifier, int floors, double floorAreaSqm) {
        if (floors <= 0 || floorAreaSqm <= 0) {
            throw new IllegalArgumentException("Dimensiones de edificacion invalidas");
        }
        this.identifier = identifier;
        this.floors = floors;
        this.floorAreaSqm = floorAreaSqm;
    }

    @Override
    public String getIdentifier() { return identifier; }
    public int getFloors() { return floors; }
    public double getFloorAreaSqm() { return floorAreaSqm; }
    public double computeTotalArea() { return floors * floorAreaSqm; }

    @Override
    public void accept(MapNodeVisitor visitor) {
        visitor.visitBuilding(this);
    }
}
