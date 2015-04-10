package kdtrees.kdtree

/**
 * This trait may be extended or mixed in by any class which represents data
 * that (1) has a distance relative to other data of the same type and (2)
 * each of its dimensions has a distance relative the same dimension of
 * other data of the same type.
 * 
 * Any data used in a kd-tree should extend or mix in this trait.
 */
trait KDData[A] {
  // The number of dimensions of the data stored
  val dimensions: Int
  
  /**
   * Computes the distance between this datum and another datum
   * 
   * @param other A datum
   * @return      The non-negative distance between other's datum and this datum
   */
  def distance(other: A): Double
  
  /**
   * Computes the difference between a single dimension of this datum and another datum
   * 
   * @param dim   The dimension of the data to compare
   * @param other A datum
   * @return      The difference between dim of this datum and dim of the other datum. The result is
   *                  positive if this datum's dim is greater than other datum's dim,
   *                  negative if this datum's dim is less than other datum's dim, and
   *                  zero if they are equal.
   */
  def compDim(dim: Int, other: A): Double
  
  
}
