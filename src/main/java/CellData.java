import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.Serializable;

public class CellData implements Serializable {
    String pais_uid;
    String markup_id;
    Geometry polygon;

    public CellData(String pais_uid, String markup_id, String polygonStr) throws ParseException {
        this.pais_uid = pais_uid;
        this.markup_id = markup_id;
        WKTReader reader = new WKTReader();
        this.polygon = reader.read(polygonStr);
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
}