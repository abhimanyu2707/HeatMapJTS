import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageData implements Serializable {
    GeometryCollection geometryCollection;
    ArrayList<Geometry> geometries;
    GeometryFactory gf;

    public ImageData() {
        this.geometries = new ArrayList<Geometry>();
        this.gf = new GeometryFactory();
    }

    public void addToGeometryCollection(Geometry geometry) {
        geometries.add(geometry);
    }
    public void updateGeometryCollection() {
        Geometry []geoms = this.geometries.toArray(new Geometry[0]);
        geometryCollection = new GeometryCollection(geoms,this.gf);
    }

    public Geometry getBoundingBox() {
        return geometryCollection.getEnvelope();
    }
}
