import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

public class Tiles implements Serializable {
    Envelope [][]envelopeTiles;
    Geometry [][]geometryTiles;
    Integer [][]counts;
    double minX, minY, maxX, maxY;

    public Tiles(Envelope envelope) {
        this.envelopeTiles = new Envelope[Constant.TILES_COUNT][Constant.TILES_COUNT];
        this.geometryTiles = new Geometry[Constant.TILES_COUNT][Constant.TILES_COUNT];
        this.counts = new Integer[Constant.TILES_COUNT][Constant.TILES_COUNT];
        this.minX = envelope.getMinX();
        this.maxX = envelope.getMaxX();
        this.minY = envelope.getMinY();
        this.maxY = envelope.getMaxY();
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

    public void dumpDataToCsv() {
//        double [][]cnt = new double[Constant.TILES_COUNT][Constant.TILES_COUNT];
//        for (int i = 0; i < Constant.TILES_COUNT; i++) {
//            for (int j = 0; j < Constant.TILES_COUNT; j++) {
//                cnt[i][j] = counts[i][j].doubleValue();
//            }
//        }
        try (PrintWriter writer = new PrintWriter(Constant.TILES_CSV_FILE)) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Constant.TILES_COUNT; i++) {
                for (int j = 0; j < Constant.TILES_COUNT; j++) {
                    if(j == Constant.TILES_COUNT-1)
                        sb.append(this.counts[i][j] + "\n");
                    else
                        sb.append(this.counts[i][j] + ",");
                }
            }
            writer.write(sb.toString());
            System.out.println("done!");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
