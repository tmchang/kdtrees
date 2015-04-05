package kdtrees.kdtree

class Profile(val attributes: Array[Double], val name: String) extends KDData[Profile] { 
  
  val dimensions = attributes.length
  
  override def distance(other: Profile): Double = {
    var toReturn: Double = 0.0
    for (i <- 0 to dimensions){
      toReturn += math.pow((other.attributes(i) - attributes(i)),2)
    }
    toReturn = math.sqrt(toReturn)
    return toReturn
  }
  
  override def compDim(dim: Int, other: Profile): Double = {
    if (dim >= 0 && dim < dimensions) math.abs(attributes(dim)-other.attributes(dim))
    else throw new NoSuchElementException()
  }
  
  override def lessThan(other: Profile)(dim: Int): Boolean = {
    if (dim >= 0 && dim < dimensions) (attributes(dim) < other.attributes(dim))
    else throw new NoSuchElementException()
  }
}
