package aws

import com.amazonaws.{AmazonServiceException, SdkClientException}
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

object S3 {
  def saveFileToS3(path: String, fileName: String, region: String, bucket: String): Unit = {
    val fullPath =
      if (path.endsWith("/"))
        path + fileName
      else
        path + "/" + fileName

    print("Uploading from file " + fileName + " to S3 bucket " + bucket + "...")
    try {
      val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
        .withCredentials(new ProfileCredentialsProvider())
        .withRegion("eu-central-1")
        .build()
      val file = scala.io.Source.fromFile(fullPath)
      s3Client.putObject(bucket, fileName, file.mkString)
      file.close
      println("done.")
    } catch {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      case e: AmazonServiceException => {
        println("error!")
        throw e
      }
      // Amazon S3 couldn't be contacted for a response, or the client
      // couldn't parse the response from Amazon S3.
      case e: SdkClientException => {
        println("error!")
        throw e
      }
    }
  }
}
