package cspace.graphic

import javax.swing.JComponent

interface GraphBuilder {

    fun build(result: Any): JComponent?
}