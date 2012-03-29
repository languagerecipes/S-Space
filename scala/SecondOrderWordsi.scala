import edu.ucla.sspace.basis.BasisMapping
import edu.ucla.sspace.dependency.CoNLLDependencyExtractor
import edu.ucla.sspace.matrix.Matrices
import edu.ucla.sspace.matrix.MatrixIO
import edu.ucla.sspace.matrix.MatrixIO.Format
import edu.ucla.sspace.text.DependencyFileDocumentIterator
import edu.ucla.sspace.util.SerializableUtil
import edu.ucla.sspace.vector.CompactSparseVector
import edu.ucla.sspace.vector.VectorMath

import scala.collection.JavaConversions.asScalaIterator
import scala.collection.JavaConversions.seqAsJavaList

import java.io.File

/**
 * Creates feature vectors using the second order wordsi model from a Bigram
 * matrix.
 */

val bigrams = MatrixIO.readSparseMatrix(args(0), Format.SVDLIBC_SPARSE_TEXT)
val basis:BasisMapping[String, String] = SerializableUtil.load(args(1))
basis.setReadOnly(true)
val parser = new CoNLLDependencyExtractor()
val docIter = new DependencyFileDocumentIterator(args(2))
val contexts = for (document <- docIter) yield {
    val reader = document.reader
    val header = reader.readLine
    val tree = parser.readNextTree(reader)
    val context = new CompactSparseVector(bigrams.columns)
    tree.map(n => basis.getDimension(n.word)).filter(_>=0).foreach(
        n => VectorMath.add(context,bigrams.getRowVector(n)))
    context
}

MatrixIO.writeMatrix(Matrices.asSparseMatrix(contexts.toList),
                     new File(args(3)), Format.SVDLIBC_SPARSE_TEXT)
