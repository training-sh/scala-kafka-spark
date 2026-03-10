import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler
import java.io.File

object AvroGen {

  def main(args: Array[String]): Unit = {

    val schemaFile = new File("src/main/avro/invoice.avsc")

    val schema = new Schema.Parser().parse(schemaFile)


    val compiler = new SpecificCompiler(schema)

    compiler.compileToDestination(
      schemaFile.getParentFile,
      new File("src/main/java")
    )

    println("Avro Java classes generated in src/main/java")
  }
}
