package helper

import java.util.Properties

import scala.io.Source

class Config extends Properties{
  def getString(name: String): String = {
    this.getProperty(name)
  }

  def getStringSequence(name: String, delimiter: String = " "): Seq[String] = {
    this.getProperty(name).split(" ")
  }

  def getInt(name: String): Int = {
    try {
      this.getProperty(name).toInt
    } catch {
      case e: NumberFormatException => {
        Console.err.println("Cannot parse property '" + name + "' with value '" + this.getProperty(name) + "' to int.")
        throw e
      }
    }
  }
}

object Config {
  def parseCommandLineArgsToConfig(args: Array[String]): Config = {
    val argsMap = args.grouped(2).collect {
      case Array(key, value) => key -> value
    }.toMap

    val myConfig = new Config()
    myConfig.load(Source.fromFile(argsMap("-c")).bufferedReader())
    myConfig
  }
}
