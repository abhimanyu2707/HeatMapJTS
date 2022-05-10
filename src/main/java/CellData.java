import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.Serializable;

public class CellData implements Serializable {
    String pais_uid;
    String markup_id;
    Geometry polygon;
    Envelope envelope;
    Integer counts;

    public CellData() {
        this.pais_uid = "";
        this.markup_id = "";
        this.polygon = null;
        this.envelope = new Envelope();
        this.counts = -1;
    }
    public CellData(String pais_uid, String markup_id, String polygonStr) throws ParseException {
        this.pais_uid = pais_uid;
        this.markup_id = markup_id;
        WKTReader reader = new WKTReader();
        this.polygon = reader.read(polygonStr);
        this.envelope = this.polygon.getEnvelopeInternal();
        this.counts = -1;
    }

    public String getPais_uid() {
        return pais_uid;
    }

    public void setPais_uid(String pais_uid) {
        this.pais_uid = pais_uid;
    }

    public String getMarkup_id() {
        return markup_id;
    }

    public void setMarkup_id(String markup_id) {
        this.markup_id = markup_id;
    }

    public Geometry getPolygon() {
        return polygon;
    }

    public void setPolygon(Geometry polygon) {
        this.polygon = polygon;
    }

    public void setPolygonFromEnvelope() {
        GeometryFactory geometryFactory = new GeometryFactory();
        this.setPolygon(geometryFactory.toGeometry(this.envelope));
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public CellData union(CellData c2) {
        CellData c = new CellData();
        c.getEnvelope().expandToInclude(this.getEnvelope());
        c.getEnvelope().expandToInclude(c2.getEnvelope());
        return  c;
    }
}