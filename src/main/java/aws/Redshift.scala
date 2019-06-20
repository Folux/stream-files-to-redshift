package aws

import java.sql.DriverManager

import helper.Config

object Redshift {
  private def getRedshiftConfig(generalConfig: Config): Config = {
    val redshiftConfig = new Config
    redshiftConfig.setString("region", generalConfig.getString("aws.region"))
    redshiftConfig.setString( "clusterIdentifier", generalConfig.getString("aws.redshift.clusterIdentifier"))
    redshiftConfig.setString( "database", generalConfig.getString("aws.redshift.database"))
    redshiftConfig.setString( "port", generalConfig.getString("aws.redshift.port"))
    redshiftConfig.setString( "user", generalConfig.getString("aws.redshift.user"))
    redshiftConfig.setString( "password", generalConfig.getString("aws.redshift.password"))
    redshiftConfig.setString( "schema", generalConfig.getString("aws.redshift.schema"))
    redshiftConfig.setString( "iamRole", generalConfig.getString("aws.redshift.iamRole"))
    redshiftConfig
  }

  private def getRedshiftUrl(redshiftConfig: Config): String = {
    "jdbc:redshift://" + redshiftConfig.getString("clusterIdentifier") +
      "." + redshiftConfig.getString("region") + ".redshift.amazonaws.com:" +
      redshiftConfig.getString("port") + "/" + redshiftConfig.getString("database")
    "jdbc:redshift://tier-dwh.cbtj0lksmd5y.eu-central-1.redshift.amazonaws.com:5439/bi"
  }

  def copyFromS3ToRedshift(generalConfig: Config, table: String, bucket: String, file: String): Unit = {
    print("Loading table " + table + " to redshift...")
    val redshiftConfig = getRedshiftConfig(generalConfig)
    try {
      Class.forName("com.amazon.redshift.jdbc41.Driver")
      val connection = DriverManager.getConnection(getRedshiftUrl(redshiftConfig), redshiftConfig)
      val statement = connection.createStatement()
      val command =
        "COPY " + redshiftConfig.getString("schema") + "." + redshiftConfig.getString("table") + "\n" +
        "FROM 'S3://" + bucket + "/" + file + "'\n"
        "IAM_ROLE '" + redshiftConfig.getString("iamRole") + "'\n" +
        "FORMAT AS JSON 'auto';"
      statement.execute(command)
      statement.close()
      connection.close()
      println(" done.")
    } catch {
      case e: Exception => {
        println(" error!")
        throw e
      }
    }
  }
}
