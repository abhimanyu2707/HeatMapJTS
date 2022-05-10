import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SQLContext;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

public class CreateHeatMap {
    public static void main(String[] args) { //Driver

        /* Initialize SparkConf */
        SparkConf conf = new SparkConf()
                .setAppName("Example Spark App");
        /* set properties */
        conf.setMaster("local[*]");  // Delete this line when submitting to a cluster
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.set("textinputformat.record.delimiter", "\n");

        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaRDD<String> paisCSV = sparkContext.textFile("/Users/abhimanyu/Stony_Brook/Advanced_Project/PAIS/pais_1_subset.csv");
        SQLContext sqlContext = new SQLContext(sparkContext);
        JavaRDD<CellData> cellRDD = paisCSV.map(
                new Function<String, CellData>() {
                    public CellData call(String line) throws Exception {
                        String[] fields = line.split(",", 3);
                        fields[2] = fields[2].substring(1, fields[2].length() - 1);
                        CellData cellData = new CellData(fields[0], fields[1], fields[2]);
                        return cellData;
                    }
                });
        System.out.println("cellRDD Number of Partitions: " + cellRDD.getNumPartitions());
        System.out.println("paisCSV Number of Partitions: " + paisCSV.getNumPartitions());

        //System.out.println("Is in envelope" + cellRDD.first().getEnvelope().contains(cellRDD.first().getPolygon()));
        CellData boundingBoxCell = cellRDD.reduce(
                new Function2<CellData, CellData, CellData>(){
                    @Override
                    public CellData call(CellData v1, CellData v2) throws Exception {
                        return v1.union(v2);
                    }
                });
        boundingBoxCell.setPolygonFromEnvelope();
        Envelope boundingBoxCellEnvelope = boundingBoxCell.getEnvelope();
        Tiles tiles = new Tiles(boundingBoxCellEnvelope);

        for (int i = 0; i < Constant.TILES_COUNT; i++) {
            for (int j = 0; j < Constant.TILES_COUNT; j++) {
                int finalI = i;
                int finalJ = j;
                CellData countCell = cellRDD.reduce(
                        new Function2<CellData, CellData, CellData>(){
                            @Override
                            public CellData call(CellData v1, CellData v2) throws Exception {
                                Integer v1Counts = 0, v2Counts = 0;
                                if (v1.getCounts() >= 0)
                                    v1Counts = v1.getCounts();
                                else if (tiles.getGeometryTiles()[finalI][finalJ].contains(v1.getPolygon()))
                                    v1Counts = 2;
                                else if (tiles.getGeometryTiles()[finalI][finalJ].intersects(v1.getPolygon()))
                                    v1Counts = 1;
                                if (v2.getCounts() >= 0)
                                    v2Counts = v2.getCounts();
                                else if (tiles.getGeometryTiles()[finalI][finalJ].contains(v2.getPolygon()))
                                    v2Counts = 2;
                                else if (tiles.getGeometryTiles()[finalI][finalJ].intersects(v2.getPolygon()))
                                    v2Counts = 1;
                                CellData c = new CellData();
                                c.setCounts(v1Counts+v2Counts);
                                return c;
                            }
                        });
                tiles.setCount(i, j, countCell.getCounts());
            }
        }


        for (int i = 0; i < Constant.TILES_COUNT; i++) {
            for (int j = 0; j < Constant.TILES_COUNT; j++) {
                System.out.print(tiles.getCount(i, j) + " ");
            }
            System.out.println();
        }

        System.out.println("Full Bounding box: " + boundingBoxCell.getEnvelope().toString());
        System.out.println("Full Bounding box polygon: " + boundingBoxCell.getPolygon().toString());
        System.out.println(boundingBoxCellEnvelope.getMinX() + " "+ boundingBoxCellEnvelope.getMinY() + " "+ boundingBoxCellEnvelope.getMaxX() + " "+ boundingBoxCellEnvelope.getMaxY() );

        tiles.dumpDataToCsv();
        //(c1, c2) -> c1.getEnvelope().union(c2.getEnvelope()));
//        System.out.println(paisCSV.first());
//        List<CellData> allCells = cellRDD.collect();
//        ImageData imageData = new ImageData();
//        cellRDD.foreach(x -> imageData.addToGeometryCollection(x.getPolygon()));
//        System.out.println("Cell data: " + cellRDD.first().getPolygon().toString());
//        imageData.updateGeometryCollection();
//        Geometry bb = imageData.getBoundingBox();
//        System.out.println("Bounding Box: " + bb.getGeometryType());
//        System.out.println("GemetryList size: " + imageData.getGeometryList().size());
//        System.out.println("Image data output: " + imageData.getGeometryList().get(0).toString());
//        System.out.println("Cell List " + allCells.get(0).getPolygon().toString());
    }
}