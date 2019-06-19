import helper.Config

object Main {
  def main(args: Array[String]): Unit = {
    val config = Config.parseCommandLineArgsToConfig(args)
    val fileNames = config.getStringSequence("file.name")
    val path = config.getString("file.path")
    val region = config.getString("aws.s3.region")
    val bucket = config.getString("aws.s3.bucket")

    for (file <- fileNames) {
      aws.S3.saveFileToS3(path, file, region, bucket)
    }
  }
}
