package io.anagraph.uztskh92

import java.io.File
import java.sql.Timestamp
import java.time.ZonedDateTime

import org.locationtech.rasterframes._
import org.locationtech.rasterframes.datasource.DataSourceOptions
import org.locationtech.rasterframes.rules._
import org.locationtech.rasterframes.util._
import geotrellis.proj4.LatLng
import geotrellis.raster._
import geotrellis.raster.resample.NearestNeighbor
import geotrellis.spark._
import geotrellis.spark.store._
import geotrellis.store.LayerId
//import geotrellis.spark.io._
//import geotrellis.spark.io.avro.AvroRecordCodec
//import geotrellis.spark.io.avro.codecs.Implicits._
//import geotrellis.spark.io.index.ZCurveKeyIndexMethod
//import geotrellis.spark.tiling.ZoomedLayoutScheme
import geotrellis.vector._
import org.apache.avro.generic._
import org.apache.avro.{Schema, SchemaBuilder}
import org.apache.hadoop.fs.FileUtil
import org.apache.spark.sql.functions.{udf => sparkUdf}
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.storage.StorageLevel
//import org.locationtech.rasterframes.TestEnvironment
//import org.scalatest.{BeforeAndAfterAll, Inspectors}
import org.apache.hadoop

import scala.math.{max, min}

import java.net.URI
import geotrellis.store._
import geotrellis.store.s3._
import geotrellis.spark.store._
import org.locationtech.rasterframes.datasource._
import org.locationtech.rasterframes.datasource.raster._
import org.locationtech.rasterframes.datasource.geotrellis._
import org.locationtech.rasterframes._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.SparkConf

object RfBisReader {
  val bucket: String = "o-sdi-general"
  val geotrellisPrefix: String = "gt3catalog"
  val layerName: String = "my_layer"
  val layerZoomLevel: Int = 0
  //val layerId: LayerId = LayerId(layerName, layerZoomLevel)

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", "org.locationtech.rasterframes.util.RFKryoRegistrator")
      .setAppName("Rasteframes0.9 reader")
      .setMaster("local[1]")

    implicit val spark: SparkSession = SparkSession.builder().config(sparkConf).withKryoSerialization.getOrCreate().withRasterFrames

    import spark.implicits._

    // Reading parquet is working.
    // We wil get an error later if in rasterframes if we don't do this first
    //val someMetadata = spark.read.parquet("s3a://geoimagery/metadata_data_frame/geoimagery_2002.parquet")
    //someMetadata.show(1)

    // Reading Geotrellis is not working
    val catalogUri = new URI(s"s3://${bucket}/${geotrellisPrefix}")
    val catalog = spark.read.geotrellisCatalog(catalogUri)
    println(s"Catalog is ${catalog}")



    val layerStoreUri = new URI(s"s3://${bucket}/${geotrellisPrefix}/")
    val layerId = LayerId(layerName, layerZoomLevel)
    //val layer = Layer(catalogUri, layerId)
    val layerDataFrame = spark.read.geotrellis.loadLayer(layerStoreUri, layerId)
    layerDataFrame.show(1)


    println("Done!")
  }
}
