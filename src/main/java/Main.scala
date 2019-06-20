import helper.Config

object Main {
  def main(args: Array[String]): Unit = {
    val config = Config.parseCommandLineArgsToConfig(args)
    val tables = config.getStringSequence("aws.redshift.table")
    val region = config.getString("aws.region")
    val bucket = config.getString("aws.s3.bucket")

    for (table <- tables) {
      val fileName = config.getString(table + ".fileName")
      val filePath = config.getString(table + ".filePath")
      aws.S3.saveFileToS3(filePath, fileName, region, bucket)
      aws.Redshift.copyFromS3ToRedshift(config, table, bucket, fileName)
    }
  }
}
