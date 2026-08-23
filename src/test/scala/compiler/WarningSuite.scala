package compiler

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters.*
import scala.util.Using

class WarningSuite extends munit.FunSuite:

  private val compiler = Compiler(Options())
  private val root = Path.of("tests", "warn")

  private val programs =
    Using.resource(Files.walk(root)) { paths =>
      paths
        .iterator()
        .asScala
        .filter(path => Files.isRegularFile(path))
        .filter(_.toString.endsWith(".neo"))
        .toList
        .sortBy(_.toString)
    }

  programs.foreach { path =>
    val relativePath = root.relativize(path)

    test(s"$relativePath compiles with warnings"):
      val compilation =
        SourceFile.read(path).flatMap(compiler.compile)

      assert(
        compilation.succeeded,
        s"$relativePath failed: ${compilation.diagnostics.mkString(", ")}"
      )
      assert(
        compilation.hasWarnings,
        s"$relativePath was expected to produce a warning"
      )
  }
