package kdtrees.kdtree

class Profile(val attributes: Array[Double], val name: String) extends KDData[Profile] { 
  
  val dimensions = attributes.length
  
  override def distance(other: Profile): Double = {
    var toReturn: Double = 0.0
    for (i <- 0 until dimensions){
      toReturn += math.pow((other.attributes(i) - attributes(i)),2)
    }
    toReturn = math.sqrt(toReturn)
    return toReturn
  }
  
  override def compDim(dim: Int, other: Profile): Double = {
    if (dim >= 0 && dim < dimensions) (attributes(dim)-other.attributes(dim))
    else throw new NoSuchElementException()
  }
  
  
  override def toString(): String = {
    
    val sb = new StringBuilder()
    for (d <- attributes) {
      sb.append(d + "\n")
    }
    
    return "Attributes: \n" + sb.toString +
              "Name: " + name
  }
  
  /**
   * returns true if Profile that is equivalent to this Profile
   * @param that - the other Profile to check
   * @return - Boolean, true if this is equal to that in name and attributes
   */
  def equals(that: Profile): Boolean ={
    return (this.attributes.deep == that.attributes.deep) &&
              (this.name == that.name)
  }
}

object Profile {
  
  def main(args: Array[String]) {
    val test = new Profile(Array(1.0,2.0,3.0), "a")
    println(test.toString())
    val t1 = new Profile(Array(3.0, 4.0),"")
    val t2 = new Profile(Array(0.0, 0.0), "")
    println(t1.distance(t2) == 5.0)
    println(t2.distance(t1) == 5.0)
    println(t1.compDim(0, t2) == 3.0)
    println(t2.compDim(1, t1) == 4.0)
    val t3 = new Profile(Array(1.0,2.0,-1.0), "")
    val t4 = new Profile(Array(-2.0,2.0,-5.0), "")
    println(t3.distance(t4) == 5.0)
    println(t4.compDim(0,t3) == 3.0)
    println(t4.compDim(2,t3) == 4.0)
    println(t3.compDim(1,t4) == 0.0)

  }
}
