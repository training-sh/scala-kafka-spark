package workshop.s003_data_types

// Scala primitives are actually Classes in Scala
// unlike Java primitives
// important: When Scala generate byte codes for primitives,
// it generate native JVM primitives

object PrimitiveTypes extends  App {

  val enabled: Boolean  = true; //  true or false

  //Byte       8-bit signed two's complement integer (-2^7 to 2^7-1, inclusive)
  // -128 to 127
  val byteA: Byte = 65;

  //   Short      16-bit signed two's complement integer (-2^15 to 2^15-1, inclusive)
  //  32,768 to 32,767
  val s: Short = 3200;

  //  Int        32-bit two's complement integer (-2^31 to 2^31-1, inclusive)
  //  2,147,483,648 to 2,147,483,647
  val i : Int = 2147483646;
//
//  Long       64-bit two's complement integer (-2^63 to 2^63-1, inclusive)
//  -9,223,372,036,854,775,808 to +9,223,372,036,854,775,807
  val l : Long = 9223372036854775806L; // small l or L at the end


//  Float      32-bit IEEE 754 single-precision float
//    1.40129846432481707e-45 to 3.40282346638528860e+38 (positive or negative)

  val f: Float = 3.14f; // f or F at the end

//  Double     64-bit IEEE 754 double-precision float
//    4.94065645841246544e-324d to 1.79769313486231570e+308d (positive or negative)

  val d: Double = 3.14;

//  Char       16-bit unsigned Unicode character (0 to 2^16-1, inclusive)
//  0 to 65,535

  val c : Char = 'C';

  // String     a sequence of Chars

  val str: String = "Hello";


}
