package visitor.domain;

public class RoadSegment implements MapNode {
    private final String identifier;
    private final double lengthKm;
    private final int lanes;

    public RoadSegment(String identifier, double lengthKm, int lanes) {
        if (lengthKm <= 0 || lanes <= 0) {
            throw new IllegalArgumentException("Longitud vial invalida: " + lengthKm);
        }
        this.identifier = identifier;
        this.lengthKm = lengthKm;
        this.lanes = lanes;
    }

    @Override
    public String getIdentifier() { return identifier; }
    public double getLengthKm() { return lengthKm; }
    public int getLanes() { return lanes; }
    public double computeLaneKm() { return lengthKm * lanes; }

    @Override
    public void accept(MapNodeVisitor visitor) {
        visitor.visitRoad(this);
    }
}
