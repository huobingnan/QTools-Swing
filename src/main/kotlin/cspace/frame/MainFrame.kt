package cspace.frame

import cspace.ApplicationStarter
import cspace.ui.MainView
import cspace.util.AssetsResolver
import cspace.util.JComponentInitializer
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.lang.Exception
import javax.swing.ImageIcon
import javax.swing.JFrame

class MainFrame: JFrame() {

    private val logger = LoggerFactory.getLogger(MainFrame::class.java)

    init {
        title = "QTools-V1.0.0(Beta) Preview version"
        contentPane = ApplicationStarter.getContext().getInstance(MainView::class.java)
        size = Dimension(1000, 800)
        preferredSize = Dimension(1000, 800)
        try {
            iconImage = ImageIcon(AssetsResolver.getAsset("application.icon")).image
            logger.info("Application icon load successfully!")
        }catch (ex: Exception) {
            logger.error("Application icon load failed...")
        }

        JComponentInitializer.frameCommonInitialization(this)
    }
}