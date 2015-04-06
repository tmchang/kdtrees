package kdtrees.kdtree

class KDTreeNode[A <: KDData[A]] (val datum: A) {
  var left: Option[KDTreeNode[A]] = None
  var right: Option[KDTreeNode[A]] = None
  
}