package visitor.domain;

public interface MapNode {
    String getIdentifier();
    void accept(MapNodeVisitor visitor);
}
