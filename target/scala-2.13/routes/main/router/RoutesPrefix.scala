// @GENERATOR:play-routes-compiler
// @SOURCE:D:/LocalCheck/ITSD-DT2023-24-Template/ITSD-DT2023-24-Template/conf/routes
// @DATE:Sun Feb 25 13:31:38 GMT 2024


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
