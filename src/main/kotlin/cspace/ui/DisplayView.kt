package cspace.ui

import cspace.ApplicationStarter
import cspace.graphic.GraphBuilder
import cspace.model.ChannelSetting
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.TitledBorder
import javax.swing.event.InternalFrameAdapter
import javax.swing.event.InternalFrameEvent

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

    private fun showGraphViaDefaultWay(selectedChannelSetting: ChannelSetting, component: JComponent) {
        val frameNameIdentifier =
            "${selectedChannelSetting.channelName}[${selectedChannelSetting.channelType}]#${selectedChannelSetting.showType}"
        // 查找是否存在这样的一个窗口，如果存在，只更新窗口显示内容
        var internalFrame: JInternalFrame? = null
        graphDesktopPane.allFrames.forEach {
            if (it.name == frameNameIdentifier) {
                internalFrame = it
                return@forEach
            }
        }
        if (internalFrame == null) {
            internalFrame = JInternalFrame(frameNameIdentifier, true, true)
            internalFrame!!.preferredSize = Dimension(350, 350)
            internalFrame!!.name = frameNameIdentifier
            internalFrame!!.size = internalFrame!!.preferredSize
            internalFrame!!.contentPane = component
            internalFrame!!.isVisible = true
            internalFrame!!.isClosable = true
            internalFrame!!.addInternalFrameListener(object : InternalFrameAdapter() {
                override fun internalFrameClosed(e: InternalFrameEvent?) {
                    super.internalFrameClosed(e)
                }
            })
            graphDesktopPane.add(internalFrame)
            logger.info("Add an internal frame into graphDesktopPane with identifier $frameNameIdentifier")
        } else {
            internalFrame!!.contentPane = component
        }

    }


    private fun showGraphViaAlwaysNew(selectedChannelSetting: ChannelSetting, component: JComponent) {
        val frameNameIdentifier =
            "${selectedChannelSetting.channelName}[${selectedChannelSetting.channelType}]#${selectedChannelSetting.showType}(${System.currentTimeMillis()})"
        val internalFrame = JInternalFrame(frameNameIdentifier, true, true)
        internalFrame.name = frameNameIdentifier
        internalFrame.preferredSize = Dimension(350, 350)
        internalFrame.size = internalFrame.preferredSize
        internalFrame.contentPane = component
        internalFrame.isVisible = true
        internalFrame.isClosable = true
        internalFrame.addInternalFrameListener(object : InternalFrameAdapter() {

            override fun internalFrameClosed(e: InternalFrameEvent?) {
                super.internalFrameClosed(e)

            }
        })
        graphDesktopPane.add(internalFrame)
        logger.info("Add an internal frame into graphDesktopPane with identifier $frameNameIdentifier")

    }

    //! ------------------------ 业务方法 -------------------------
    fun acceptGraphBuilder(builder: GraphBuilder, data: Any) {
        val component = builder.build(data)
        if (component != null) {
            // 显示
            val channelView = ApplicationStarter.getContext().getInstance(ChannelView::class.java)
            val selectedChannelSetting = channelView.getSelectedChannelSetting() ?: return
            when (selectedChannelSetting.showArea) {
                ChannelSetting.SHOW_AREA_DEFAULT -> {
                    showGraphViaDefaultWay(selectedChannelSetting, component)
                }
                ChannelSetting.SHOW_AREA_ALWAYS_NEW -> {
                    showGraphViaAlwaysNew(selectedChannelSetting, component)
                }
                else -> {
                    JOptionPane.showMessageDialog(
                        this,
                        "Can't create graphic because of invalid \"show area \" value",
                        "error",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }
        }else {
            JOptionPane.showMessageDialog(
                this,
                "Can't create graphic because of an internal error",
                "error",
                JOptionPane.ERROR_MESSAGE
            )
        }

    }
}