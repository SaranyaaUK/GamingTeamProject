// @GENERATOR:play-routes-compiler
// @SOURCE:D:/LocalCheck/ITSD-DT2023-24-Template/ITSD-DT2023-24-Template/conf/routes
// @DATE:Sun Feb 25 13:31:38 GMT 2024

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseGameScreenController GameScreenController = new controllers.ReverseGameScreenController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseGameScreenController GameScreenController = new controllers.javascript.ReverseGameScreenController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
  }

}
