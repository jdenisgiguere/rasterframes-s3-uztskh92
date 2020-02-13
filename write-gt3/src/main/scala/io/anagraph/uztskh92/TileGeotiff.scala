package io.anagraph.uztskh92

import java.net.URI

import geotrellis.raster.{Tile, TileFeature}
import geotrellis.vector._
import geotrellis.layer._
import geotrellis.raster.geotiff._
import geotrellis.spark.RasterSourceRDD
import geotrellis.spark.ingest.MultibandIngest
import geotrellis.store._
import geotrellis.spark.store._
import geotrellis.spark.store.hadoop._
import geotrellis.spark.store.s3._
import geotrellis.store.hadoop.{HadoopAttributeStore, SerializableConfiguration}
import geotrellis.store.index.ZCurveKeyIndexMethod
import geotrellis.store.s3._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
// import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession

object TileGeotiff {
  val prefix: String = "gt3_catalog"
  val attributeStore = new S3AttributeStore("o-sdi-general", "gt3catalog")
  val layerWriter = new S3LayerWriter(attributeStore, "o-sdi-general", "gt3catalog")


  def main(args: Array[String]): Unit = {
    val conf =
      new SparkConf()
        .setMaster("local[2]")
        .setAppName("Spark Tiler")
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")


    implicit val sc: SparkContext = SparkContext.getOrCreate(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()


    val geoTiffPath: GeoTiffPath = GeoTiffPath("s3://o-sdi-general/tif_files/tiled_U-19OrthoSectorTile1_2012x2012.tif")
    val rasterSource = GeoTiffRasterSource(geoTiffPath)
    val crs = rasterSource.crs
    val scheme = FloatingLayoutScheme(1006, 1006)
    val layout = scheme.levelFor(rasterSource.extent, rasterSource.cellSize).layout

    val reprojectedSource = rasterSource.reprojectToGrid(crs, layout)

    val rdd = RasterSourceRDD.spatial(reprojectedSource, layout)

    val actualKeys = rdd.keys.collect().sortBy { key => (key.col, key.row) }
    println(actualKeys)

   val inputRdd = RasterSourceRDD.spatial(rasterSource, layout)
    val layerId = LayerId("my_layer", 0)
    layerWriter.write(layerId, inputRdd, ZCurveKeyIndexMethod)

    sc.stop()
    println("Done!")

  }


}
