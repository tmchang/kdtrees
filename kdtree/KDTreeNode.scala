package kdtrees.kdtree

/**
 * Represents a node used in KDTree
 * @constructor - datum of type A which is a subtype of KDData[A]
 */
class KDTreeNode[A <: KDData[A]] (val datum: A) {
  var left: Option[KDTreeNode[A]] = None
  var right: Option[KDTreeNode[A]] = None
  
}