package visitor.app;

import visitor.domain.CityMap;
import visitor.domain.MapNodeVisitor;
import visitor.io.OutputSink;

public class MapAnalysisService {
    private final CityMap cityMap;
    private final OutputSink sink;

    public MapAnalysisService(CityMap cityMap, OutputSink sink) {
        this.cityMap = cityMap;
        this.sink = sink;
    }

    public void execute(MapNodeVisitor visitor) {
        cityMap.apply(visitor);
        sink.writeLine(visitor.getResult());
    }
}
