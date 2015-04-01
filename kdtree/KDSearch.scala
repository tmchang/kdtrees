package kdtrees.kdtree

/**
 * This trait may be extended or mixed in by any class which holds a 
 * collection of data and can search the data to find closest matches 
 * to a given datum. The parameterized type A is the type of the data.
 */
trait KDSearch[A] {
  /**
   * Given some datum, finds the nearest neighbor, or closest match, to the 
   * datum from a collection of data which have the same structure.
   * 
   * @param target A datum to find the closest match to
   * @return Optionally returns the closest match found to target
   */
  def findNN(target: A): Option[A]
}
