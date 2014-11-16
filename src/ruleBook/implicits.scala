package object ruleBook {  
   import scala.language.implicitConversions
   
   implicit class BoardDimension(val m:Int) {
    def x(n:Int) = SpaceList(Seq.fill(m,n){None})
   }
}