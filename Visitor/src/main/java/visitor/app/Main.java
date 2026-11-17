package visitor.app;

import visitor.domain.CityMap;
import visitor.domain.GeoJsonExportVisitor;
import visitor.domain.MaintenanceCostVisitor;
import visitor.domain.ResidentialBuilding;
import visitor.domain.RoadSegment;
import visitor.io.ConsoleSink;
import visitor.io.OutputSink;

public class Main {
    public static void main(String[] args) {
        OutputSink sink = new ConsoleSink();
        CityMap map = new CityMap();
        map.register(new ResidentialBuilding("Torre Norte", 8, 420.0));
        map.register(new RoadSegment("Av. Central", 2.5, 4));
        MapAnalysisService service = new MapAnalysisService(map, sink);

        sink.writeLine("[CASO 1]");
        service.execute(new MaintenanceCostVisitor());
        service.execute(new GeoJsonExportVisitor());

        sink.writeLine("[CASO 2]");
        try {
            map.register(new RoadSegment("Tramo Sur", -1.2, 2));
        } catch (IllegalArgumentException ex) {
            sink.writeLine("ERROR: " + ex.getMessage());
        }
    }
}
