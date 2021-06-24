package cspace.frame

import cspace.ApplicationStarter
import cspace.ui.MainView
import cspace.util.JComponentInitializer
import java.awt.Dimension
import javax.swing.JFrame

class MainFrame: JFrame() {

    init {
        title = "QTools-V1.0.0(Beta) Preview version"
        contentPane = ApplicationStarter.getContext().getInstance(MainView::class.java)
        size = Dimension(800, 500)
        preferredSize = Dimension(800, 500)
        JComponentInitializer.frameCommonInitialization(this)
    }
}