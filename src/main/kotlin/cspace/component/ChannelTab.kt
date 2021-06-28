package cspace.component

import cspace.ApplicationStarter
import cspace.dialog.FrameOptionDialog
import cspace.model.AnalyseKeyFrame
import cspace.util.AssetsResolver
import cspace.util.JComponentInitializer
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*

/**
 * 分析通道
 * @author huobn
 */
class ChannelTab: JPanel() {

    private val FRAME_BUTTON_SIZE = Dimension(80,80)

    // 分析关键帧的容器
    private val frameContainer: JPanel by lazy {
        val box = JPanel()
        box.layout = FlowLayout().apply {
            hgap = 10
            vgap = 10
            alignment = FlowLayout.LEFT

        }
        val addButton = JButton(ImageIcon(AssetsResolver.getAsset("channel-tab.new-frame.icon")))
        addButton.isOpaque = false
        addButton.size = FRAME_BUTTON_SIZE
        addButton.preferredSize = FRAME_BUTTON_SIZE
        addButton.toolTipText = "new frame"
        // 添加关键帧事件初始化
        addButton.addActionListener {
            val dialog = ApplicationStarter.getContext().getInstance(FrameOptionDialog::class.java)
            JComponentInitializer.showDialogSupport(dialog)
            if (!dialog.isExitOnApprove()) return@addActionListener
            var frame = dialog.getAnalyseKeyFrame()
            // 检查重复
            while (dialog.isExitOnApprove() && checkFrameDuplication(box, frame.name)) {
                JOptionPane.showMessageDialog(dialog, "duplicated frame ${frame.name}!", "invalid input", JOptionPane.ERROR_MESSAGE)
                JComponentInitializer.showDialogSupport(dialog)
                frame = dialog.getAnalyseKeyFrame(frame)
            }

            if (dialog.isExitOnApprove()) {
                // 添加显示
                box.add(
                    KeyFrameButton(box.componentCount, frame).apply {
                        refreshUI()
                    },
                    box.componentCount - 1
                )
            }

        }
        box.add(addButton)
        box
    }

    init {
        layout = BorderLayout()
        add(JScrollPane(frameContainer), BorderLayout.CENTER)
    }

    // ---------------------- 业务方法 --------------------------

    private fun checkFrameDuplication(pane: JPanel, name: String): Boolean {
        var duplicated = false
        if (pane.components != null) {
            pane.components.forEach {
                if (it is KeyFrameButton) {
                    if (it.keyFrame.name == name) {
                        duplicated = true
                        return@forEach
                    }
                }

            }
        }
        return duplicated
    }

    fun getAnalyseKeyFrameList(): List<AnalyseKeyFrame> {
        return frameContainer.components.filterIsInstance<KeyFrameButton>().map { (it as KeyFrameButton).keyFrame }
    }
}