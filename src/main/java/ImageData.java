import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageData implements Serializable {
    GeometryCollection geometryCollection;
    List<Geometry> geometryList;
    GeometryFactory gf;
    Integer count;

    public ImageData() {
        this.geometryList = new ArrayList<Geometry>();
        this.gf = new GeometryFactory();
        this.count = 0;
    }

    public List<Geometry> getGeometryList() {
        return geometryList;
    }

    public void setGeometryList(ArrayList<Geometry> geometryList) {
        this.geometryList = geometryList;
    }

    public void addToGeometryCollection(Geometry geometry) {
//        System.out.println("addToGeometryCollection: "+ geometry.toString());
        this.geometryList.add(geometry);
        this.count++;
//        System.out.println("Size : " + geometryList.size());
    }
    public void updateGeometryCollection() {
        Geometry []geoms = this.geometryList.toArray(new Geometry[0]);
        this.geometryCollection = new GeometryCollection(geoms,this.gf);
    }

    public Geometry getBoundingBox() {
        return geometryCollection.getEnvelope();
    }
}
