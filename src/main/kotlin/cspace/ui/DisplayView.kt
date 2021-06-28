package cspace.ui

import cspace.ApplicationStarter
import cspace.graphic.GraphBuilder
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JDesktopPane
import javax.swing.JInternalFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.border.TitledBorder

/**
 * 结果展示区域
 * @author: huobn
 */
class DisplayView: JPanel() {

    private val logger = LoggerFactory.getLogger(DisplayView::class.java)

    private val graphDesktopPane: JDesktopPane by lazy {
        val pane = JDesktopPane()
        pane.isVisible = true

        pane
    }

    init {
        layout = BorderLayout()
        border = TitledBorder("graphic")
        add(graphDesktopPane, BorderLayout.CENTER)
    }

    //! ------------------------ 业务方法 -------------------------
    fun acceptGraphBuilder(builder: GraphBuilder, data: Any) {
        val component = builder.build(data)
        if (component != null) {
            // 显示
            val channelView = ApplicationStarter.getContext().getInstance(ChannelView::class.java)
            val selectedChannelSetting = channelView.getSelectedChannelSetting() ?: return
            val frameNameIdentifier = "${selectedChannelSetting.channelName}[${selectedChannelSetting.channelType}]#${selectedChannelSetting.showType}"
            val internalFrame = JInternalFrame(frameNameIdentifier, true, true)
            internalFrame.preferredSize = Dimension(350, 350)
            internalFrame.size = internalFrame.preferredSize
            internalFrame.contentPane = component
            internalFrame.isVisible = true
            internalFrame.isClosable = true
            internalFrame.defaultCloseOperation = JInternalFrame.EXIT_ON_CLOSE
            graphDesktopPane.add(internalFrame)
            logger.info("Add an internal frame into graphDesktopPane with identifier $frameNameIdentifier")
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Can't create graphic because of an internal error",
                "error",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }
}