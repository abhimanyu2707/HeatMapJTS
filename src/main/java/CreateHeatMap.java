import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SQLContext;
import org.locationtech.jts.io.ParseException;

public class CreateHeatMap {
    public static void main(String[] args) throws ParseException { //Driver

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
        JavaRDD<CellData> rdd_records = paisCSV.map(
                new Function<String, CellData>() {
                    public CellData call(String line) throws Exception {
                        String[] fields = line.split(",", 3);
                        fields[2] = fields[2].substring(1, fields[2].length() - 1);
                        CellData cellData = new CellData(fields[0], fields[1], fields[2]);
                        return cellData;
                    }
                });
        System.out.println(paisCSV.first());
        System.out.println("Cell data: " + rdd_records.first().getPolygon().toString());
    }
}