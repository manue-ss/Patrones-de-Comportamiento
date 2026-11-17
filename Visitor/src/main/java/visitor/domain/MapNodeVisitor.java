package visitor.domain;

public interface MapNodeVisitor {
    void visitBuilding(ResidentialBuilding building);
    void visitRoad(RoadSegment road);
    String getResult();
}
