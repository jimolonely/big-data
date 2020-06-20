
package com.jimo {


  object Test {

    import com.jimo.web.Web

    def main(args: Array[String]): Unit = {
      val web = new Web()
    }
  }

  package pkg {

    class PkgDemo {
    }

  }

  package web {

    class Web {
      @_root_.scala.beans.BeanProperty
      var url: String = _
    }

  }

}
