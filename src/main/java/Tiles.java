import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.Serializable;

public class Tiles implements Serializable {
    Envelope [][]envelopeTiles;
    Geometry [][]geometryTiles;
    Integer [][]counts;

    public Tiles(Envelope envelope) {
        this.envelopeTiles = new Envelope[Constant.TILES_COUNT][Constant.TILES_COUNT];
        this.geometryTiles = new Geometry[Constant.TILES_COUNT][Constant.TILES_COUNT];
        this.counts = new Integer[Constant.TILES_COUNT][Constant.TILES_COUNT];
        double minX = envelope.getMinX();
        double maxX = envelope.getMaxX();
        double minY = envelope.getMinY();
        double maxY = envelope.getMaxY();
        double xInterval = (maxX - minX)/Constant.TILES_COUNT;
        double yInterval = (maxY - minY)/Constant.TILES_COUNT;
        double y = maxY;
        GeometryFactory geometryFactory = new GeometryFactory();
        for (int j = 0 ; j < Constant.TILES_COUNT; j++) {
            double x = minX;
            for (int i = 0; i < Constant.TILES_COUNT; i++) {
                this.envelopeTiles[i][j] = new Envelope(x, x+xInterval, y, y-yInterval);
                this.geometryTiles[i][j] = geometryFactory.toGeometry(envelopeTiles[i][j]);
                x += xInterval;
            }
            y -= yInterval;
        }
    }

    public void setCount(int x, int y, int c) {
        this.counts[x][y] = c;
    }

    public Integer getCount(int x, int y) {
        return this.counts[x][y];
    }

    public Envelope[][] getEnvelopeTiles() {
        return envelopeTiles;
    }

    public void setEnvelopeTiles(Envelope[][] envelopeTiles) {
        this.envelopeTiles = envelopeTiles;
    }

    public Geometry[][] getGeometryTiles() {
        return geometryTiles;
    }

    public void setGeometryTiles(Geometry[][] geometryTiles) {
        this.geometryTiles = geometryTiles;
    }
}
